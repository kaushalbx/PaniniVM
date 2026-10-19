package dev.panini.linganushasanam

import dev.panini.core.Linga
import dev.panini.linganushasanam.adhyaya3.DandaMandaKhandaSavaSaindhavaParsvaSutra
import dev.panini.linganushasanam.adhyaya3.SilaMulaMangalaSalaKamalaTalaMusalaKundalaSutra
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame

class CommonGenderEnumerationsTest {
    @Test fun `rules 145 and 183 preserve both licensed genders`() {
        for ((rule, words) in listOf(
            SilaMulaMangalaSalaKamalaTalaMusalaKundalaSutra to listOf("मूल", "बाल", "शूल"),
            DandaMandaKhandaSavaSaindhavaParsvaSutra to listOf("दण्ड", "शव"),
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
