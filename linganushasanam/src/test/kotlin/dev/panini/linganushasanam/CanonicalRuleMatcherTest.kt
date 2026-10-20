package dev.panini.linganushasanam

import dev.panini.core.Linga
import dev.panini.core.LingaSemanticClass
import dev.panini.core.SamasaType
import dev.panini.core.Vacana
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CanonicalRuleMatcherTest {
    @Test
    fun `canonical affix and ending rules resolve directly`() {
        assertRule("9", Linga.STRI, LingaRuleContext("कृति", pratyaya = "क्तिन्"))
        assertRule("10", Linga.STRI, LingaRuleContext("लक्ष्मी"))
        assertRule("17", Linga.STRI, LingaRuleContext("शुक्लता", pratyaya = "तल्"))
        assertRule(
            "120",
            Linga.NAPUMSAKA,
            LingaRuleContext("कृत", pratyaya = "क्त", semanticClasses = setOf(LingaSemanticClass.BHAVA)),
        )
        assertRule("121", Linga.NAPUMSAKA, LingaRuleContext("गुरुत्व", pratyaya = "त्व"))
        assertRule("187", Linga.STRI, LingaRuleContext("कर्तव्य", pratyaya = "तव्य", governingLinga = Linga.STRI))
    }

    @Test
    fun `canonical compound and semantic rules resolve directly`() {
        assertRule(
            "124", Linga.NAPUMSAKA,
            LingaRuleContext("पाणिपाद", samasaType = SamasaType.DVANDVA, vacana = Vacana.EKAVACANA),
        )
        assertRule(
            "188", Linga.STRI,
            LingaRuleContext(
                "करण",
                pratyaya = "ल्युट्",
                semanticClasses = setOf(LingaSemanticClass.INSTRUMENT),
                governingLinga = Linga.STRI,
            ),
        )
        assertRule(
            "43", Linga.PUMS,
            LingaRuleContext("इन्द्र", semanticClasses = setOf(LingaSemanticClass.DEVA_ABHIDHANA)),
        )
    }

    @Test
    fun `canonical exception rules expose their actual optional gender scopes`() {
        assertEquals(setOf(Linga.STRI, Linga.PUMS), LinganushasanamRegistry.require("5").targetLingas)
        assertEquals(setOf(Linga.STRI, Linga.NAPUMSAKA), LinganushasanamRegistry.require("21").targetLingas)
        assertEquals(setOf(Linga.PUMS, Linga.NAPUMSAKA), LinganushasanamRegistry.require("47").targetLingas)
        assertEquals(Linga.entries.toSet(), LinganushasanamRegistry.require("174").targetLingas)
    }

    private fun assertRule(number: String, linga: Linga, context: LingaRuleContext) {
        val sutra = LinganushasanamRegistry.require(number)
        val result = sutra.apply(context) as LingaRuleResult.Matched
        assertEquals(true, sutra.matches(context))
        assertEquals(number, result.ruleId)
        assertTrue(linga in result.lingas)
    }
}
