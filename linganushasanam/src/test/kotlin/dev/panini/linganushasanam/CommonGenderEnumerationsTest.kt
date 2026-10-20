package dev.panini.linganushasanam

import dev.panini.core.Linga
import dev.panini.linganushasanam.sutras.DandaMandaKhandaSavaSaindhavaParsvaSutra
import dev.panini.linganushasanam.sutras.SilaMulaMangalaSalaKamalaTalaMusalaKundalaSutra
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame

class CommonGenderEnumerationsTest {
    @Test fun `rules 143 and 181 preserve both licensed genders`() {
        for ((rule, words) in listOf(
            SilaMulaMangalaSalaKamalaTalaMusalaKundalaSutra to listOf("मूल", "वाल", "शूल"),
            DandaMandaKhandaSavaSaindhavaParsvaSutra to listOf("दण्ड", "खम", "आकाश"),
        )) {
            assertSame(rule, LinganushasanamRegistry.require(rule.number))
            for (word in words) {
                val result = rule.apply(LingaRuleContext(word)) as LingaRuleResult.Matched
                assertEquals(setOf(Linga.PUMS, Linga.NAPUMSAKA), result.lingas, word)
                assertEquals(setOf(Linga.PUMS, Linga.NAPUMSAKA),
                    PaninianPratipadikaLexicon.findPratipadika(word)?.linga, word)
            }
        }
        assertFalse(SilaMulaMangalaSalaKamalaTalaMusalaKundalaSutra.matches(LingaRuleContext("अबाल")))
        assertFalse(DandaMandaKhandaSavaSaindhavaParsvaSutra.matches(LingaRuleContext("अदण्ड")))
    }
}
