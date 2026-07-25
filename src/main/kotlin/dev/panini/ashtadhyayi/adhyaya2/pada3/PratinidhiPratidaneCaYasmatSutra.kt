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
 * Sūtra 2.3.11 प्रतिनिधिप्रतिदाने च यस्मात्.
 * Assigns Pañcamī in connection with representative/exchange relations (pratinidhi, pratidāna).
 */
object PratinidhiPratidaneCaYasmatSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.11", text = "प्रतिनिधिप्रतिदाने च यस्मात्",
    hindiExplanation = "प्रतिनिधि तथा प्रतिदान अर्थ में जिससे प्रति/अभि का योग हो, उसमें पञ्चमी होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230011,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            SemanticRelation.REPRESENTATIVE_EXCHANGE in context.participant?.semanticRelations.orEmpty() &&
            Vibhakti.PANCHAMI in context.morphologicalCandidates

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.PANCHAMI,
        KarakaEvidence(number, text, "पञ्चमी realizes representative/exchange relation (2.3.11)."),
    )
}
