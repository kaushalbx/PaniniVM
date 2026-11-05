package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.analysis.*
import dev.panini.core.Vibhakti
import dev.panini.core.Karaka
import dev.panini.sutra.*

/** 2.3.66 उभयप्राप्तौ कर्मणि. */
object UbhayapraptauKarmaniSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.66", text = "उभयप्राप्तौ कर्मणि",
    hindiExplanation = "कृत्-प्रयोग में कर्ता और कर्म दोनों को षष्ठी प्राप्त होने पर केवल कर्म में षष्ठी होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230066,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.KARAKA, SutraInput.SEMANTIC_FEATURE), adhikara = setOf("2.3.1"), anuvrtti = setOf("2.3.50"),
) {
    override fun matches(context: VibhaktiRuleContext) = !context.abhihita &&
        !GatyarthaKarmaniDvitiyaCaturthyauSutra.isApplicableDomain(context) &&
        !AdhikaranavacinacCaSutra.matches(context) &&
        context.karaka == Karaka.KARMAN &&
        SemanticRelation.KRT_GOVERNED_PARTICIPANT in context.participant?.semanticRelations.orEmpty() &&
        SemanticRelation.KRT_DUAL_ELIGIBLE_OBJECT in context.participant?.semanticRelations.orEmpty() &&
        Vibhakti.SASTHI in context.morphologicalCandidates

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.SASTHI, KarakaEvidence(number, text, "When agent and object are both eligible under a kṛt form, ṣaṣṭhī is restricted to the object (2.3.66)."),
    )
}
