package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.core.Vibhakti
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.vyakaranam.analysis.KarakaEvidence
import dev.panini.vyakaranam.analysis.SemanticRelation
import dev.panini.vyakaranam.analysis.VibhaktiRuleContext
import dev.panini.vyakaranam.analysis.VibhaktiRuleResult

/**
 * Sūtra 2.3.7 सप्तमीपञ्चम्यौ कारकमध्ये.
 * Assigns Saptamī or Pañcamī for time or distance intervening between two kārakas.
 */
object SaptamiPancamyauKarakamadhyeSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.7", text = "सप्तमीपञ्चम्यौ कारकमध्ये",
    hindiExplanation = "दो कारकों के मध्यवर्ती काल अथवा अध्व (मार्ग) का बोध कराने वाले शब्द में सप्तमी अथवा पञ्चमी विभक्ति होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = true, kramaValue = 230007,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            SemanticRelation.INTERVENING_DURATION_DISTANCE in context.participant?.semanticRelations.orEmpty() &&
            (Vibhakti.SAPTAMI in context.morphologicalCandidates || Vibhakti.PANCHAMI in context.morphologicalCandidates)

    override fun apply(context: VibhaktiRuleContext): VibhaktiRuleResult {
        val v = if (Vibhakti.SAPTAMI in context.morphologicalCandidates) Vibhakti.SAPTAMI else Vibhakti.PANCHAMI
        return VibhaktiRuleResult.Assigned(
            v,
            KarakaEvidence(number, text, "$v realizes intervening duration/distance between kārakas (2.3.7)."),
        )
    }
}
