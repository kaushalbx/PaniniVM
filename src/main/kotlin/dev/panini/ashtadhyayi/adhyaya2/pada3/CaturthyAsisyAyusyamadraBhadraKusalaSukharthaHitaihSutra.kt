package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.core.Vibhakti
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.analysis.KarakaEvidence
import dev.panini.analysis.SemanticRelation
import dev.panini.analysis.VibhaktiRuleContext
import dev.panini.analysis.VibhaktiRuleResult

/**
 * Sūtra 2.3.73 चतुर्थ्यशीष्यायुष्यमद्रभद्रकुशलसुखार्थहितैः.
 * Assigns Caturthī or Ṣaṣṭhī in benediction with āyuṣya, madra, bhadra, kuśala, sukha, artha, hita.
 */
object CaturthyAsisyAyusyamadraBhadraKusalaSukharthaHitaihSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.73", text = "चतुर्थ्यशीष्यायुष्यमद्रभद्रकुशलसुखार्थहितैः",
    hindiExplanation = "आशीर्वाद अर्थ में आयुष्य, मद्र, भद्र, कुशल, सुख, अर्थ, हित शब्दों के योग में चतुर्थी अथवा षष्ठी होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = true, kramaValue = 230073,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            SemanticRelation.BENEDICTION_WELLBEING in context.participant?.semanticRelations.orEmpty() &&
            (Vibhakti.CHATURTHI in context.morphologicalCandidates || Vibhakti.SASTHI in context.morphologicalCandidates)

    override fun apply(context: VibhaktiRuleContext): VibhaktiRuleResult {
        val v = if (Vibhakti.CHATURTHI in context.morphologicalCandidates) Vibhakti.CHATURTHI else Vibhakti.SASTHI
        return VibhaktiRuleResult.Assigned(
            v,
            KarakaEvidence(number, text, "$v realizes benediction / well-being relation (2.3.73)."),
        )
    }
}
