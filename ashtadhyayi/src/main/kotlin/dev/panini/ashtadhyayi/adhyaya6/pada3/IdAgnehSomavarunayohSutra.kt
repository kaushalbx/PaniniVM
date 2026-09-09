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
 * Sūtra 6.3.27: ईदग्नेः सोमवरुणयोः.
 * Long ī substitution for agni before soma or varuṇa (agnīṣomau).
 */
object IdAgnehSomavarunayohSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.27",
    text = "ईदग्नेः सोमवरुणयोः",
    hindiExplanation = "अग्नि शब्द को सोम अथवा वरुण परे होने पर ईकार आदेश होता है (उदा. अग्नीषोमौ)।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630027,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.DVANDVA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        val last = context.padas.last().upadesha
        return context.samasaType == SamasaType.DVANDVA &&
            SamasaSemanticRelation.DEVATA_COORDINATION in context.semanticRelations &&
            first == "अग्नि" && (last == "सोम" || last == "वरुण")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val last = context.padas.last().upadesha
        val compoundStem = "अग्नी" + last
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.27 substitutes long ī for agni in '$compoundStem'.",
            memberEdits = mapOf(0 to "अग्नी"),
        )
    }
}
