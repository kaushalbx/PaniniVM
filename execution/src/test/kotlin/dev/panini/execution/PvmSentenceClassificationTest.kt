package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertIs

class PvmSentenceClassificationTest {
    @Test
    fun `structured sentence semantics are classified once during parsing`() {
        assertIs<PvmSentenceSemantics.SchemaDeclaration>(
            sentence("अवस्था + अम् प्रयत्नसङ्ख्या + अम् अनुमानपरिणाम + मतुप् + सुँ ।").semantics,
        )
        assertIs<PvmSentenceSemantics.StructConstruction>(
            sentence("पञ्चन् + दशत + अम् मूल्य + अम् सङ्ख्या + मतुप् + सुँ ।").semantics,
        )
        assertIs<PvmSentenceSemantics.AttributeAccess>(
            sentence("सङ्ख्या + मतुप् + ङस् मूल्य + अम् ।").semantics,
        )
        assertIs<PvmSentenceSemantics.AttributePipeline>(
            sentence("सङ्ख्या + मतुप् + ङस् मूल्य + अम् ततः मुद्र् + लोट् + सिप् ।").semantics,
        )
        assertIs<PvmSentenceSemantics.StructuredConditional>(
            sentence(
                "यदि परिणाम + मतुप् + ङस् प्रयत्नसङ्ख्या + अम् द्वि + अम् च विद् + लोट् + सिप् " +
                    "तर्हि जय + अम् मुद्र् + लोट् + सिप् अन्यथा पराजय + अम् मुद्र् + लोट् + सिप् ।",
            ).semantics,
        )
    }

    private fun sentence(source: String): PvmScriptStatement.Sentence =
        PvmScript.parse(source).single() as PvmScriptStatement.Sentence
}
