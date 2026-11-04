package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.analysis.KarakaEvidence
import dev.panini.analysis.SemanticRelation
import dev.panini.analysis.VibhaktiRuleContext
import dev.panini.analysis.VibhaktiRuleResult
import dev.panini.analysis.OptionalVibhaktiRuleOutcome
import dev.panini.analysis.DistanceCaseConstructions
import dev.panini.core.Vibhakti
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 2.3.34 दूरान्तिकार्थैः षष्ठ्यन्यतरस्याम्. */
object DurantikarthaihSasthiAnyatarasyamSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.34",
    text = "दूरान्तिकार्थैः षष्ठ्यन्यतरस्याम्",
    hindiExplanation = "दूर अथवा समीप अर्थ वाले शब्दों के योग में विकल्प से षष्ठी होती है।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 3,
    optional = true,
    kramaValue = 230034,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
), OptionalVibhaktiRuleOutcome {
    override val permittedCases = setOf(Vibhakti.SASTHI, Vibhakti.PANCHAMI)
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            DistanceCaseConstructions.isReference(context) &&
            context.morphologicalCandidates.any { it in permittedCases }

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        if (Vibhakti.SASTHI in context.morphologicalCandidates) Vibhakti.SASTHI else Vibhakti.PANCHAMI,
        KarakaEvidence(number, text, "षष्ठी/पञ्चमी realizes the reference linked to a distance/proximity word (2.3.34)."),
    )
}
