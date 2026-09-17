package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.test.assertFailsWith

class PrakriyaTypeFlowTest {
    @Test
    fun `invalid prakriya bodies are rejected before execution`() {
        val source = """
            प्रयत्न + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            अवैध + ॥
        """.trimIndent()

        assertFailsWith<IllegalArgumentException> { PvmScript.parse(source) }
        assertTrue(
            PrakriyaScriptValidator.validate(source).any { it.message.contains("invalid Sanskrit") },
        )
    }

    @Test
    fun `samjna declaration is rejected as a reusable prakriya marker`() {
        val source = "गणित + सुँ इति संज्ञा + सुँ ।"

        assertEquals(PvmSourceKind.UTTERANCE, PvmScript.classify(source))
        val diagnostic = PrakriyaScriptValidator.validate(source).single()
        assertTrue(diagnostic.message.contains("declare reusable code"))
        assertEquals("इति प्रक्रिया + सुँ असँ + लट् + तिप्", diagnostic.replacement)
    }

    @Test
    fun `validator recommends canonical segmented numeral stems`() {
        val diagnostics = PrakriyaScriptValidator.validate("पञ्च + शस् दश + अम् च मुद्र् + लोट् + सिप् ।")

        assertEquals(listOf("पञ्चन्", "दशन्"), diagnostics.mapNotNull(PrakriyaDiagnostic::replacement))
        assertTrue(diagnostics.all { it.severity == PrakriyaDiagnosticSeverity.WARNING })
    }

    @Test
    fun `validator reports duplicate parameters and bad call arity`() {
        val source = """
            योजन + ल्युट् + सुँ ।
            मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            सङ्ख्या + सुँ इति परिणाम + सुँ ।
            मान + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ॥

            त्रि + अम् योजन + ल्युट् + टा कृ + लोट् + सिप् ।
        """.trimIndent()

        val messages = PrakriyaScriptValidator.validate(source).map(PrakriyaDiagnostic::message)

        assertTrue(messages.any { it.contains("declared more than once") }, messages.toString())
        assertTrue(messages.any { it.contains("expects 2 arguments") }, messages.toString())
    }

    @Test
    fun `custom structured result declaration resolves its schema`() {
        val source = """
            अवस्था + अम् प्रयत्नसङ्ख्या + अम् अनुमानपरिणाम + मतुप् + सुँ ।
            अनुमान + ल्युट् + सुँ ।
            अनुमानपरिणाम + सुँ इति परिणाम + सुँ ।
            विजय + अम् दा + लोट् + सिप् ॥
        """.trimIndent()

        val definition = assertIs<PvmScriptStatement.PrakriyaDefinition>(
            PvmScript.parse(source).filterIsInstance<PvmScriptStatement.PrakriyaDefinition>().single(),
        )
        val signature = PrakriyaSignatureCompiler.compile(definition.body)

        assertEquals("अनुमानपरिणाम", signature.resultSchema)
        assertTrue(PrakriyaScriptValidator.validate(source).isEmpty())
    }

    @Test
    fun `typed values remain typed between prakriya pipeline stages`() {
        val results = PaniniVM().evalScript(
            """
            गणित + सुँ इति अधिकार + सुँ ।

            प्रथमक्रिया + ल्युट् + सुँ ।
            वाम + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            दक्षिण + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            सङ्ख्या + सुँ इति परिणाम + सुँ ।
            वाम + अम् दक्षिण + अम् च युज् + णिच् + लोट् + सिप् ॥

            द्वितीयक्रिया + ल्युट् + सुँ ।
            फलमान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            दक्षिण + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            सङ्ख्या + सुँ इति परिणाम + सुँ ।
            फलमान + अम् दक्षिण + अम् च गण् + णिच् + लोट् + सिप् ॥

            द्वि + अम् त्रि + अम् च गणित + ङस् प्रथमक्रिया + ल्युट् + ङस् गणित + ङस् द्वितीयक्रिया + ल्युट् + ङस् पूर्व + ङस् पर + ङस् एका + सुँ कृ + लोट् + सिप् ।
            """.trimIndent(),
        )

        val successes = results.filterIsInstance<ExecutionResult.Success>()
        assertTrue(successes.isNotEmpty(), results.toString())
        val success = successes.last()
        assertEquals("पञ्चदश", success.value, results.toString())
        assertIs<SanskritValue.Sankhya>(success.typedValue)
    }

    @Test
    fun `structured value codec preserves schema and typed fields`() {
        val value = SanskritValue.Rupa(
            "अनुमानपरिणाम",
            mapOf("प्रयत्नसङ्ख्या" to SanskritValue.Sankhya(3, "त्रि")),
        )

        val decoded = dev.panini.execution.sutra.ProgramSutraArthaCodec.decodeValue(
            dev.panini.execution.sutra.ProgramSutraArthaCodec.encodeValue(value),
        )

        assertEquals(value, decoded)
    }
}
