package dev.panini

import dev.panini.dhatupatha.DhatuPathaRegistration
import dev.panini.execution.ExecutionBindingResult
import dev.panini.execution.SambhashanaContext
import dev.panini.execution.SanskritUktiInput
import dev.panini.execution.binding.VyakaranamExecutionAdapter
import dev.panini.execution.sutra.ExecutableUktiSutraCompiler
import dev.panini.sutra.runtime.SutraBlueprintGranthaTextCodec
import dev.panini.sutra.runtime.SutraBlueprintGranthaTextEncoding
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class MainTest {
    @Test
    fun `eval command executes pvm script file and formats results`() {
        val scriptPath = listOf("parser/src/test/kotlin/dev/panini/parser/addition.pvm", "src/test/kotlin/dev/panini/parser/addition.pvm").first { java.io.File(it).exists() }
        val output = runCli(arrayOf("--eval", scriptPath))

        assertEquals("=== PaniniVM Script Execution: addition.pvm ===", output.first())
        assertTrue(output.any { it.contains("✓ Result: षट्") }, output.joinToString("\n"))
        assertTrue(output.any { it.contains("✓ Result: पञ्च") }, output.joinToString("\n"))
    }

    @Test
    fun `grantha command executes canonical segmented source`() {
        DhatuPathaRegistration.ensureRegistered()
        val conversation = SambhashanaContext("प्रयोक्ता", "यन्त्रम्")
        val bound = assertIs<ExecutionBindingResult.Bound>(
            VyakaranamExecutionAdapter.bind(
                SanskritUktiInput(
                    conversation.speaker,
                    conversation.listener,
                    "दश + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।",
                ),
                conversation,
            ),
        )
        val source = assertIs<SutraBlueprintGranthaTextEncoding.Success>(
            SutraBlueprintGranthaTextCodec.encode(
                ExecutableUktiSutraCompiler.compileBlueprintGrantha(bound.ukti),
            ),
        ).text
        val file = Files.createTempFile("addition", ".sutra")
        try {
            Files.writeString(file, source)

            val output = runCli(arrayOf("--grantha", file.toString()))

            assertEquals("=== Sūtra Grantha Execution: ${file.fileName} ===", output.first())
            assertTrue(output.any { it.contains("✓ योग-1: द्वादश") }, output.joinToString("\n"))
        } finally {
            Files.deleteIfExists(file)
        }
    }

    @Test
    fun `grantha command reports invalid source diagnostics`() {
        val file = Files.createTempFile("invalid", ".sutra")
        try {
            Files.writeString(file, """{"id":t"incomplete"}""")

            val output = runCli(arrayOf("--grantha", file.toString()))

            assertTrue(output.any { it.contains("INVALID_SCHEMA") }, output.joinToString("\n"))
        } finally {
            Files.deleteIfExists(file)
        }
    }

    @Test
    fun `emit grantha command compiles Sanskrit source that grantha command executes`() {
        val directory = Files.createTempDirectory("grantha-toolchain")
        val input = directory.resolve("addition.pvm")
        val output = directory.resolve("addition.sutra")
        try {
            Files.writeString(
                input,
                "दश + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।",
            )

            val emitted = runCli(
                arrayOf("--emit-grantha", input.toString(), output.toString()),
            )
            val executed = runCli(arrayOf("--grantha", output.toString()))

            assertTrue(emitted.any { it.contains("✓ Emitted 1 sūtra(s)") })
            assertTrue(Files.exists(output))
            assertTrue(executed.any { it.contains("✓ उक्ति-१/योग-1: द्वादश") })
        } finally {
            Files.deleteIfExists(output)
            Files.deleteIfExists(input)
            Files.deleteIfExists(directory)
        }
    }

    @Test
    fun `emitted grantha preserves previous result references across source lines`() {
        val directory = Files.createTempDirectory("grantha-turns")
        val input = directory.resolve("turns.pvm")
        val output = directory.resolve("turns.sutra")
        try {
            Files.writeString(
                input,
                """
                दश + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।
                पूर्वफल + अम् त्रि + शस् च युज् + णिच् + लोट् + सिप् ।
                """.trimIndent(),
            )

            val emitted = runCli(
                arrayOf("--emit-grantha", input.toString(), output.toString()),
            )
            val executed = runCli(arrayOf("--grantha", output.toString()))

            assertTrue(emitted.any { it.contains("✓ Emitted 2 sūtra(s)") })
            assertTrue(executed.any { it.contains("✓ उक्ति-२/योग-1: पञ्चदश") })
        } finally {
            Files.deleteIfExists(output)
            Files.deleteIfExists(input)
            Files.deleteIfExists(directory)
        }
    }

    @Test
    fun `derive command returns the form and its sutra trace`() {
        val output = runCli(arrayOf("--derive", "राम", "SASTHI", "BAHUVACANA"))
        val sanskritLabels = runCli(arrayOf("--derive", "राम", "षष्ठी", "बहुवचन"))

        assertEquals("SASTHI BAHUVACANA: रामाणाम्", output.first())
        assertEquals(output.first(), sanskritLabels.first())
        assertTrue(output.any { it.startsWith("7.1.54 ") })
        assertTrue(output.any { it.startsWith("6.4.3 ") })
    }

    @Test
    fun `sutra command reads direct base sutra fields`() {
        val output = runCli(arrayOf("--sutra", "7.1.54"))

        assertEquals("7.1.54 ह्रस्वनद्यापो नुट्", output.first())
        assertTrue(output.any { it.contains("action=AGAMA") })
    }

    @Test
    fun `verb command returns bhavati with its sutra trace`() {
        val output = runCli(arrayOf("--verb", "भू"))
        val plural = runCli(arrayOf("--verb", "भू", "बहुवचन"))

        assertEquals("भू: भवति", output.first())
        assertEquals("भू: भवन्ति", plural.first())
        assertTrue(output.any { it.startsWith("7.3.84 —") })
    }

    @Test
    fun `verb command derives the loṭ imperative`() {
        val output = runCli(arrayOf("--verb", "भू", "LOT", "बहुवचन"))

        assertEquals("भू: भवन्तु", output.first())
        assertTrue(output.any { it.startsWith("3.3.162 —") })
        assertTrue(output.any { it.startsWith("3.4.90 —") })
    }

    @Test
    fun `verb command derives the vidhi ling`() {
        val output = runCli(arrayOf("--verb", "भू", "LING"))

        assertEquals("भू: भवेत्", output.first())
        assertTrue(output.any { it.startsWith("3.3.161 —") })
        assertTrue(output.any { it.startsWith("3.4.103 —") })
    }

    @Test
    fun `sankhya command derives a cardinal with its trace`() {
        val output = runCli(arrayOf("--sankhya", "23"))

        assertEquals("CARDINAL 23: त्रयोविंशति", output.first())
        assertTrue(output.any { it.startsWith("6.3.48 ") }, output.joinToString("\n"))
    }

    @Test
    fun `sankhya command derives an ordinal with its trace`() {
        val output = runCli(arrayOf("--sankhya", "6", "ordinal"))

        assertEquals("ORDINAL 6: षष्ठ", output.first())
        assertTrue(output.any { it.startsWith("5.2.51 ") }, output.joinToString("\n"))
        assertTrue(output.any { it.startsWith("8.4.41 ") }, output.joinToString("\n"))
    }

    @Test
    fun `sankhya command exposes licensed variants`() {
        val output = runCli(arrayOf("--sankhya", "42", "cardinal", "--variants"))

        assertEquals(
            setOf("द्विचत्वारिंशत्", "द्वाचत्वारिंशत्"),
            output.filter { it.startsWith("CARDINAL 42 [") }
                .map { it.substringAfter(": ") }
                .toSet(),
        )
    }
}
