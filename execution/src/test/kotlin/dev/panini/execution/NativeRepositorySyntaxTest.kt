package dev.panini.execution

import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertEquals

class NativeRepositorySyntaxTest {
    @Test
    fun `adhikara governs subsequent declarations without retroactive scope`() {
        val source = """
            पूर्व + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            गणित + सुँ इति अधिकार + सुँ असँ + लट् + तिप् ।
            मध्य + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            भाषा + सुँ इति अधिकार + सुँ असँ + लट् + तिप् ।
            उत्तर + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            गणित + ङस् प्रयत्न + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
        """.trimIndent()
        val registry = PrakriyaRegistry()
        PvmProjectLoader().registerDeclarations(registry, PvmScript.parse(source), "scope.pvm")
        assertEquals(mapOf("पूर्व" to null, "मध्य" to "गणित", "उत्तर" to "भाषा", "प्रयत्न" to "गणित"),
            registry.all().associate { it.nameStem to it.domainStem })
    }

    @Test
    fun `repository programs parse as complete native documents`() {
        val failures = listOf("examples", "projects").flatMap { root ->
            File(root).walkTopDown().filter { it.isFile && it.extension == "pvm" }
                .sortedBy(File::getPath).mapNotNull { file ->
                    runCatching { PvmScript.parse(file.readText()) }.exceptionOrNull()
                        ?.let { "${file.path}: ${it.message}" }
                }.toList()
        }
        assertTrue(failures.isEmpty(), failures.joinToString("\n"))
    }
}
