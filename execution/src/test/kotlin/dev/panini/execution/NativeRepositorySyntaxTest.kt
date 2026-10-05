package dev.panini.execution

import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertEquals

class NativeRepositorySyntaxTest {
    @Test
    fun `adhikara governs subsequent declarations without retroactive scope`() {
        val source = """
            पूर्व + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            गणित + सुँ इति अधिकार + सुँ ।
            मध्य + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            भाषा + सुँ इति अधिकार + सुँ ।
            उत्तर + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            गणित + ङस् प्रयत्न + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
        """.trimIndent()
        val registry = PrakriyaRegistry()
        PvmProjectLoader().registerDeclarations(registry, PvmScript.parse(source), "scope.pvm")
        assertEquals(mapOf("पूर्व" to null, "मध्य" to "गणित", "उत्तर" to "भाषा", "प्रयत्न" to "गणित"),
            registry.all().associate { it.nameStem to it.domainStem })
    }

    @Test
    fun `native repository declarations and sentence classifications match existing semantics`() {
        fun signature(statements: List<PvmScriptStatement>): List<String> = statements.map { statement ->
            when (statement) {
                is PvmScriptStatement.PrakriyaDefinition ->
                    "procedure:${statement.prakriya.nameIdentity}:${statement.prakriya.domainIdentity}:${statement.prakriya.modifiers}:${statement.body.size}:" +
                        statement.body.joinToString { "${it.semantics}:${it.isNishedha}" }
                is PvmScriptStatement.AdhikaraDefinition -> "scope:${statement.scope.domainIdentity}"
                is PvmScriptStatement.RangeDefinition -> "range:${statement.range.minimum.value}:${statement.range.maximum.value}"
                is PvmScriptStatement.Sentence -> "sentence:${statement.semantics}:${statement.isNishedha}"
            }
        }.sorted()
        val failures = listOf("examples", "projects").flatMap { root ->
            File(root).walkTopDown().filter { it.isFile && it.extension == "pvm" }.mapNotNull { file ->
                val source = file.readText()
                val old = signature(PvmScript.parseLegacy(source))
                val native = signature(PvmScript.parseNative(source))
                if (old == native) null else "${file.path}\nlegacy: $old\nnative: $native"
            }.toList()
        }
        assertTrue(failures.isEmpty(), failures.joinToString("\n"))
    }

    @Test
    fun `repository programs parse as complete native documents`() {
        val failures = listOf("examples", "projects").flatMap { root ->
            File(root).walkTopDown().filter { it.isFile && it.extension == "pvm" }
                .sortedBy(File::getPath).mapNotNull { file ->
                    runCatching { PvmScript.parseNative(file.readText()) }.exceptionOrNull()
                        ?.let { "${file.path}: ${it.message}" }
                }.toList()
        }
        assertTrue(failures.isEmpty(), failures.joinToString("\n"))
    }
}
