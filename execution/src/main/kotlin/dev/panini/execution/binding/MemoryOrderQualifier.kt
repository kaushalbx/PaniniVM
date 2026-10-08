package dev.panini.execution.binding

import dev.panini.execution.memory.KriyaMemory
import dev.panini.execution.memory.RememberedKriya
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadikaIdentity
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.SubantaPada

/** One grammatical ordering modifier shared by phala and kāraka memory queries. */
internal data class MemoryOrderQualifier(
    val pada: Pada? = null,
    val ordinalNumber: Long? = null,
    val previous: Boolean = false,
    val unresolvedOrdinal: Boolean = false,
) {
    val isExplicit: Boolean get() = ordinalNumber != null || previous || unresolvedOrdinal
    private val invalidOrdinal: Boolean get() = unresolvedOrdinal || (ordinalNumber != null && ordinalNumber <= 0)

    fun agreesWith(target: Pada): Boolean {
        if (invalidOrdinal) return false
        if (!isExplicit) return true
        if (!NominalGenderAgreement.compatible(pada, target)) return false
        fun sup(pada: Pada?): String? = when (pada) {
            is SubantaPada -> pada.sup.text
            is dev.panini.vyakaranam.ast.SankhyaPada -> pada.sup.text
            is dev.panini.vyakaranam.ast.SankhyaPuranaPada -> pada.sup.text
            else -> null
        }
        val modifierCases = sup(pada)?.let(dev.panini.core.SupAffix::candidates).orEmpty()
        val targetCases = sup(target)?.let(dev.panini.core.SupAffix::candidates).orEmpty()
        return modifierCases.any { modifier -> targetCases.any { referent ->
            modifier.vibhakti == referent.vibhakti && modifier.vacana == referent.vacana
        } }
    }

    fun <T> select(values: List<T>): T? = when {
        invalidOrdinal -> null
        ordinalNumber != null -> if (ordinalNumber in 1L..values.size.toLong()) values[(ordinalNumber - 1).toInt()] else null
        previous -> values.getOrNull(values.lastIndex - 1)
        else -> values.lastOrNull()
    }

    fun select(memory: KriyaMemory, dhatuUpadesha: String): RememberedKriya? =
        if (invalidOrdinal) null
        else if (ordinalNumber != null) memory.ordinalKriya(ordinalNumber, dhatuUpadesha)
        else memory.latestKriya(dhatuUpadesha, offset = if (previous) 1 else 0)
}

internal object MemoryOrderQualifierResolver {
    fun before(target: Pada, padas: List<Pada>): MemoryOrderQualifier {
        val pada = padas.getOrNull(padas.indexOfFirst { it === target } - 1)
        return from(pada)
    }

    fun from(pada: Pada?): MemoryOrderQualifier {
        val ordinalNumber = pada?.let {
            NumeralPadaBinder.extractOrdinalValue(it) ?: dev.panini.execution.PuranaPratyayaResolver.ordinalValue(it)
        }
        val previous = ((pada as? SubantaPada)?.pratipadika as? MulaPratipadika)
            ?.lexicalIdentity == MulaPratipadikaIdentity.PURVA
        val nominal = (pada as? SubantaPada)?.pratipadika as? MulaPratipadika
        val derivedOrdinal = nominal?.lexicalIdentity?.ordinalValue != null &&
            nominal.lexicalOrdinalValue == null
        return MemoryOrderQualifier(
            pada, ordinalNumber, previous,
            unresolvedOrdinal = derivedOrdinal ||
                (pada is dev.panini.vyakaranam.ast.SankhyaPuranaPada && ordinalNumber == null),
        )
    }
}
