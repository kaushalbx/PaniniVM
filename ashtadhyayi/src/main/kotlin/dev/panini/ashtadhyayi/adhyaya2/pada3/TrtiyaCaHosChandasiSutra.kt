package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.analysis.KarakaEvidence
import dev.panini.analysis.SemanticRelation
import dev.panini.analysis.VibhaktiRuleContext
import dev.panini.analysis.VibhaktiRuleResult
import dev.panini.core.Vibhakti
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 2.3.3 तृतीया च होश्छन्दसि — Vedic hu-object may also take tṛtīyā. */
object TrtiyaCaHosChandasiSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.3",
    text = "तृतीया च होश्छन्दसि",
    hindiExplanation = "छन्दस् में हु धातु के कर्म में द्वितीया के साथ विकल्प से तृतीया होती है।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 3,
    optional = true,
    kramaValue = 230003,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            SemanticRelation.VEDIC_HU_OBJECT in context.participant?.semanticRelations.orEmpty() &&
            Vibhakti.TRTIYA in context.morphologicalCandidates

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.TRTIYA,
        KarakaEvidence(number, text, "In chandas, tṛtīyā optionally realizes the object of hu (2.3.3)."),
    )
}
