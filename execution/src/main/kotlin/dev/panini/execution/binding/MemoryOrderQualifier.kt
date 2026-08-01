package dev.panini.execution.binding

import dev.panini.execution.memory.KriyaMemory
import dev.panini.execution.memory.RememberedKriya
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.SankhyaPuranaPada
import dev.panini.vyakaranam.ast.SubantaPada

/** One grammatical ordering modifier shared by phala and kāraka memory queries. */
internal data class MemoryOrderQualifier(
    val pada: Pada? = null,
    val ordinalNumber: Int? = null,
    val previous: Boolean = false,
) {
    val isExplicit: Boolean get() = ordinalNumber != null || previous

    fun <T> select(values: List<T>): T? = when {
        ordinalNumber != null -> values.getOrNull(ordinalNumber - 1)
        previous -> values.getOrNull(values.lastIndex - 1)
        else -> values.lastOrNull()
    }

    fun select(memory: KriyaMemory, dhatuUpadesha: String): RememberedKriya? =
        if (ordinalNumber != null) memory.ordinalKriya(ordinalNumber, dhatuUpadesha)
        else memory.latestKriya(dhatuUpadesha, offset = if (previous) 1 else 0)
}

internal object MemoryOrderQualifierResolver {
    fun before(target: Pada, padas: List<Pada>): MemoryOrderQualifier {
        val pada = padas.getOrNull(padas.indexOf(target) - 1)
        val ordinalNumber = when (pada) {
            is SankhyaPuranaPada -> NumeralPadaBinder.evaluateStems(pada.stems).value.toInt()
            is SubantaPada -> ExpressionBuilder.ordinalNumber(pada.pratipadika.baseText())
            else -> null
        }
        val previous = (pada as? SubantaPada)?.pratipadika?.baseText() == "पूर्व"
        return MemoryOrderQualifier(pada, ordinalNumber, previous)
    }
}
