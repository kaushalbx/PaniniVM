package dev.panini.sutra

import dev.panini.sutra.runtime.SutraArtha

sealed interface SutraArthaDefinition {
    fun toSutraArtha(): SutraArtha
}
