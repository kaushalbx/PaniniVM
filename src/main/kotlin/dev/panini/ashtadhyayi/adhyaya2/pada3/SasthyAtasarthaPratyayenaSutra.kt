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
 * Sūtra 2.3.30 षष्ठ्यतसर्थप्रत्ययेन.
 * Assigns Ṣaṣṭhī in connection with words ending in suffixes having the sense of 'atas' (purastāt, upari, adhaḥ).
 */
object SasthyAtasarthaPratyayenaSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.30", text = "षष्ठ्यतसर्थप्रत्ययेन",
    hindiExplanation = "अतस्-अर्थ वाले प्रत्ययों के योग में षष्ठी विभक्ति होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230030,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            SemanticRelation.SPATIAL_DIRECTION in context.participant?.semanticRelations.orEmpty() &&
            Vibhakti.SASTHI in context.morphologicalCandidates

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.SASTHI,
        KarakaEvidence(number, text, "षष्ठी realizes relation with atas-artha spatial direction suffix (2.3.30)."),
    )
}
