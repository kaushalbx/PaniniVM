package dev.panini.ashtadhyayi.adhyaya2

import dev.panini.analysis.SamasaPada
import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.ashtadhyayi.adhyaya2.pada2.UpapadamAtingSutra
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UpapadaClassificationBoundaryTest {
    @Test
    fun `classification does not delete final consonants syllables or orthographic marks`() {
        for (word in listOf("कुम्भ", "सामन्", "वन", "किम्", "सामन्॑")) {
            val context = SamasaRuleContext(
                listOf(SamasaPada(word, Vibhakti.DVITIYA), SamasaPada("ग")),
                SamasaType.UPAPADA_TATPURUSA,
            )
            val result = UpapadamAtingSutra.apply(context) as SamasaRuleResult.Formed
            assertEquals(word + "ग", result.compoundStem, word)
            assertTrue(result.memberEdits.isEmpty())
            assertEquals(word, context.purvaPada.upadesha)
        }
    }
}
