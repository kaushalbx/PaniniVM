package dev.panini.ashtadhyayi.adhyaya2.pada3

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

import dev.panini.vyakaranam.analysis.SemanticRelation

/** Sūtra 2.3.41 यतश्च निर्धारणम्. Assigns Ṣaṣṭhī or Saptamī when selecting/distinguishing a member from a group. */
object YatasCaNirdharanamSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.41", text = "यतश्च निर्धारणम्",
    hindiExplanation = "जातिगुणक्रियासंज्ञाभिः समुदायदेकदेशस्य पृथक्करणं निर्धारणम्, यतः क्रियते ततः षष्ठीसप्तम्यौ स्तः।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = true, kramaValue = 230041,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.KARAKA, SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            SemanticRelation.GROUP_SELECTION in context.participant?.semanticRelations.orEmpty() &&
            (Vibhakti.SASTHI in context.morphologicalCandidates || Vibhakti.SAPTAMI in context.morphologicalCandidates)

    override fun apply(context: VibhaktiRuleContext): VibhaktiRuleResult {
        val caseAssigned = if (Vibhakti.SASTHI in context.morphologicalCandidates) Vibhakti.SASTHI else Vibhakti.SAPTAMI
        return VibhaktiRuleResult.Assigned(
            caseAssigned,
            KarakaEvidence(number, text, "$caseAssigned assigns group selection / specification (2.3.36 यतश्च निर्धारणम्)."),
        )
    }
}
