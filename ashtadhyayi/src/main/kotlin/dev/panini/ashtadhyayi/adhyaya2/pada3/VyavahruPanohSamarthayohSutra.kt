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
 * Sūtra 2.3.57 व्यवहृपणोः समर्थयोः.
 * Assigns Ṣaṣṭhī for the karman of vi-ava-hṛ and paṇ in transaction/gambling.
 */
object VyavahruPanohSamarthayohSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.57", text = "व्यवहृपणोः समर्थयोः",
    hindiExplanation = "समान अर्थ वाले वि-अव-हृ तथा पण् धातु के कर्म में षष्ठी विभक्ति होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230057,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            SemanticRelation.TRANSACTION_GAMBLING_OBJECT in context.participant?.semanticRelations.orEmpty() &&
            Vibhakti.SASTHI in context.morphologicalCandidates

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.SASTHI,
        KarakaEvidence(number, text, "षष्ठी realizes karman of vi-ava-hṛ and paṇ in transaction/gambling (2.3.57)."),
    )
}
