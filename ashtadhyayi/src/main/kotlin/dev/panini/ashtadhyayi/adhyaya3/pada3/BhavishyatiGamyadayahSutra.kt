package dev.panini.ashtadhyayi.adhyaya3.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationalMeaning
import dev.panini.derivation.TermKind
import dev.panini.ganapatha.GanaPatha
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 3.3.3: भविष्यति गम्यादयः. */
object BhavishyatiGamyadayahSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.3.3", text = "भविष्यति गम्यादयः",
    hindiExplanation = "गम्यादि शब्द भविष्यत्काल के अर्थ में प्रयुक्त होते हैं।",
    type = SutraType.UTSARGA, chapter = 3, pada = 3, optional = false, kramaValue = 330003,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        DerivationalMeaning.BHAVISYAT !in context.effectiveContext.derivedMeanings &&
            context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(41, it.surface, it.lexicalUses) }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.copy(
            context = context.effectiveContext.copy(
                derivedMeanings = context.effectiveContext.derivedMeanings + DerivationalMeaning.BHAVISYAT,
            ),
        ),
        "3.3.3 records the future-time interpretation licensed for an eligible गम्यादि expression.",
    )
}
