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

/**
 * Sūtra 2.3.39 स्वाम्यीश्वराधिपतिदायादसाक्षीप्रतिभूप्रसूतैश्च.
 * Assigns Ṣaṣṭhī or Saptamī in connection with svāmin, īśvara, adhipati, dāyāda, sākṣin, pratibhū, prasūta.
 */
object SwamyIsvaraAdhipatiSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.39", text = "स्वाम्यीश्वराधिपतिदायादसाक्षीप्रतिभूप्रसूतैश्च",
    hindiExplanation = "स्वाम्यादिभिर्योगे षष्ठीसप्तम्यौ स्तः।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = true, kramaValue = 230039,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.KARAKA, SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            SemanticRelation.OWNERSHIP_SWAMIN in context.participant?.semanticRelations.orEmpty() &&
            (Vibhakti.SASTHI in context.morphologicalCandidates || Vibhakti.SAPTAMI in context.morphologicalCandidates)

    override fun apply(context: VibhaktiRuleContext): VibhaktiRuleResult {
        val caseAssigned = if (Vibhakti.SASTHI in context.morphologicalCandidates) Vibhakti.SASTHI else Vibhakti.SAPTAMI
        return VibhaktiRuleResult.Assigned(
            caseAssigned,
            KarakaEvidence(number, text, "$caseAssigned expresses ownership or witnessing relation (2.3.39)."),
        )
    }
}
