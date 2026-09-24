package dev.panini.execution

import dev.panini.vyakaranam.ast.PrakriyaPrecedence
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/** Discovers project sources and registers their reusable grammatical declarations. */
internal class PvmProjectLoader(private val metrics: ExecutionMetrics? = null) {
    private data class CachedSource(
        val lastModified: Long,
        val length: Long,
        val statements: List<PvmScriptStatement>,
    )

    private val parsedLibraries = ConcurrentHashMap<String, CachedSource>()
    fun registerDeclarations(
        registry: PrakriyaRegistry,
        statements: List<PvmScriptStatement>,
        sourceFile: String?,
        includeExecutionModifiers: Boolean = true,
    ) {
        val fallbackDomain = statements.filterIsInstance<PvmScriptStatement.AdhikaraDefinition>()
            .firstOrNull()?.let { derivePrakriyaStem(it.scope.domain) }
        registerInheritances(registry, statements)
        statements.filterIsInstance<PvmScriptStatement.PrakriyaDefinition>().forEach { definition ->
            val prakriya = definition.prakriya
            registry.register(
                Prakriya(
                    nameSegmented = prakriya.name,
                    nameStem = derivePrakriyaStem(prakriya.name),
                    body = definition.body,
                    sourceFile = sourceFile,
                    domainStem = prakriya.domain ?: deriveDomainStem(prakriya.name) ?: fallbackDomain,
                    visibility = prakriya.modifiers.visibility,
                    precedence = if (includeExecutionModifiers) {
                        prakriya.modifiers.precedence
                    } else {
                        PrakriyaPrecedence.DEFAULT
                    },
                ),
            )
        }
    }

    fun loadLibraryRegistry(entryFile: File): PrakriyaRegistry {
        val projectDir = entryFile.parentFile ?: entryFile.absoluteFile.parentFile
            ?: error("Cannot determine project directory for ${entryFile.path}")
        val registry = PrakriyaRegistry()
        projectDir.walkTopDown()
            .filter { it.isFile && it.extension == "pvm" && it.canonicalPath != entryFile.canonicalPath }
            .sortedBy(File::getName)
            .forEach { library ->
                val parsed = parsedLibrary(library) ?: return@forEach
                registerDeclarations(
                    registry,
                    parsed,
                    sourceFile = library.name,
                    includeExecutionModifiers = false,
                )
            }
        return registry
    }

    private fun parsedLibrary(library: File): List<PvmScriptStatement>? {
        val path = library.canonicalPath
        val modified = library.lastModified()
        val length = library.length()
        parsedLibraries[path]?.takeIf { it.lastModified == modified && it.length == length }?.let {
            metrics?.recordProjectCacheHit()
            return it.statements
        }
        val source = runCatching { library.readText() }.getOrNull() ?: return null
        val parsed = PvmScript.parse(source)
        parsedLibraries[path] = CachedSource(modified, length, parsed)
        metrics?.recordProjectCacheMiss()
        metrics?.recordParsedFile()
        metrics?.recordParsedSentences(parsed.sumOf { statement ->
            when (statement) {
                is PvmScriptStatement.Sentence -> 1
                is PvmScriptStatement.PrakriyaDefinition -> statement.body.size
                is PvmScriptStatement.AdhikaraDefinition, is PvmScriptStatement.RangeDefinition -> 0
            }
        })
        return parsed
    }

    fun hasSiblingSource(file: File): Boolean {
        val directory = file.parentFile ?: file.absoluteFile.parentFile
        return directory?.walkTopDown()?.any {
            it.isFile && it.extension == "pvm" && it.canonicalPath != file.canonicalPath
        } == true
    }

    private fun registerInheritances(
        registry: PrakriyaRegistry,
        statements: List<PvmScriptStatement>,
    ) {
        statements.filterIsInstance<PvmScriptStatement.AdhikaraDefinition>().forEach { adhikara ->
            TaddhitaInheritanceEngine.detectInheritanceAdhikara(adhikara.scope.domain)?.let {
                registry.registerInheritance(it)
            }
        }
    }

    private fun derivePrakriyaStem(nameSegmented: String): String =
        requireNotNull(PrakriyaHeaderIdentityParser.parse(nameSegmented)) {
            "Unable to parse saṃjñā header identity: $nameSegmented"
        }.operationStem

    private fun deriveDomainStem(nameSegmented: String): String? =
        PrakriyaHeaderIdentityParser.parse(nameSegmented)?.domainStem
}
