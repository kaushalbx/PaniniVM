package dev.panini.ashtadhyayi.adhyaya1.pada3

import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

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
    fun hasSamjnaTarget(state: DerivationState): Boolean =
        state.stage == DerivationStage.PRATYAYA_SELECTED && state.terms.any {
            it.surface.endsWith(
                "ँ"
            ) && ItMarker.U !in it.itMarkers
        }

    fun assignSamjna(state: DerivationState): DerivationChange = DerivationChange(
        state.copy(terms = state.terms.map { if (it.surface.endsWith("ँ")) it.copy(itMarkers = it.itMarkers + ItMarker.U) else it }),
        "1.3.2 assigns it-status to the nasalized उ of सुँ.",
    )

    override fun matches(context: DerivationState): Boolean = hasSamjnaTarget(context)

    override fun apply(context: DerivationState): DerivationChange = assignSamjna(context)
}
