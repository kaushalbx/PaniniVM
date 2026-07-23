package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.core.Karaka
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
 * Sūtra 2.3.32 पृथग्विनानानाभिस्तृतीयान्तरस्याम्.
 * Assigns Trtīyā, Pañcamī, or Dvitīyā in connection with pṛthag, vinā, and nānā.
 */
object PrthagVinaNanabhihSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.32", text = "पृथग्विनानानाभिस्तृतीयान्तरस्याम्",
    hindiExplanation = "पृथक-विना-नाना इत्येतैर्योगे तृतीया स्यात्, पक्षे पञ्चमी द्वितीया च।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = true, kramaValue = 230032,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.KARAKA, SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            SemanticRelation.EXCLUSION_VINA in context.participant?.semanticRelations.orEmpty() &&
            (Vibhakti.TRTIYA in context.morphologicalCandidates || Vibhakti.PANCHAMI in context.morphologicalCandidates || Vibhakti.DVITIYA in context.morphologicalCandidates)

    override fun apply(context: VibhaktiRuleContext): VibhaktiRuleResult {
        val caseAssigned = when {
            Vibhakti.TRTIYA in context.morphologicalCandidates -> Vibhakti.TRTIYA
            Vibhakti.PANCHAMI in context.morphologicalCandidates -> Vibhakti.PANCHAMI
            else -> Vibhakti.DVITIYA
        }
        return VibhaktiRuleResult.Assigned(
            caseAssigned,
            KarakaEvidence(number, text, "$caseAssigned realizes exclusion/separation with pṛthag/vinā/nānā (2.3.32)."),
        )
    }
}
