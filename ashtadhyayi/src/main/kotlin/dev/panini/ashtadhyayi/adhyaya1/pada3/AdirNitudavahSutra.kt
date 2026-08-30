package dev.panini.ashtadhyayi.adhyaya1.pada3

import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.ItDesignation
import dev.panini.derivation.TermKind
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
    stage = dev.panini.sutra.SutraStage.IT_PROCESSING,
), DerivationSutra {
    fun hasSamjnaTarget(state: DerivationState): Boolean =
        (state.stage == DerivationStage.INITIAL || state.terms.any { it.itProcessingPending }) &&
            state.terms.any { term ->
                term.kind == TermKind.DHATU && initialMarker(term.surface) != null &&
                    term.itDesignations.none { it.start == 0 }
        }

    fun assignSamjna(state: DerivationState): DerivationChange {
        val newTerms = state.terms.map { term ->
            if (term.kind == TermKind.DHATU) {
                val marker = initialMarker(term.surface) ?: return@map term
                term.copy(
                    itMarkers = term.itMarkers + marker,
                    itDesignations = term.itDesignations + ItDesignation(0, 2, marker = marker, sutra = sutra),
                )
            } else term
        }
        return DerivationChange(
            state = state.copy(terms = newTerms),
            explanation = "1.3.5: Assigned it-status to initial ñi/ṭu/ḍu."
        )
    }

    override fun matches(context: DerivationState): Boolean = hasSamjnaTarget(context)

    override fun apply(context: DerivationState): DerivationChange = assignSamjna(context)

    private fun initialMarker(surface: String): ItMarker? = when {
        surface.startsWith("ञि") -> ItMarker.NYIT
        surface.startsWith("टु") -> ItMarker.T
        surface.startsWith("डु") -> ItMarker.DIT
        else -> null
    }
}
