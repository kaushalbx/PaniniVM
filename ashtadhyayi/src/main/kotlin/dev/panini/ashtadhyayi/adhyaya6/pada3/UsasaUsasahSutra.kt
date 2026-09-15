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
 * Sūtra 6.3.31: उषासोषसः.
 * Aluk / Pūrvapada rule for uṣas in Devatā-dvandva.
 * Example: उषासानक्ता.
 */
object UsasaUsasahSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.31",
    text = "उषासोषसः",
    hindiExplanation = "नक्ता उत्तरपद परे होने पर उषस् शब्द का उषासा रूप निष्पन्न होता है (उदा. उषासानक्ता)।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630031,
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
            (first == "उषस्" || first == "उषास्") && last == "नक्ता"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = "उषासानक्ता"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.31 forms uṣāsānaktā in '$compoundStem'.",
            memberEdits = mapOf(0 to "उषासा",1 to "नक्ता"),
        )
    }
}
