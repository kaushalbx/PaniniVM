package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.core.Vibhakti
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.analysis.KarakaEvidence
import dev.panini.analysis.VibhaktiRuleContext
import dev.panini.analysis.VibhaktiRuleResult

import dev.panini.analysis.SemanticRelation

/** Sūtra 2.3.19 सहयुक्तोऽप्रधाने. Assigns Trtīyā to a non-principal participant associated with saha/sākam/sārdham/samam. */
object SahaYukteApradhaneSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.19", text = "सहयुक्तोऽप्रधाने",
    hindiExplanation = "सहार्थेन युक्ते अप्रधाने तृतीया स्यात्।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230019,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.KARAKA, SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            Vibhakti.TRTIYA in context.morphologicalCandidates &&
            SemanticRelation.ACCOMPANIMENT in context.participant?.semanticRelations.orEmpty()

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.TRTIYA,
        KarakaEvidence(number, text, "तृतीया realizes secondary participant in accompaniment with saha (2.3.19)."),
    )
}
