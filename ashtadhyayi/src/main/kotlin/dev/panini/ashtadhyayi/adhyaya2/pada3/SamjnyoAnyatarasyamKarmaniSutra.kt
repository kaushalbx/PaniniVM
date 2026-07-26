package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.core.Karaka
import dev.panini.core.Vibhakti
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.analysis.KarakaEvidence
import dev.panini.analysis.VibhaktiRuleContext
import dev.panini.analysis.VibhaktiRuleResult

/**
 * Sūtra 2.3.22 संज्ञोऽन्यतरस्यांकर्मणि.
 * Option for Tṛtīyā (or Dvitīyā) for the karman of sam + jñā.
 */
object SamjnyoAnyatarasyamKarmaniSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.22", text = "संज्ञोऽन्यतरस्यांकर्मणि",
    hindiExplanation = "सम्-पूर्वक ज्ञ् धातु के कर्म में तृतीया अथवा द्वितीया विभक्ति होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = true, kramaValue = 230022,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.KARAKA, SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            context.karaka == Karaka.KARMAN &&
            (Vibhakti.TRTIYA in context.morphologicalCandidates || Vibhakti.DVITIYA in context.morphologicalCandidates)

    override fun apply(context: VibhaktiRuleContext): VibhaktiRuleResult {
        val v = if (Vibhakti.TRTIYA in context.morphologicalCandidates) Vibhakti.TRTIYA else Vibhakti.DVITIYA
        return VibhaktiRuleResult.Assigned(
            v,
            KarakaEvidence(number, text, "$v realizes karman for sam-jñā dhātu (2.3.22)."),
        )
    }
}
