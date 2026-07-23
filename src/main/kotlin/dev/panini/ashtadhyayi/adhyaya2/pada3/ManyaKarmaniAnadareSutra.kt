package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.core.Karaka
import dev.panini.core.Vibhakti
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.vyakaranam.analysis.KarakaEvidence
import dev.panini.vyakaranam.analysis.VibhaktiRuleContext
import dev.panini.vyakaranam.analysis.VibhaktiRuleResult

/**
 * Sūtra 2.3.17 मन्यकर्मण्यनादरे विभाषाऽप्राणिषु.
 * Assigns optionally Caturthī or Dvitīyā for the object of root man in disregard towards non-sentient objects.
 */
object ManyaKarmaniAnadareSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.17", text = "मन्यकर्मण्यनादरे विभाषाऽप्राणिषु",
    hindiExplanation = "अनादराधिके मन्यतेः कर्मणि अप्राणििन वा चतुर्थी स्यात् पक्षे द्वितीया च।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = true, kramaValue = 230017,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.KARAKA, SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            context.karaka == Karaka.KARMAN &&
            (Vibhakti.CHATURTHI in context.morphologicalCandidates || Vibhakti.DVITIYA in context.morphologicalCandidates)

    override fun apply(context: VibhaktiRuleContext): VibhaktiRuleResult {
        val caseAssigned = if (Vibhakti.CHATURTHI in context.morphologicalCandidates) Vibhakti.CHATURTHI else Vibhakti.DVITIYA
        return VibhaktiRuleResult.Assigned(
            caseAssigned,
            KarakaEvidence(number, text, "$caseAssigned realizes object of man in inanimate disregard (2.3.17)."),
        )
    }
}
