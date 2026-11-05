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
 * Sūtra 2.3.42 पञ्चमी विभक्ते.
 * Assigns Pañcamī to an entity from which a distinction/division (vibhakta) is drawn.
 */
object PancamiVibhakteSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.42", text = "पञ्चमी विभक्ते",
    hindiExplanation = "निर्धारण में व्यक्त आधार पर पृथक् समूहों की तुलना के अवधिभूत पद में पञ्चमी होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230042,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
    anuvrtti = setOf("2.3.41"),
) {
    /** Applicability independent of observed case, so the exception cannot fall back to 2.3.41. */
    fun hasDistinctReference(context: VibhaktiRuleContext): Boolean {
        val relations = context.participant?.semanticRelations.orEmpty()
        return SemanticRelation.COMPARATIVE_DISTINCTION in relations &&
            SemanticRelation.NIRDHARANA_REFERENCE in relations &&
            SemanticRelation.SEPARATE_COMPARISON_GROUPS in relations
    }

    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            hasDistinctReference(context) &&
            Vibhakti.PANCHAMI in context.morphologicalCandidates

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.PANCHAMI,
        KarakaEvidence(number, text, "पञ्चमी realizes comparative distinction (2.3.42)."),
    )
}
