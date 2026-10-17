package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya2.pada4.LutahPrathamasyaDarauRasahSutra
import dev.panini.core.Lakara
import dev.panini.core.TingAffix
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import kotlin.test.*

class LutEndingVarnaTest {
    private val rule = LutahPrathamasyaDarauRasahSutra

    @Test fun `both padas receive exact replacements without reselecting the sthanin`() {
        for ((affix, expected) in listOf(
            TingAffix.TIP to listOf(Vyanjana.DDA, Svara.AA),
            TingAffix.TA to listOf(Vyanjana.DDA, Svara.AA),
            TingAffix.TAS to listOf(Vyanjana.RA, Svara.AU),
            TingAffix.ATAM to listOf(Vyanjana.RA, Svara.AU),
            TingAffix.JHI to listOf(Vyanjana.RA, Svara.A, Vyanjana.SA),
            TingAffix.JHA to listOf(Vyanjana.RA, Svara.A, Vyanjana.SA),
        )) {
            val original = affix.term("ending")
            val state = DerivationState(listOf(original), context = DerivationalContext(rupa = Rupa(lakara = Lakara.LUT)))
            assertTrue(rule.matches(state))
            val result = rule.apply(state).state
            val ending = result.terms.single()
            assertEquals(expected, ending.varnas)
            assertEquals(original.id, ending.id)
            assertEquals(affix, ending.sourceTingAffix)
            assertEquals(expected.first() == Vyanjana.DDA, ending.itProcessingPending)
            assertFalse(rule.matches(result))
            assertEquals(affix.upadesha, ending.sthaniProps?.upadesha)
            if (expected.first() == Vyanjana.DDA) assertEquals(rule.sutra, ending.createdBySutra)
        }
    }

    @Test fun `other purushas lakaras and nonaffix spellings are excluded`() {
        for (affix in listOf(TingAffix.SIP, TingAffix.MIP, TingAffix.THAS_A, TingAffix.IT)) {
            assertFalse(rule.matches(DerivationState(listOf(affix.term("ending")),
                context = DerivationalContext(rupa = Rupa(lakara = Lakara.LUT)))))
        }
        assertFalse(rule.matches(DerivationState(listOf(TingAffix.TIP.term("ending")),
            context = DerivationalContext(rupa = Rupa(lakara = Lakara.LAT)))))
        assertFalse(rule.matches(DerivationState(listOf(DerivationTerm("word", "तिप्", TermKind.PRATIPADIKA)),
            context = DerivationalContext(rupa = Rupa(lakara = Lakara.LUT)))))
    }
}
