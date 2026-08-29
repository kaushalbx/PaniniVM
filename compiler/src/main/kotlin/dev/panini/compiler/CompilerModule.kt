package dev.panini.compiler

import dev.panini.execution.PvmScript
import dev.panini.execution.PvmScriptStatement
import dev.panini.execution.PuranaPratyayaResolver
import dev.panini.execution.SamjnaParameter
import dev.panini.execution.SamjnaSignature
import dev.panini.execution.SamjnaSignatureCompiler
import dev.panini.execution.SamjnaValueType
import dev.panini.execution.TaddhitaInheritanceEngine
import dev.panini.execution.TaddhitaStructEngine
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadikaIdentity
import dev.panini.vyakaranam.ast.SankhyaPuranaPada
import dev.panini.vyakaranam.ast.SubantaPada
import java.io.File
import java.util.Base64

data class PaniniModuleSource(
    val name: String,
    val content: String,
    val isEntryPoint: Boolean = true,
)

data class PaniniModuleDescriptor(
    val name: String,
    val sources: List<PaniniModuleSource>,
    val dependencies: List<PaniniModuleMetadata> = emptyList(),
) {
    init {
        require(name.isNotBlank()) { "Panini module name must not be blank." }
        require(sources.isNotEmpty()) { "A Panini module must contain source files." }
        require(sources.map(PaniniModuleSource::name).distinct().size == sources.size) {
            "Panini module source names must be unique."
        }
        require(dependencies.map(PaniniModuleMetadata::moduleName).distinct().size == dependencies.size) {
            "Panini module dependencies must have unique module names."
        }
        dependencies.flatMap { dependency -> dependency.procedures.map { it.symbol to dependency.moduleName } }
            .groupBy { it.first }.entries.firstOrNull { entry ->
                entry.value.map { it.second }.distinct().size > 1
            }?.let { ambiguous ->
                error("Ambiguous imported Panini symbol '${ambiguous.key}' from ${ambiguous.value.map { it.second }.distinct()}.")
            }
    }

    companion object {
        /** Discovers a module, optionally honoring a `panini.module` descriptor. */
        fun discover(sourceDir: File, dependencies: List<PaniniModuleMetadata> = emptyList()): PaniniModuleDescriptor {
            require(sourceDir.isDirectory) { "Panini module directory not found: ${sourceDir.absolutePath}" }
            val config = File(sourceDir, "panini.module").takeIf(File::isFile)
                ?.readLines()?.mapNotNull { line ->
                    line.substringBefore('#').trim().takeIf(String::isNotEmpty)?.let { entry ->
                        entry.substringBefore('=').trim() to entry.substringAfter('=', "").trim()
                    }
                }?.toMap().orEmpty()
            val allFiles = sourceDir.walkTopDown().filter { it.isFile && it.extension == "pvm" }
                .sortedBy { it.relativeTo(sourceDir).invariantSeparatorsPath }.toList()
            val selected = config["sources"]?.split(',')?.map(String::trim)?.filter(String::isNotEmpty)
                ?.map { File(sourceDir, it) } ?: allFiles
            val configuredEntries = config["entries"]?.split(',')?.map(String::trim)?.filter(String::isNotEmpty)?.toSet()
            val inferredEntries = allFiles.filter { file ->
                file.nameWithoutExtension.contains("mukhya", ignoreCase = true) ||
                    !file.nameWithoutExtension.endsWith("_lib") && allFiles.none {
                        it.nameWithoutExtension.contains("mukhya", ignoreCase = true)
                    }
            }.map { it.relativeTo(sourceDir).invariantSeparatorsPath }.toSet()
            val entries = configuredEntries ?: inferredEntries
            return PaniniModuleDescriptor(
                name = config["name"].orEmpty().ifBlank { sourceDir.name },
                sources = selected.map { file ->
                    require(file.isFile && file.extension == "pvm") { "Invalid Panini source: ${file.path}" }
                    val relative = file.relativeTo(sourceDir).invariantSeparatorsPath
                    PaniniModuleSource(relative, file.readText(), relative in entries)
                },
                dependencies = dependencies,
            )
        }
    }
}

enum class PaniniSymbolVisibility { PUBLIC, INTERNAL }

data class PaniniExportedProcedure(
    val symbol: String,
    val methodName: String,
    val domain: String?,
    val parameters: List<SamjnaParameter>,
    val resultType: SamjnaValueType?,
    val resultSchema: String?,
)

