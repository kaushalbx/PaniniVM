package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.core.Vibhakti
import dev.panini.core.Karaka
import dev.panini.core.KrtAffix
import dev.panini.vyakaranam.ast.isRecognizedStriAffix
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
 * Sūtra 2.3.68 अधिकरणवाचिनश्च.
 * Assigns Ṣaṣṭhī in connection with a kta-participle expressing location/adhikaraṇa.
 */
object AdhikaranavacinacCaSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.68", text = "अधिकरणवाचिनश्च",
    hindiExplanation = "अधिकरण (स्थान) अर्थ वाले क्त-प्रत्ययान्त शब्द के योग में षष्ठी विभक्ति होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230068,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            context.karaka in setOf(Karaka.KARTR, Karaka.KARMAN) &&
            context.governingKrt?.krtAffix == KrtAffix.KTA &&
            context.governingKrt?.vikaras?.all { it.isRecognizedStriAffix } == true &&
            Karaka.ADHIKARANA in context.governingKrtExpressedRoles.orEmpty() &&
            context.karaka !in context.governingKrtExpressedRoles.orEmpty() &&
            SemanticRelation.KRT_GOVERNED_PARTICIPANT in context.participant?.semanticRelations.orEmpty() &&
            SemanticRelation.LOCATION_PARTICIPLE_RELATION in context.participant?.semanticRelations.orEmpty() &&
            Vibhakti.SASTHI in context.morphologicalCandidates

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.SASTHI,
        KarakaEvidence(number, text, "षष्ठी realizes connection with location-denoting kta-participle (2.3.68)."),
    )
}
