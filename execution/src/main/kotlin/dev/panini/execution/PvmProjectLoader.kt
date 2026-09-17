package dev.panini.execution

import dev.panini.vyakaranam.ast.ProcedurePrecedence
import java.io.File

/** Discovers project sources and registers their reusable grammatical declarations. */
internal class PvmProjectLoader {
    fun registerDeclarations(
        registry: SamjnaKriyaRegistry,
        statements: List<PvmScriptStatement>,
        sourceFile: String?,
        includeExecutionModifiers: Boolean = true,
    ) {
        val fallbackDomain = statements.filterIsInstance<PvmScriptStatement.AdhikaraDefinition>()
            .firstOrNull()?.let { deriveSamjnaStem(it.scope.domain) }
        registerInheritances(registry, statements)
        statements.filterIsInstance<PvmScriptStatement.SamjnaDefinition>().forEach { definition ->
            val procedure = definition.procedure
            registry.register(
                SamjnaKriya(
                    nameSegmented = procedure.name,
                    nameStem = deriveSamjnaStem(procedure.name),
                    body = definition.body,
                    sourceFile = sourceFile,
                    domainStem = procedure.domain ?: deriveDomainStem(procedure.name) ?: fallbackDomain,
                    visibility = procedure.modifiers.visibility,
                    precedence = if (includeExecutionModifiers) {
                        procedure.modifiers.precedence
                    } else {
                        ProcedurePrecedence.DEFAULT
                    },
                ),
            )
        }
    }

    fun loadLibraryRegistry(entryFile: File): SamjnaKriyaRegistry {
        val projectDir = entryFile.parentFile ?: entryFile.absoluteFile.parentFile
            ?: error("Cannot determine project directory for ${entryFile.path}")
        val registry = SamjnaKriyaRegistry()
        projectDir.walkTopDown()
            .filter { it.isFile && it.extension == "pvm" && it.canonicalPath != entryFile.canonicalPath }
            .sortedBy(File::getName)
            .forEach { library ->
                registerDeclarations(
                    registry,
                    PvmScript.parse(library.readText()),
                    sourceFile = library.name,
                    includeExecutionModifiers = false,
                )
            }
        return registry
    }

    fun hasSiblingSource(file: File): Boolean {
        val directory = file.parentFile ?: file.absoluteFile.parentFile
        return directory?.walkTopDown()?.any {
            it.isFile && it.extension == "pvm" && it.canonicalPath != file.canonicalPath
        } == true
    }

    private fun registerInheritances(
        registry: SamjnaKriyaRegistry,
        statements: List<PvmScriptStatement>,
    ) {
        statements.filterIsInstance<PvmScriptStatement.AdhikaraDefinition>().forEach { adhikara ->
            TaddhitaInheritanceEngine.detectInheritanceAdhikara(adhikara.scope.domain)?.let {
                registry.registerInheritance(it)
            }
        }
    }

    private fun deriveSamjnaStem(nameSegmented: String): String =
        requireNotNull(SamjnaHeaderIdentityParser.parse(nameSegmented)) {
            "Unable to parse saṃjñā header identity: $nameSegmented"
        }.operationStem

    private fun deriveDomainStem(nameSegmented: String): String? =
        SamjnaHeaderIdentityParser.parse(nameSegmented)?.domainStem
}
