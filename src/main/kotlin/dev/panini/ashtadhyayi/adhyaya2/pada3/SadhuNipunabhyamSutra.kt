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
import dev.panini.vyakaranam.analysis.SemanticRelation
import dev.panini.vyakaranam.analysis.VibhaktiRuleContext
import dev.panini.vyakaranam.analysis.VibhaktiRuleResult

/**
 * Sūtra 2.3.43 साधुनिपुणाभ्यामर्चायां सप्तम्यप्रतेः.
 * Assigns Saptamī in connection with sādhu or nipuṇa expressing respect.
 */
object SadhuNipunabhyamSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.43", text = "साधुनिपुणाभ्यामर्चायां सप्तम्यप्रतेः",
    hindiExplanation = "साधु निपुण इत्येतैर्योगे अर्चायां द्योत्यायां सप्तमी स्यात्, न तु प्रतिना योगे।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230043,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.KARAKA, SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            (context.karaka == Karaka.ADHIKARANA || context.karaka == Karaka.ANIRDHARITA) &&
            Vibhakti.SAPTAMI in context.morphologicalCandidates &&
            (context.participant?.semanticRelations.orEmpty().isEmpty() || SemanticRelation.ACTION_MARKING in context.participant?.semanticRelations.orEmpty())

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.SAPTAMI,
        KarakaEvidence(number, text, "सप्तमी realizes respect/aptitude with sādhu/nipuṇa (2.3.43)."),
    )
}
