package dev.panini.execution

import dev.panini.shiksha.Samjna

/** Stable textual encoding shared by execution persistence formats. */
internal object PersistedSamjnaCodec {
    private const val RUDHI_PREFIX = "RUDHI:"

    fun encode(samjna: Samjna): String = when (samjna) {
        is Samjna.Rudhi -> "$RUDHI_PREFIX${samjna.word}"
        is Enum<*> -> samjna.name
    }

    fun decode(value: String): Samjna = if (value.startsWith(RUDHI_PREFIX)) {
        Samjna.Rudhi(value.removePrefix(RUDHI_PREFIX))
    } else {
        Samjna.valueOf(value)
    }
}
