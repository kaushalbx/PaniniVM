package dev.panini.derivation

import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna

/** An accent prescribed by grammar on an exact operative vowel occurrence. */
data class GrammaticalAccent(val varnaIndex: Int, val accent: AccentType, val sutra: String)

internal fun DerivationTerm.projectGrammaticalAccents(from: Int, count: Int,
    replacement: List<Varna>): List<GrammaticalAccent> {
    val vowel = replacement.indexOfFirst { it is Svara }
    return grammaticalAccents.mapNotNull {
        require(varnas.getOrNull(it.varnaIndex) is Svara) { "Stale grammatical accent on $id" }
        when {
            it.varnaIndex < from -> it
            it.varnaIndex >= from + count -> it.copy(varnaIndex = it.varnaIndex + replacement.size - count)
            vowel >= 0 -> it.copy(varnaIndex = from + vowel)
            else -> null // A coalescence caller explicitly transfers a consumed vowel's accent.
        }
    }
}
