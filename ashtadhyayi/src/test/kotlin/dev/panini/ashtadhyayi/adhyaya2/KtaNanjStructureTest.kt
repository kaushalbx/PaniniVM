package dev.panini.ashtadhyayi.adhyaya2

import dev.panini.analysis.SamasaPada
import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.ashtadhyayi.adhyaya2.pada1.KtenaNanjVisistenaSutra
import dev.panini.core.KrtAffix
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class KtaNanjStructureTest {
    private fun context(left: SamasaPada, right: SamasaPada) =
        SamasaRuleContext(listOf(left, right), SamasaType.KARMADHARAYA)

    @Test
    fun `classification preserves member spelling and explicit structure before boundary sandhi`() {
        val base = SamasaPada("कृत", krtAffix = KrtAffix.KTA)
        val negative = SamasaPada("अकृत॑", nanjBase = base)
        val input = context(base, negative)
        val formed = KtenaNanjVisistenaSutra.apply(input) as SamasaRuleResult.Formed
        assertEquals("कृतअकृत॑", formed.compoundStem)
        assertEquals(base, negative.nanjBase)
        assertTrue(formed.memberEdits.isEmpty())
        assertEquals(listOf(base, negative), input.padas)
    }

    @Test
    fun `same kta base qualified only by nanj is applicable with either negation allomorph`() {
        for ((positive, negative) in listOf("कृत" to "अकृत", "भुक्त" to "अभुक्त", "पीत" to "अपीत", "उदित" to "अनुदित")) {
            val base = SamasaPada(positive, krtAffix = KrtAffix.KTA)
            val negated = SamasaPada(negative, nanjBase = base)
            assertTrue(KtenaNanjVisistenaSutra.matches(context(base, negated)), negative)
        }
    }

    @Test
    fun `initial a and matching text do not establish kta or nanj`() {
        val base = SamasaPada("कृत", krtAffix = KrtAffix.KTA)
        assertFalse(KtenaNanjVisistenaSutra.matches(context(base, SamasaPada("अकृत"))))
        val untyped = SamasaPada("कृत")
        assertFalse(KtenaNanjVisistenaSutra.matches(context(untyped, SamasaPada("अकृत", nanjBase = untyped))))
    }

    @Test
    fun `different bases affixes cases or negation on both members are excluded`() {
        val base = SamasaPada("कृत", krtAffix = KrtAffix.KTA)
        val negated = SamasaPada("अकृत", nanjBase = base)
        assertFalse(KtenaNanjVisistenaSutra.matches(context(SamasaPada("सिद्ध", krtAffix = KrtAffix.KTA), negated)))
        assertFalse(KtenaNanjVisistenaSutra.matches(context(base.copy(krtAffix = KrtAffix.KTAVATU), negated)))
        assertFalse(KtenaNanjVisistenaSutra.matches(context(base, negated.copy(vibhakti = Vibhakti.DVITIYA))))
        assertFalse(KtenaNanjVisistenaSutra.matches(context(negated, negated)))
        assertFalse(KtenaNanjVisistenaSutra.matches(context(base, negated.copy(nanjBase = negated))))
    }
}
