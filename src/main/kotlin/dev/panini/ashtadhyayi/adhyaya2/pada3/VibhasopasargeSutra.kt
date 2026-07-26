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
 * Sūtra 2.3.59 विभाषोपसर्गे.
 * Option for Ṣaṣṭhī (or Dvitīyā) when vi-ava-hṛ or paṇ is prefixed with an upasarga.
 */
object VibhasopasargeSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.59", text = "विभाषोपसर्गे",
    hindiExplanation = "उपसर्गयुक्त व्यवहार/पण् धातुओं के कर्म में षष्ठी अथवा द्वितीया विभक्ति विकल्प से होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = true, kramaValue = 230059,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            SemanticRelation.TRANSACTION_GAMBLING_OBJECT in context.participant?.semanticRelations.orEmpty() &&
            (Vibhakti.SASTHI in context.morphologicalCandidates || Vibhakti.DVITIYA in context.morphologicalCandidates)

    override fun apply(context: VibhaktiRuleContext): VibhaktiRuleResult {
        val v = if (Vibhakti.SASTHI in context.morphologicalCandidates) Vibhakti.SASTHI else Vibhakti.DVITIYA
        return VibhaktiRuleResult.Assigned(
            v,
            KarakaEvidence(number, text, "$v realizes karman for prefixed transaction verb (2.3.59)."),
        )
    }
}
