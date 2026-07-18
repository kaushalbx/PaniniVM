package dev.panini.ashtadhyayi.adhyaya8.pada2

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

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
        (context.stage == DerivationStage.IT_PROCESSED || context.stage == DerivationStage.PADA_FORMED || context.stage == DerivationStage.FINAL) &&
        context.terms.last().surface.endsWith(Vyanjana.SA.halanta)

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.copy(
            terms = context.terms.dropLast(1) + context.terms.last()
                .copy(surface = context.terms.last().surface.dropLast(2) + Vyanjana.RA.devanagari)
        ), "8.2.66 replaces final स् with र्."
    )
}
