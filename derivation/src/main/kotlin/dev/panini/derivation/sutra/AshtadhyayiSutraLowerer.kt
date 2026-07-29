package dev.panini.derivation.sutra

import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.runtime.RuntimeSutra
import dev.panini.sutra.runtime.SutraId

/**
 * Keeps Aṣṭādhyāyī authoring independent of the shared runtime representation.
 *
 * A derivation sūtra needs only its existing Kotlin matches/apply implementation.
 */
object AshtadhyayiSutraLowerer {
    fun lower(
        sutra: Sutra<*, *>,
        localSutraIds: Set<SutraId> = emptySet(),
    ): RuntimeSutra<DerivationAvastha> =
        DerivationSutraRuntimeAdapter.adapt(
            sutra as? DerivationSutra
                ?: error(
                    "Executable Aṣṭādhyāyī sūtra ${sutra.number} must implement DerivationSutra.",
                ),
            localSutraIds,
        )
}
