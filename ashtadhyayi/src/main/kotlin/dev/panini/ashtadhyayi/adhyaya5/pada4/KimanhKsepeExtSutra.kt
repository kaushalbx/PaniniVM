package dev.panini.ashtadhyayi.adhyaya5.pada4

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.sutra.SamasaSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 5.4.146: किंअन्हः क्षेपे (Ext registered as 5.4.146).
 * Extended censure rule for kim-ahan.
 */
object KimanhKsepeExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.146",
    text = "किंअन्हः क्षेपे",
    hindiExplanation = "निन्दा (क्षेप) अर्थ में किम् + अहन् समास से कप् प्रत्यय होता है।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540146,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.BAHUVRIHI,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        val last = context.padas.last().upadesha
        return first == "किम्" && last == "अहन्"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = "किमह्नक"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.146 adds Samāsānta kap for kim-ahan censure compound in '$compoundStem'.",
        )
    }
}
