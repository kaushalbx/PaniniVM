package dev.panini.derivation.sutra

import dev.panini.ashtadhyayi.runtime.AshtadhyayiCompiler
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.ArthavatSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.runtime.RuntimeSutra
import dev.panini.sutra.runtime.SutraId
import dev.panini.sutra.runtime.toBlueprint

/**
 * Keeps Aṣṭādhyāyī authoring independent of the shared runtime representation.
 *
 * A legacy derivation sūtra needs only its existing matches/apply implementation.
 * A sūtra may opt into native lowering by additionally exposing typed artha.
 */
object AshtadhyayiSutraLowerer {
    fun lower(
        sutra: Sutra<*, *>,
        localSutraIds: Set<SutraId> = emptySet(),
    ): RuntimeSutra<DerivationAvastha> = when (sutra) {
        is ArthavatSutra -> AshtadhyayiCompiler.compile(sutra.toBlueprint())
        is DerivationSutra -> DerivationSutraRuntimeAdapter.adapt(sutra, localSutraIds)
        else -> error(
            "Aṣṭādhyāyī sūtra ${sutra.number} must define typed artha " +
                "or implement the legacy DerivationSutra contract.",
        )
    }
}
