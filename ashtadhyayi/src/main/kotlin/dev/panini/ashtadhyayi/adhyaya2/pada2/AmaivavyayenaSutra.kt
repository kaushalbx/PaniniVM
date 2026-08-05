package dev.panini.ashtadhyayi.adhyaya2.pada2

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
 * Sūtra 2.2.20: अमैवाव्ययेन.
 * Rules compounding of am-ending indeclinable (amanta avyaya) only with another subanta.
 */
object AmaivavyayenaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.20",
    text = "अमैवाव्ययेन",
    hindiExplanation = "अमन्त अव्यय का समर्थ सुबन्त के साथ ही समास होता है (उदा. स्वाहाकृतम्)।",
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
        val purva = context.purvaPada.upadesha
        return context.samasaType == SamasaType.TATPURUSA &&
            purva.endsWith("म्")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.2.20 forms Amanta-avyaya Tatpuruṣa compound '$compoundStem'.",
        )
    }
}
