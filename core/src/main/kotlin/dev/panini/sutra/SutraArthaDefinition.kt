package dev.panini.sutra

import dev.panini.sutra.runtime.SutraArtha

sealed interface SutraArthaDefinition {
    fun toSutraArtha(): SutraArtha
}

/** Capability of a sūtra that defines native, evaluator-free grammatical meaning. */
interface ArthavatSutra {
    val artha: SutraArthaDefinition
}
