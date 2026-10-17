package dev.panini.derivation

import dev.panini.shiksha.Ayogavaha
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana

/** The exact surviving occurrence of a prescribed ru, not a term-wide historical flag. */
data class RutvaOccurrence(val varnaIndex: Int, val sutra: String) {
    init { require(varnaIndex >= 0) }
}

fun DerivationTerm.hasRutvaAt(index: Int): Boolean =
    varnas.getOrNull(index) in rutvaCarrierVarnas && rutvaOccurrences.any { it.varnaIndex == index }

/** Projects a known positional operation. A ru consumed by u/ya substitution is no longer ru. */
internal fun DerivationTerm.projectRutvaOccurrences(from: Int, count: Int,
    replacement: List<Varna>, replacementSources: Map<Int, Int> = emptyMap()): List<RutvaOccurrence> {
    require(from >= 0 && count >= 0 && from + count <= varnas.size)
    require(replacementSources.all { (target, source) -> target in replacement.indices && source in 0 until count })
    require(replacementSources.values.distinct().size == replacementSources.size)
    return rutvaOccurrences.mapNotNull { occurrence ->
        require(varnas.getOrNull(occurrence.varnaIndex) in rutvaCarrierVarnas) {
            "Stale ru occurrence on $id at ${occurrence.varnaIndex}"
        }
        when {
            occurrence.varnaIndex < from -> occurrence
            occurrence.varnaIndex >= from + count ->
                occurrence.copy(varnaIndex = occurrence.varnaIndex + replacement.size - count)
            count == 1 && replacement.size == 1 && replacement.single() in rutvaCarrierVarnas ->
                occurrence.copy(varnaIndex = from)
            else -> replacementSources.entries.singleOrNull {
                it.value == occurrence.varnaIndex - from && replacement[it.key] in rutvaCarrierVarnas
            }?.let { occurrence.copy(varnaIndex = from + it.key) }
        }
    }
}

internal val rutvaCarrierVarnas: Set<Varna> = setOf(Vyanjana.RA, Ayogavaha.VISARGA,
    Ayogavaha.JIHVAMULIYA, Ayogavaha.UPADHMANIYA)
