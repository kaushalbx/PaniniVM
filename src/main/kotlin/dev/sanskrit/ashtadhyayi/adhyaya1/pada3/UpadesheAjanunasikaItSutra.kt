package dev.sanskrit.ashtadhyayi.adhyaya1.pada3

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.ItMarker
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 1.3.2 assigns it-status to a nasalized vowel in upadeśa. */
object UpadesheAjanunasikaItSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.3.2",
    text = "उपदेशेऽजनुनासिक इत्",
    hindiExplanation = "उपदेश में अनुनासिक अच् इत्संज्ञक होता है।",
    type = SutraType.SAMJNA,
    chapter = 1,
    pada = 3,
    optional = false,
    kramaValue = 130002,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.stage == DerivationStage.PRATYAYA_SELECTED && context.terms.any {
            it.surface.endsWith(
                "ँ"
            ) && ItMarker.U !in it.itMarkers
        }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.copy(terms = context.terms.map { if (it.surface.endsWith("ँ")) it.copy(itMarkers = it.itMarkers + ItMarker.U) else it }),
        "1.3.2 assigns it-status to the nasalized उ of सुँ.",
    )
}
