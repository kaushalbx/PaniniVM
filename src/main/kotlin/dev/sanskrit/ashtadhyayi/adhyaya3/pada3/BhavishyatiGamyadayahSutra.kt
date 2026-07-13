package dev.sanskrit.ashtadhyayi.adhyaya3.pada3

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.ganapatha.GanaPatha
import dev.sanskrit.shiksha.SemanticFeature
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 3.3.3: भविष्यति गम्यादयः. */
object BhavishyatiGamyadayahSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.3.3", text = "भविष्यति गम्यादयः",
    hindiExplanation = "गम्यादि शब्द भविष्यत्काल के अर्थ में प्रयुक्त होते हैं।",
    type = SutraType.UTSARGA, chapter = 3, pada = 3, optional = false, kramaValue = 330003,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        SemanticFeature.BHAVISYAT !in context.semanticFeatures &&
            context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(41, it.surface, it.lexicalUses) }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.copy(semanticFeatures = context.semanticFeatures + SemanticFeature.BHAVISYAT),
        "3.3.3 records the future-time interpretation licensed for an eligible गम्यादि expression.",
    )
}
