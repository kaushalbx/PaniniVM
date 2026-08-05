package dev.panini.ashtadhyayi.adhyaya2.pada1

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
 * Sūtra 2.1.55: उपमानानि सामान्यवचनैः.
 * Prescribes Karmadhāraya compound of a noun representing standard of comparison (Upamāna)
 * with a word representing the common attribute (Sāmānyavacana).
 * Example: घन इव श्यामः = घनश्यामः.
 */
object UpamananiSamanyavacanaihSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.55",
    text = "उपमानानि सामान्यवचनैः",
    hindiExplanation = "उपमानवाचक सुबन्त का सामान्यगुणवाचक सुबन्त के साथ कर्मधारय समास होता है (उदा. घनश्यामः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210055,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
), SamasaSutra {
    override val samasaType: SamasaType = SamasaType.KARMADHARAYA
    private val upamanaGana = setOf("घन", "कमल", "बिम्ब", "मृग", "चन्द्र", "विद्युत्", "शश", "मेघ")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        return purva in upamanaGana
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val stem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = stem,
            explanation = "2.1.55 forms Upamāna Karmadhāraya compound '$stem'.",
        )
    }
}
