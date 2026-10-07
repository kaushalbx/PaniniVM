package dev.panini.sutra

import dev.panini.analysis.*
import dev.panini.ashtadhyayi.adhyaya2.pada1.KrtyairRneSutra
import dev.panini.ashtadhyayi.adhyaya2.pada2.NisthaBahuvrihauSutra
import dev.panini.core.*
import kotlin.test.*

class CompoundAffixIdentityTest {
    @Test fun `nistha ordering uses derivational affix not ta spelling`() {
        fun c(stem: String, affix: KrtAffix?) = SamasaRuleContext(
            listOf(SamasaPada(stem, krtAffix=affix), SamasaPada("कृत्य")), SamasaType.BAHUVRIHI,
        )
        assertTrue(NisthaBahuvrihauSutra.matches(c("कृत", KrtAffix.KTA)))
        assertTrue(NisthaBahuvrihauSutra.matches(c("कृतवत्", KrtAffix.KTAVATU)))
        assertFalse(NisthaBahuvrihauSutra.matches(c("कृत", null)))
        assertFalse(NisthaBahuvrihauSutra.matches(c("शत", null)))
        assertFalse(NisthaBahuvrihauSutra.matches(c("कृत", KrtAffix.YAT)))
    }

    @Test fun `debt compound requires locative yat and obligation together`() {
        val input=SamasaRuleContext(
            listOf(SamasaPada("मास", Vibhakti.SAPTAMI), SamasaPada("देय", krtAffix=KrtAffix.YAT)),
            SamasaType.TATPURUSA, semanticRelations=setOf(SamasaSemanticRelation.DEBT_OR_OBLIGATION),
        )
        assertTrue(KrtyairRneSutra.matches(input))
        assertEquals("मासदेय", (KrtyairRneSutra.apply(input) as SamasaRuleResult.Formed).compoundStem)
        assertFalse(KrtyairRneSutra.matches(input.copy(semanticRelations=emptySet())))
        assertFalse(KrtyairRneSutra.matches(input.copy(padas=listOf(input.purvaPada.copy(vibhakti=Vibhakti.TRTIYA), input.uttaraPada))))
        for (affix in listOf(null, KrtAffix.NYAT, KrtAffix.TAVYAT, KrtAffix.ANIYAR)) {
            assertFalse(KrtyairRneSutra.matches(input.copy(padas=listOf(input.purvaPada, input.uttaraPada.copy(krtAffix=affix)))))
        }
    }
}