data class PaniniModuleMetadata(
    val formatVersion: Int = CURRENT_FORMAT_VERSION,
    val moduleName: String,
    val className: String,
    val procedures: List<PaniniExportedProcedure>,
    val inheritance: Map<String, String> = emptyMap(),
    val schemas: Map<String, List<String>> = emptyMap(),
) {
    init {
        require(formatVersion == CURRENT_FORMAT_VERSION) {
            "Unsupported Panini module metadata version $formatVersion; expected $CURRENT_FORMAT_VERSION."
        }
    }

    companion object { const val CURRENT_FORMAT_VERSION = 1 }
}

object PaniniModuleMetadataCodec {
    fun encode(metadata: PaniniModuleMetadata): String = buildString {
        appendLine("format=${metadata.formatVersion}")
        appendLine("module=${encoded(metadata.moduleName)}")
        appendLine("class=${encoded(metadata.className)}")
        metadata.procedures.sortedBy(PaniniExportedProcedure::symbol).forEach { procedure ->
            val parameters = procedure.parameters.joinToString(",") { "${encoded(it.nameStem)}:${it.type.name}" }
            appendLine(listOf(
                "procedure", encoded(procedure.symbol), encoded(procedure.methodName),
                encoded(procedure.domain.orEmpty()), parameters,
                procedure.resultType?.name.orEmpty(), encoded(procedure.resultSchema.orEmpty()),
            ).joinToString("|"))
        }
        metadata.inheritance.toSortedMap().forEach { (child, parent) ->
            appendLine("inheritance|${encoded(child)}|${encoded(parent)}")
        }
        metadata.schemas.toSortedMap().forEach { (name, fields) ->
            appendLine("schema|${encoded(name)}|${fields.joinToString(",", transform = ::encoded)}")
        }
    }

    fun decode(source: String): PaniniModuleMetadata {
        val lines = source.lineSequence().filter(String::isNotBlank).toList()
        val format = lines.firstOrNull { it.startsWith("format=") }?.substringAfter('=')?.toInt()
            ?: error("Panini module metadata lacks a format version.")
        val module = lines.firstOrNull { it.startsWith("module=") }?.substringAfter('=')?.let(::decoded)
            ?: error("Panini module metadata lacks a module name.")
        val className = lines.firstOrNull { it.startsWith("class=") }?.substringAfter('=')?.let(::decoded)
            ?: error("Panini module metadata lacks a class name.")
        val procedures = lines.filter { it.startsWith("procedure|") }.map { line ->
            val parts = line.split('|')
            val parameters = parts[4].takeIf(String::isNotEmpty)?.split(',')?.map { parameter ->
                val pair = parameter.split(':', limit = 2)
                SamjnaParameter(decoded(pair[0]), SamjnaValueType.valueOf(pair[1]))
            }.orEmpty()
            PaniniExportedProcedure(
                decoded(parts[1]), decoded(parts[2]), decoded(parts[3]).ifBlank { null }, parameters,
                parts[5].takeIf(String::isNotEmpty)?.let(SamjnaValueType::valueOf),
                decoded(parts[6]).ifBlank { null },
            )
        }
        val inheritance = lines.filter { it.startsWith("inheritance|") }.associate { line ->
            val parts = line.split('|'); decoded(parts[1]) to decoded(parts[2])
        }
        val schemas = lines.filter { it.startsWith("schema|") }.associate { line ->
            val parts = line.split('|'); decoded(parts[1]) to parts[2].takeIf(String::isNotEmpty)
                ?.split(',')?.map(::decoded).orEmpty()
        }
        return PaniniModuleMetadata(format, module, className, procedures, inheritance, schemas)
    }

    fun read(file: File): PaniniModuleMetadata = decode(file.readText())
    fun write(metadata: PaniniModuleMetadata, file: File) {
        file.parentFile?.mkdirs()
        file.writeText(encode(metadata))
    }

    private fun encoded(value: String): String = Base64.getUrlEncoder().withoutPadding()
        .encodeToString(value.toByteArray(Charsets.UTF_8))
    private fun decoded(value: String): String = if (value.isEmpty()) "" else
        String(Base64.getUrlDecoder().decode(value), Charsets.UTF_8)
}

internal data class AnalyzedProcedure(
    val source: PaniniModuleSource,
    val definition: PvmScriptStatement.SamjnaDefinition,
    val symbol: String,
    val localSymbol: String,
    val domain: String?,
    val signature: SamjnaSignature,
    val visibility: PaniniSymbolVisibility,
    val methodName: String,
)

internal data class AnalyzedPaniniModule(
    val descriptor: PaniniModuleDescriptor,
    val statements: Map<PaniniModuleSource, List<PvmScriptStatement>>,
    val procedures: List<AnalyzedProcedure>,
    val inheritance: Map<String, String>,
    val schemas: Map<String, List<String>>,
)

