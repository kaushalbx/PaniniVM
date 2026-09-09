package dev.panini.ashtadhyayi.adhyaya6.pada3

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.analysis.SamasaSemanticRelation
import dev.panini.core.SamasaType
import dev.panini.sutra.SamasaSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 6.3.33: पितरामातरा च च्छन्दसि.
 * Vedic nipātana; it must not leak into ordinary-language derivation.
 */
object PitramataSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.33",
    text = "पितरामातरा च च्छन्दसि",
    hindiExplanation = "छन्दस् में पितरामातरा रूप निपातित है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630033,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.DVANDVA,
    samasaPriority = 30,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        val last = context.padas.last().upadesha
        return context.samasaType == SamasaType.DVANDVA &&
            SamasaSemanticRelation.VEDIC_REGISTER in context.semanticRelations &&
            (first == "पितृ" || first == "पिता") && (last == "मातृ" || last == "माता")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = "पितरामातृ"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.33 forms pitāmātṛ in '$compoundStem'.",
            memberEdits = mapOf(0 to "पितरा",1 to "मातृ"),
        )
    }
}
