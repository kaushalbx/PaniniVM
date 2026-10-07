package dev.panini.ashtadhyayi.adhyaya2.pada2

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.core.KrtAffix
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.toDevanagari
import dev.panini.sutra.SamasaSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

object AmaivavyayenaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.20",
    text = "अमैवाव्ययेन",
    hindiExplanation = "केवल णमुल् के साथ तुल्यविधान उपपद का णमुलन्त अव्यय से समास होता है।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220020,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        return (context.samasaType == SamasaType.UPAPADA_TATPURUSA || context.samasaType == SamasaType.TATPURUSA) &&
            context.purvaPada.upapadaAffixPrescription?.affixes == setOf(KrtAffix.NAMUL) &&
            context.uttaraPada.krtAffix == KrtAffix.NAMUL && Samjna.AVYAYA in context.uttaraPada.samjnas
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.flatMap { it.varnas }.toDevanagari()
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.2.20 forms mandatory Upapada compound '$compoundStem'.",
        )
    }
}
