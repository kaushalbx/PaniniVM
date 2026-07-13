package dev.sanskrit.ashtadhyayi.adhyaya8.pada2

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.shiksha.Vyanjana
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

object SasajusoRuhSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.2.66",
    text = "ससजुषो रुः",
    hindiExplanation = "स् के स्थान पर रुँ आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 2,
    optional = false,
    kramaValue = 820066,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.stage == DerivationStage.PADA_FORMED && context.terms.last().surface.endsWith(
            Vyanjana.SA.halanta
        )

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.copy(
            terms = context.terms.dropLast(1) + context.terms.last()
                .copy(surface = context.terms.last().surface.dropLast(2) + Vyanjana.RA.devanagari)
        ), "8.2.66 replaces final स् with र्."
    )
}
