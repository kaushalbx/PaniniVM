package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import dev.panini.vyakaranam.ast.SubantaPada

class PvmSentenceClassificationTest {
    @Test
    fun `structured sentence semantics are classified once during parsing`() {
        assertIs<PvmSentenceSemantics.SchemaDeclaration>(
            sentence(
                "अवस्था + सुँ प्रयत्नसङ्ख्या + सुँ च अनुमानपरिणाम + ङस् क्षेत्र + जस् " +
                    "असँ + लट् + झि ।",
            ).semantics,
        )
        val fieldAssertion = assertIs<PvmSentenceSemantics.StructFieldAssertion>(
            sentence(
                "सङ्ख्या + मतुप् + ङस् मूल्य + सुँ पञ्चाशत् + सुँ असँ + लट् + तिप् ।",
            ).semantics,
        )
        assertIs<SubantaPada>(fieldAssertion.assertion.valuePada)
        assertEquals(50L, assertIs<SanskritValue.Sankhya>(
            TaddhitaStructEngine.assertionValue(fieldAssertion.assertion),
        ).value)
        assertIs<PvmSentenceSemantics.AttributeAccess>(
            sentence("सङ्ख्या + मतुप् + ङस् मूल्य + अम् ग्रहँ + श्ना + लोट् + सिप् ।").semantics,
        )
        assertIs<PvmSentenceSemantics.AttributePipeline>(
            sentence(
                "सङ्ख्या + मतुप् + ङस् मूल्य + अम् ग्रहँ + श्ना + लोट् + सिप् " +
                    "ततः मुद्र् + लोट् + सिप् ।",
            ).semantics,
        )
        assertIs<PvmSentenceSemantics.StructuredConditional>(
            sentence(
                "यदि परिणाम + मतुप् + ङस् प्रयत्नसङ्ख्या + अम् द्वि + अम् च विद् + लोट् + सिप् " +
                    "तर्हि जय + अम् मुद्र् + लोट् + सिप् अन्यथा पराजय + अम् मुद्र् + लोट् + सिप् ।",
            ).semantics,
        )
    }

    @Test
    fun `result schema declaration renders as an ordinary possessive sentence`() {
        val source =
            "अवस्था + सुँ प्रयत्नसङ्ख्या + सुँ च अनुमानपरिणाम + ङस् क्षेत्र + जस् " +
                "असँ + लट् + झि ।"

        assertEquals(
            "अवस्था प्रयत्नसङ्ख्या च अनुमानपरिणामस्य क्षेत्राः सन्ति ।",
            PvmUktiSadhaka().sadhayaLine(source),
        )
    }

    @Test
    fun `structured predicate identity retains its complete derivation`() {
        val fieldAssertion = assertIs<PvmSentenceSemantics.StructFieldAssertion>(
            sentence(
                "सङ्ख्या + मतुप् + ङस् अवस्था + सुँ भू + क्त + सुँ असँ + लट् + तिप् ।",
            ).semantics,
        )

        assertEquals("भू+क्त", fieldAssertion.assertion.valueStem)
        assertEquals(
            SanskritValue.Shabda("भू+क्त"),
            TaddhitaStructEngine.assertionValue(fieldAssertion.assertion),
        )
    }

    @Test
    fun `predicate-less genitive fragment is not an attribute read`() {
        assertIs<PvmSentenceSemantics.Executable>(
            sentence("सङ्ख्या + मतुप् + ङस् मूल्य + अम् ।").semantics,
        )
    }

    @Test
    fun `attribute retrieval requires an accusative object`() {
        assertIs<PvmSentenceSemantics.Executable>(
            sentence(
                "सङ्ख्या + मतुप् + ङस् मूल्य + भिस् ग्रहँ + श्ना + लोट् + सिप् ।",
            ).semantics,
        )
    }

    private fun sentence(source: String): PvmScriptStatement.Sentence =
        PvmScript.parse(source).single() as PvmScriptStatement.Sentence
}
