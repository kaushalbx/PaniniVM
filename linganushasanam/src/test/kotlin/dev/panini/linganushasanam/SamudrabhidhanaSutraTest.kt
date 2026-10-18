package dev.panini.linganushasanam

import dev.panini.core.Linga
import dev.panini.core.LingaSemanticClass
import dev.panini.linganushasanam.adhyaya2.DevasuratmaSvargaGiriSamudraSutra
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SamudrabhidhanaSutraTest {
    @Test
    fun `43 assigns masculine only from declared ocean meaning provenance`() {
        val sourced = LingaRuleContext("सागर",
            semanticClasses = setOf(LingaSemanticClass.SAMUDRA_ABHIDHANA))
        assertTrue(DevasuratmaSvargaGiriSamudraSutra.matches(sourced))
        val result = DevasuratmaSvargaGiriSamudraSutra.apply(sourced) as LingaRuleResult.Matched
        assertEquals(Linga.PUMS, result.linga)
        assertEquals("43", result.ruleId)

        assertFalse(DevasuratmaSvargaGiriSamudraSutra.matches(LingaRuleContext("सागर")))
        assertFalse(DevasuratmaSvargaGiriSamudraSutra.matches(LingaRuleContext("अगर")))
    }

    @Test
    fun `declared ocean stems retain semantic and sutra provenance`() {
        for (stem in listOf("सागर", "रत्नाकर")) {
            val entry = PaninianPratipadikaLexicon.findPratipadika(stem)!!
            assertEquals(setOf(Linga.PUMS), entry.linga)
            assertEquals(setOf(LingaSemanticClass.SAMUDRA_ABHIDHANA), entry.lingaSemanticClasses)
            assertEquals(setOf("43"), entry.lingaRuleIds)
        }
    }
}
