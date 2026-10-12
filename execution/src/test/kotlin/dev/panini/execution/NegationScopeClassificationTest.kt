package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class NegationScopeClassificationTest {
    @Test
    fun `negated control predicates are not declaration level prohibitions`() {
        val sources = listOf(
            "एक + कृत्वसुच् यावत् स्वसङ्ख्याप्राप्ति + सुँ न असँ + लट् + तिप् तावत् एक + अम् मुद्र् + णिच् + लोट् + सिप् ।",
            "यदि स्वसङ्ख्याप्राप्ति + सुँ न असँ + लट् + तिप् तर्हि एक + अम् मुद्र् + णिच् + लोट् + सिप् ।",
        )
        for (source in sources) {
            for (statements in listOf(PvmScript.parseNative(source), PvmScript.parseLegacy(source))) {
                assertFalse(assertIs<PvmScriptStatement.Sentence>(statements.single()).isNishedha)
            }
        }
    }

    @Test
    fun `standalone nominal prohibition retains guard classification`() {
        val source = "प्रथम + अम् शून्य + अम् न ।"
        for (statements in listOf(PvmScript.parseNative(source), PvmScript.parseLegacy(source))) {
            assertTrue(assertIs<PvmScriptStatement.Sentence>(statements.single()).isNishedha)
        }
    }
}
