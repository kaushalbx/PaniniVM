package dev.panini.derivation

import dev.panini.shiksha.*

/** Lexical identities, not tables of inflected outputs. */
enum class PersonalPronounIdentity(val upadesha: String, val originalVarnas: List<Varna>) {
    YUSMAD("युष्मद्", listOf(Vyanjana.YA, Svara.U, Vyanjana.SSA, Vyanjana.MA, Svara.A, Vyanjana.DA)),
    ASMAD("अस्मद्", listOf(Svara.A, Vyanjana.SA, Vyanjana.MA, Svara.A, Vyanjana.DA));
}

fun DerivationTerm.personalPronounIdentity(): PersonalPronounIdentity? =
    PersonalPronounIdentity.entries.firstOrNull { upadesha == it.upadesha || compoundHeadUpadesha == it.upadesha }

/** Original head footprint and current m delimit precisely the maparyanta
 * portion. Coalesced compound heads require explicit occurrence mapping. */
fun DerivationTerm.personalPronounMaparyantaRange(): IntRange? {
    val identity = personalPronounIdentity() ?: return null
    val original = upadeshaVarnas
    val start = original.size - identity.originalVarnas.size
    if (start < 0 || original.drop(start) != identity.originalVarnas) return null
    val m = varnas.indexOfLast { it == Vyanjana.MA }
    return if (m >= start) start..m else null
}

fun DerivationState.replacePersonalPronounMaparyanta(stemId: String, replacement: List<Varna>, sutra: String): DerivationState {
    val stem = terms.single { it.id == stemId }
    val range = requireNotNull(stem.personalPronounMaparyantaRange())
    val sourceVowel = stem.varnas.subList(range.first, range.last + 1).indexOfFirst { it is Svara }
    val replacementVowel = replacement.indexOfFirst { it is Svara }
    val annotations = if (sourceVowel >= 0 && replacementVowel >= 0) mapOf(replacementVowel to sourceVowel) else emptyMap()
    val changed = replaceTermVarnaRange(stemId, range.first, range.count(), replacement, annotations, sutra)
    val current = changed.terms.single { it.id == stemId }
    return changed.replaceTerm(stemId, current.copy(establishedBySutras = current.establishedBySutras + sutra))
}
