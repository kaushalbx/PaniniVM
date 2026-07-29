package dev.panini.ashtadhyayi.adhyaya1.pada3

import dev.panini.ashtadhyayi.runtime.ContextualSamjnaSutra
import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.ContextualSamjnaAssignmentArtha
import dev.panini.sutra.SamjnaAssignmentTarget
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 1.3.5: ādir ñitūḍavaḥ.
 * Initial ñi, ṭu, and ḍu syllables in an upadeśa (usually dhātus) are it-markers.
 */
object AdirNitudavahSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.3.5",
    text = "आदिर्ञिटूडवः",
    hindiExplanation = "उपदेश के आदि में स्थित ञि, टु और डु की इत् संज्ञा होती है।",
    type = SutraType.SAMJNA,
    chapter = 1,
    pada = 3,
    optional = false,
    kramaValue = 130005,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.DHATU,
), DerivationSutra, ContextualSamjnaSutra {
    override val artha = ContextualSamjnaAssignmentArtha(
        target = SamjnaAssignmentTarget.DHATU_UPADESHA_INITIAL_NI_TU_DU,
        samjna = Samjna.IT,
    )

    override fun hasSamjnaTarget(state: DerivationState): Boolean =
        state.stage == DerivationStage.INITIAL && state.terms.any { term ->
            term.kind == TermKind.DHATU && (
                term.surface.startsWith("ञि") ||
                term.surface.startsWith("टु") ||
                term.surface.startsWith("डु")
            )
        }

    override fun assignSamjna(state: DerivationState): DerivationChange {
        val newTerms = state.terms.map { term ->
            if (term.kind == TermKind.DHATU) {
                when {
                    term.surface.startsWith("ञि") -> term.copy(itMarkers = term.itMarkers + ItMarker.KIT) // Using KIT as proxy
                    term.surface.startsWith("टु") -> term.copy(itMarkers = term.itMarkers + ItMarker.T)
                    term.surface.startsWith("डु") -> term.copy(itMarkers = term.itMarkers + ItMarker.KIT)
                    else -> term
                }
            } else term
        }
        return DerivationChange(
            state = state.copy(terms = newTerms),
            explanation = "1.3.5: Assigned it-status to initial ñi/ṭu/ḍu."
        )
    }

    override fun matches(context: DerivationState): Boolean = hasSamjnaTarget(context)

    override fun apply(context: DerivationState): DerivationChange = assignSamjna(context)
}
