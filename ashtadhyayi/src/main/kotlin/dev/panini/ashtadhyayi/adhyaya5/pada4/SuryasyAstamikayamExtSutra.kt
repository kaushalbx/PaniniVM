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
 * Sūtra 5.4.163: सूर्यस्यास्तमिकायाम् (Ext registered as 5.4.163).
 * Extended sunset rule for sūrya.
 */
object SuryasyAstamikayamExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.163",
    text = "सूर्यस्यास्तमिकायाम्",
    hindiExplanation = "सूर्यस्य अस्तमयन विषय में समासान्त प्रत्यय होता है।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540163,
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
        return first == "सूर्य" && (last == "अस्त" || last == "अस्तमिका")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = "सूर्यास्तमयन"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.163 derives sūryāstamayana sunset compound.",
        )
    }
}
