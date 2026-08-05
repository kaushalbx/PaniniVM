package dev.panini.ashtadhyayi.adhyaya2.pada1

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.shiksha.Samjna
import dev.panini.sutra.SamasaSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 2.1.52: संख्यापूर्वो द्विगुः.
 * Prescribes Dvigu Tatpuruṣa compound when the prior member (pūrvapada) is a numeral (Saṅkhyā).
 * Example: त्रि + भुवन = त्रिभुवनम्.
 */
object SankhyapurvoDviguhSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.52",
    text = "संख्यापूर्वो द्विगुः",
    hindiExplanation = "जिस कर्मधारय समास में पूर्वपद संख्यावाचक हो, उसकी 'द्विगु' संज्ञा और समास होता है (उदा. त्रिभुवनम्)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210052,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.DVIGU,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada
        return purva.samjnas.contains(Samjna.SANKHYA) || isSankhyaStem(purva.upadesha)
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val stem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = stem,
            explanation = "2.1.52 forms Dvigu Tatpuruṣa compound '$stem'.",
        )
    }

    private fun isSankhyaStem(stem: String): Boolean =
        stem in setOf("एक", "द्वि", "त्रि", "चतुर्", "पञ्चन्", "षष्", "सप्तन्", "अष्टन्", "नवन्", "दशन्", "त्रि", "द्वि") ||
                stem.startsWith("त्रि") || stem.startsWith("द्वि") || stem.startsWith("पञ्च") || stem.startsWith("चतुर्")
}
