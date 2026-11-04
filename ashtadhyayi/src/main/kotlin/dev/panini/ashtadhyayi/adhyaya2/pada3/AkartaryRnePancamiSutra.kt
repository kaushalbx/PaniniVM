package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.analysis.KarakaEvidence
import dev.panini.analysis.SemanticRelation
import dev.panini.analysis.VibhaktiRuleContext
import dev.panini.analysis.VibhaktiRuleResult
import dev.panini.core.Vibhakti
import dev.panini.core.Karaka
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 2.3.24 अकर्तर्यृणे पञ्चमी. */
object AkartaryRnePancamiSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.24",
    text = "अकर्तर्यृणे पञ्चमी",
    hindiExplanation = "ऋण के हेतु में, कर्ता न होने पर, पञ्चमी होती है।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 3,
    optional = false,
    kramaValue = 230024,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.KARAKA, SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    fun hasConstruction(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            !SasthiHetuprayogeSutra.hasConstruction(context) &&
            context.karaka != Karaka.KARTR &&
            SemanticRelation.NON_AGENT_DEBT_REFERENCE in context.participant?.semanticRelations.orEmpty()

    override fun matches(context: VibhaktiRuleContext): Boolean =
        hasConstruction(context) &&
            Vibhakti.PANCHAMI in context.morphologicalCandidates

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.PANCHAMI,
        KarakaEvidence(number, text, "Pañcamī marks a non-agent reference in a debt relation (2.3.24)."),
    )
}
