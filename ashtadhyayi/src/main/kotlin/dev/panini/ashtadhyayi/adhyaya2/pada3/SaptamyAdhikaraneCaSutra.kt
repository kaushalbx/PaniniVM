package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.core.Karaka
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
import dev.panini.analysis.OptionalVibhaktiRuleOutcome
import dev.panini.analysis.DistanceCaseConstructions

object SaptamyAdhikaraneCaSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.36", text = "सप्तम्यधिकरणे च", hindiExplanation = "अनभिहित अधिकरण में सप्तमी होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230036,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.KARAKA, SutraInput.SEMANTIC_FEATURE), adhikara = setOf("2.3.1"),
), OptionalVibhaktiRuleOutcome {
    override val permittedCases = emptySet<Vibhakti>()
    override fun permittedCases(context: VibhaktiRuleContext) =
        if (DistanceCaseConstructions.isNonSubstanceWord(context)) DurantikarthebhyoDvitiyaCaSutra.permittedCases else emptySet()
    override fun alternativeRuleNumbers(context: VibhaktiRuleContext) = setOf("2.3.35", "2.3.36")
    override fun matches(context: VibhaktiRuleContext) = !context.abhihita &&
        Vibhakti.SAPTAMI in context.morphologicalCandidates &&
        (context.karaka == Karaka.ADHIKARANA || DistanceCaseConstructions.isNonSubstanceWord(context)) &&
        !NaksatreCaLupiSutra.hasNakshatraLupLocus(context)
    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.SAPTAMI,
        KarakaEvidence(number, text, if (DistanceCaseConstructions.isNonSubstanceWord(context))
            "सप्तमी realizes the non-substance distance/proximity word under च (2.3.36)."
            else "सप्तमी realizes ${context.karaka} in this construction."),
    )
}
