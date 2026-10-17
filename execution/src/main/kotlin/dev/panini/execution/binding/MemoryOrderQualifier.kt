package dev.panini.execution.binding

import dev.panini.execution.memory.KriyaMemory
import dev.panini.execution.memory.RememberedKriya
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadikaIdentity
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.isRecognizedStriAffix

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
        val modifier = pada ?: return false
        return NominalModifierAttachment.analyze(modifier, listOf(target)).unique != null
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

    fun select(memory: KriyaMemory, actionIdentity: dev.panini.core.ActionIdentity): RememberedKriya? =
        select(memory.matchingActions(actionIdentity))
}

internal object MemoryOrderQualifierResolver {
    /** Free order is safe only when the remaining phrase contains ordering modifiers alone. */
    fun forHead(target: Pada, action: Pada, padas: List<Pada>, singleHead: Boolean): MemoryOrderQualifier {
        val targetSup = (target as? dev.panini.vyakaranam.ast.SupBearingPada)?.sup
        fun separateDestination(pada: Pada?): Boolean {
            val slots = (pada as? dev.panini.vyakaranam.ast.SupBearingPada)?.sup?.affixCandidates.orEmpty()
            return targetSup != null && !targetSup.supports(dev.panini.core.Vibhakti.CHATURTHI) &&
                slots.isNotEmpty() && slots.all { it.vibhakti == dev.panini.core.Vibhakti.CHATURTHI }
        }
        val preceding = before(target, padas)
        val adjacent = if (separateDestination(preceding.pada)) MemoryOrderQualifier() else preceding
        val candidates = padas.filter {
            it !== target && it !== action && it !is dev.panini.vyakaranam.ast.TingantaPada
        }.filterNot(::separateDestination).map(::from)
        // A separate case-marked argument (e.g. पूर्वाय as destination) must not
        // compete with an ordinal merely because its stem is पूर्व.
        return if (singleHead && candidates.isNotEmpty() && candidates.all { it.isExplicit }) {
            candidates.singleOrNull() ?: MemoryOrderQualifier(unresolvedOrdinal = true)
        } else adjacent
    }

    fun before(target: Pada, padas: List<Pada>): MemoryOrderQualifier {
        val pada = padas.getOrNull(padas.indexOfFirst { it === target } - 1)
        return from(pada)
    }

    fun from(pada: Pada?): MemoryOrderQualifier {
        val ordinalNumber = pada?.let {
            runCatching { dev.panini.execution.PuranaPratyayaResolver.semanticValue(it)?.value }.getOrNull()
        }
        val nominal = (pada as? SubantaPada)?.pratipadika as? MulaPratipadika
        val purva = nominal?.lexicalIdentity == MulaPratipadikaIdentity.PURVA
        val derivedPurva = purva && nominal.vikaras.any {
            !it.isRecognizedStriAffix
        }
        val previous = purva && !derivedPurva
        val derivedOrdinal = nominal?.lexicalIdentity?.ordinalValue != null &&
            nominal.lexicalOrdinalValue == null
        return MemoryOrderQualifier(
            pada, ordinalNumber, previous,
            unresolvedOrdinal = derivedOrdinal || derivedPurva ||
                (pada is dev.panini.vyakaranam.ast.SankhyaPuranaPada && ordinalNumber == null),
        )
    }
}
