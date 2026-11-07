package dev.panini.execution

import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class PrakriyaTypeFlowTest {
    @Test
    fun `validator produces identical diagnostics from a canonical document`() {
        val source = "पञ्च + शस् दश + अम् च मुद्र् + लोट् + सिप् ।"
        val document = PaniniParser().parseDocument(source)

        assertEquals(
            PrakriyaScriptValidator.validate(source),
            PrakriyaScriptValidator.validate(source, document),
        )
    }

    @Test
    fun `validator preserves parser semantic code span and offending text`() {
        val source = "सूची + ङस् एकैक + टाप् + अम् सङ्ख्या + अम् " +
            "तद् + ङस् + लुक् - उत्तर + टाप् + टा सङ्ख्या + भिस् " +
            "युज् + णिच् + लोट् + सिप् ।"

        val diagnostic = PrakriyaScriptValidator.validate(source).single()

        assertEquals("RELATIVE_INSTRUMENTAL", diagnostic.code)
        assertEquals("सङ्ख्या+भिस्", diagnostic.offendingText)
        assertEquals(diagnostic.offendingText,
            source.substring(diagnostic.span.start, diagnostic.span.endExclusive).filterNot(Char::isWhitespace))
        assertNotNull(diagnostic.span)
    }

    @Test
    fun `explicit reusable header validates through the document parser`() {
        val source = "वृध् + ल्युट् + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।\nएक + अम् मुद्र् + णिच् + लोट् + सिप् ॥"
        assertTrue(PrakriyaScriptValidator.validate(source).isEmpty())
        assertIs<PvmScriptStatement.PrakriyaDefinition>(PvmScript.parse(source).single())
    }

    @Test
    fun `a nominal sentence with no verb is rejected`() {
        assertTrue(runCatching { dev.panini.vyakaranam.parser.PaniniParser().parseDocument("वृध् + ल्युट् + सुँ ।") }.isFailure)
    }

    @Test
    fun `invalid prakriya bodies are rejected before execution`() {
        val source = """
            प्रयत्न + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            अवैध + ॥
        """.trimIndent()

        assertFailsWith<IllegalArgumentException> { PvmScript.parse(source) }
        assertTrue(
            PrakriyaScriptValidator.validate(source).any { it.message.contains("invalid Sanskrit") },
        )
    }

    @Test
    fun `samjna declaration is rejected as a reusable prakriya marker`() {
        val source = "गणित + सुँ इति संज्ञा + सुँ असँ + लट् + तिप् ।"

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
            योजन + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ सङ्ख्या + सुँ असँ + लट् + तिप् ।
            मान + सुँ सङ्ख्या + सुँ असँ + लट् + तिप् ।
            परिणाम + सुँ सङ्ख्या + सुँ असँ + लट् + तिप् ।
            मान + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ॥

            गणन + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            पूर्व + सुँ नाम मान + सुँ सङ्ख्या + सुँ असँ + लट् + तिप् ।
            उत्तर + सुँ नाम मान + सुँ सङ्ख्या + सुँ असँ + लट् + तिप् ।
            पूर्व + अम् उत्तर + अम् च युज् + णिच् + लोट् + सिप् ॥

            त्रि + अम् गणन + टा कृ + लोट् + सिप् ।
        """.trimIndent()

        val messages = PrakriyaScriptValidator.validate(source).map(PrakriyaDiagnostic::message)

        assertTrue(messages.any { it.contains("declared more than once") }, messages.toString())
        assertTrue(messages.any { it.contains("2 मानानि अपेक्षितानि") }, messages.toString())
    }

    @Test
    fun `custom structured result declaration resolves its schema`() {
        val source = """
            अवस्था + सुँ प्रयत्नसङ्ख्या + सुँ च अनुमानपरिणाम + ङस् क्षेत्र + जस् असँ + लट् + झि ।
            अनुमान + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            परिणाम + सुँ अनुमानपरिणाम + सुँ असँ + लट् + तिप् ।
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
            गणित + सुँ इति अधिकार + सुँ असँ + लट् + तिप् ।

            प्रथमक्रिया + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            वाम + सुँ नाम मान + सुँ सङ्ख्या + सुँ असँ + लट् + तिप् ।
            दक्षिण + सुँ नाम मान + सुँ सङ्ख्या + सुँ असँ + लट् + तिप् ।
            परिणाम + सुँ सङ्ख्या + सुँ असँ + लट् + तिप् ।
            वाम + अम् दक्षिण + अम् च युज् + णिच् + लोट् + सिप् ॥

            द्वितीयक्रिया + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            फलमान + सुँ नाम मान + सुँ सङ्ख्या + सुँ असँ + लट् + तिप् ।
            दक्षिण + सुँ नाम मान + सुँ सङ्ख्या + सुँ असँ + लट् + तिप् ।
            परिणाम + सुँ सङ्ख्या + सुँ असँ + लट् + तिप् ।
            फलमान + अम् दक्षिण + अम् च गुण + णिच् + लोट् + सिप् ॥

            द्वि + अम् त्रि + अम् च गणित + ङस् प्रथमक्रिया + ङस् गणित + ङस् द्वितीयक्रिया + ङस् पूर्व + ङस् पर + ङस् एका + सुँ कृ + लोट् + सिप् ।
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
