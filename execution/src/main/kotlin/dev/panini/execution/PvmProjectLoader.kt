package dev.panini.execution

import dev.panini.vyakaranam.ast.PrakriyaPrecedence
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/** Discovers project sources and registers their reusable grammatical declarations. */
internal class PvmProjectLoader(private val metrics: ExecutionMetrics? = null,
    private val readSource: (File) -> String = { it.readText() }) {
    private data class CachedSource(
        val source: String,
        val statements: List<PvmScriptStatement>,
    )

    private val parsedLibraries = ConcurrentHashMap<String, CachedSource>()
    fun registerDeclarations(
        registry: PrakriyaRegistry,
        statements: List<PvmScriptStatement>,
        sourceFile: String?,
        includeExecutionModifiers: Boolean = true,
    ) {
        var activeDomain: String? = null
        registerInheritances(registry, statements)
        statements.forEach { statement ->
            if (statement is PvmScriptStatement.AdhikaraDefinition) {
                activeDomain = statement.scope.domainIdentity
                return@forEach
            }
            val definition = statement as? PvmScriptStatement.PrakriyaDefinition ?: return@forEach
            val prakriya = definition.prakriya
            registry.register(
                Prakriya(
                    nameSegmented = prakriya.name,
                    nameStem = prakriya.nameIdentity,
                    body = definition.body,
                    headerResult = prakriya.declaredResult,
                    headerPredicate = prakriya.resultPredicate,
                    sourceFile = sourceFile,
                    domainStem = prakriya.domainIdentity ?: activeDomain,
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
            .sortedBy { it.relativeTo(projectDir).invariantSeparatorsPath }
            .forEach { library ->
                val parsed = parsedLibrary(library)
                registerDeclarations(
                    registry,
                    parsed,
                    sourceFile = library.relativeTo(projectDir).invariantSeparatorsPath,
                    includeExecutionModifiers = false,
                )
            }
        return registry
    }

    private fun parsedLibrary(library: File): List<PvmScriptStatement> {
        val path = library.canonicalPath
        val source = try { readSource(library) } catch (failure: java.io.IOException) {
            throw IllegalArgumentException("Cannot read PVM library '$path'.", failure)
        }
        parsedLibraries[path]?.takeIf { it.source == source }?.let {
            metrics?.recordProjectCacheHit()
            return it.statements
        }
        val parsed = PvmScript.parse(source)
        parsedLibraries[path] = CachedSource(source, parsed)
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

}
