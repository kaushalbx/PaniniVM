package dev.panini.linganushasanam.rules

import dev.panini.core.Linga
import dev.panini.core.SamasaType
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LingaRuleResult
import dev.panini.linganushasanam.LinganushasanamRule

/**
 * Pāṇinian Sūtra rules for compound gender resolution (2.4.26 परवल्लिङ्गं द्वन्द्वतत्पुरुषयोः, 2.4.17 स नपुंसकम्).
 */
object SamasaLingaRule : LinganushasanamRule {
    override val ruleId: String = "SAMASA_LINGA_2_4_26"
    override val description: String = "Sūtra 2.4.26: Compounds take uttarapada gender, or Neuter for Avyayībhāva, Dvigu, and Samāhāra Dvandva."
    override val priority: Int = 30

    override fun matches(context: LingaRuleContext): Boolean {
        return context.samasaType != null
    }

    override fun apply(context: LingaRuleContext): LingaRuleResult {
        val samasaType = context.samasaType!!
        if (samasaType == SamasaType.AVYAYIBHAVA || samasaType == SamasaType.DVIGU) {
            return LingaRuleResult.Matched(
                linga = Linga.NAPUMSAKA,
                ruleId = "2.4.17",
                explanation = "Sūtra 2.4.17 (स नपुंसकम्): Avyayībhāva/Dvigu is Neuter.",
            )
        }
        if (samasaType == SamasaType.BAHUVRIHI) {
            return LingaRuleResult.Matched(
                linga = Linga.PUMS,
                ruleId = "2.2.24",
                explanation = "Sūtra 2.2.24 (अनेकमन्यपदार्थे): Bahuvrīhi takes Anyapadārtha gender (default Masculine).",
            )
        }

        val lastPada = context.padas.lastOrNull() ?: context.pratipadika
        val innerContext = LingaRuleContext(pratipadika = lastPada, pratyaya = context.pratyaya)

        return when {
            StrilingaAffixRule.matches(innerContext) -> LingaRuleResult.Matched(
                linga = Linga.STRI,
                ruleId = "2.4.26",
                explanation = "Sūtra 2.4.26 (परवल्लिङ्गं द्वन्द्वतत्पुरुषयोः): Uttarapada '$lastPada' is Feminine.",
            )
            NapumsakalingaAffixRule.matches(innerContext) -> LingaRuleResult.Matched(
                linga = Linga.NAPUMSAKA,
                ruleId = "2.4.26",
                explanation = "Sūtra 2.4.26 (परवल्लिङ्गं द्वन्द्वतत्पुरुषयोः): Uttarapada '$lastPada' is Neuter.",
            )
            else -> LingaRuleResult.Matched(
                linga = Linga.PUMS,
                ruleId = "2.4.26",
                explanation = "Sūtra 2.4.26 (परवल्लिङ्गं द्वन्द्वतत्पुरुषयोः): Defaulting to Masculine.",
            )
        }
    }
}