internal object PaniniModuleAnalyzer {
    fun analyze(descriptor: PaniniModuleDescriptor): AnalyzedPaniniModule {
        val statements = descriptor.sources.associateWith { PvmScript.parse(it.content) }
        val procedures = statements.flatMap { (source, unitStatements) ->
            val fallbackDomain = unitStatements.filterIsInstance<PvmScriptStatement.AdhikaraDefinition>()
                .firstOrNull()?.scope?.domain?.let(CompilerSymbols::stem)
            unitStatements.filterIsInstance<PvmScriptStatement.SamjnaDefinition>().map { definition ->
                val symbol = CompilerSymbols.stem(definition.nameSegmented)
                val domain = definition.domainStem ?: fallbackDomain
                val signature = inferSignature(definition)
                AnalyzedProcedure(
                    source, definition, symbol, CompilerSymbols.localStem(symbol), domain,
                    signature,
                    if (definition.procedure.modifiers.isInternal) PaniniSymbolVisibility.INTERNAL else PaniniSymbolVisibility.PUBLIC,
                    "samjna_${stableMethodSuffix(source.name, domain, symbol, signature, definition)}",
                )
            }
        }
        procedures.groupBy {
            listOf(
                it.domain.orEmpty(), it.localSymbol, signatureKey(it.signature),
                it.definition.procedure.modifiers.toString(),
            )
        }
            .entries.firstOrNull { it.value.size > 1 }?.let { duplicate ->
                error("Duplicate Panini procedure '${duplicate.value.first().localSymbol}' in domain '${duplicate.value.first().domain.orEmpty()}'.")
            }
        val inheritance = statements.values.flatten().filterIsInstance<PvmScriptStatement.AdhikaraDefinition>()
            .mapNotNull { TaddhitaInheritanceEngine.detectInheritanceAdhikara(it.scope.domain) }
            .associate { it.childStem to it.parentStem }
        val schemas = statements.values.flatten().filterIsInstance<PvmScriptStatement.Sentence>()
            .mapNotNull { TaddhitaStructEngine.detectResultSchema(it.text, it.ukti) }
            .associate { it.nameStem to it.fields }
        return AnalyzedPaniniModule(descriptor, statements, procedures, inheritance, schemas)
    }

    private fun inferSignature(definition: PvmScriptStatement.SamjnaDefinition): SamjnaSignature {
        val declared = SamjnaSignatureCompiler.compile(definition.body)
        if (declared.parameters.isNotEmpty()) return declared
        val padas = definition.body.flatMap { sentence ->
            sentence.ukti?.grammaticalVakyas()?.flatMap { it.padas }.orEmpty()
        }
        val ordinalValues = padas.mapNotNull(PuranaPratyayaResolver::ordinalValue).distinct().sorted()
        val ordinalParameters = ordinalValues.mapNotNull { value ->
            val name = when (value) { 1L -> "प्रथम"; 2L -> "द्वितीय"; 3L -> "तृतीय"; else -> null }
            name?.let { SamjnaParameter(it, SamjnaValueType.SANKHYA) }
        }
        val hasCollection = padas.filterIsInstance<SubantaPada>().any { pada ->
            (pada.pratipadika as? MulaPratipadika)?.lexicalIdentity == MulaPratipadikaIdentity.SAMAVAYA
        }
        return declared.copy(parameters = when {
            ordinalParameters.isNotEmpty() -> ordinalParameters
            hasCollection -> listOf(SamjnaParameter("समवाय", SamjnaValueType.SUCHI))
            else -> emptyList()
        })
    }

    private fun signatureKey(signature: SamjnaSignature): String = signature.parameters.joinToString { it.type.name }
    private fun stableMethodSuffix(
        source: String,
        domain: String?,
        symbol: String,
        signature: SamjnaSignature,
        definition: PvmScriptStatement.SamjnaDefinition,
    ): String = (listOf(
        source, domain.orEmpty(), symbol, signatureKey(signature), definition.procedure.modifiers.toString(),
    ).joinToString("\u0000")).hashCode().toUInt().toString(16)
}

internal object CompilerSymbols {
    fun stem(name: String): String {
        val parts = name.split('+').map(String::trim).filter(String::isNotEmpty)
        return if (parts.lastOrNull() in setOf(
                "सुँ", "औ", "जस्", "अम्", "औट्", "शस्", "टा", "भ्याम्", "भिस्",
                "ङे", "भ्यस्", "ङसि", "ङसिँ", "ङस्", "ओस्", "आम्", "ङि", "सुप्",
            )) parts.dropLast(1).joinToString(" + ") else name.trim()
    }

    fun localStem(stem: String): String = stem.substringAfter("ङस्", stem).trim().trimStart('+').trim()
}
