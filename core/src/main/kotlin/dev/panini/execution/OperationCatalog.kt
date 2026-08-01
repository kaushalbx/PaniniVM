package dev.panini.execution

import dev.panini.dhatupatha.Dhatu
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.core.Karaka

/** Read-only executable-operation view supplied by the authoritative Dhātupāṭha. */
class OperationCatalog(
    private val operationsForUpadesha: (String) -> List<DhatuOperation>,
    private val dhatus: () -> List<Dhatu> = { emptyList() },
) {
    data class Entry(val dhatu: Dhatu, val operation: DhatuOperation)

    val entries: List<Entry> get() = dhatus().flatMap { dhatu ->
        dhatu.operations.map { operation -> Entry(dhatu, operation) }
    }

    fun resolveAll(upadesha: String): List<DhatuOperation> =
        operationsForUpadesha(upadesha).distinct()

    fun resolve(name: String): DhatuOperation? {
        val candidates = resolveAll(name)
        return when {
            candidates.size == 1 -> candidates.single()
            candidates.map(DhatuOperation::action).distinct().size == 1 -> candidates.first()
            else -> null
        }
    }

    fun resolve(
        upadesha: String,
        providedKarakas: Set<Karaka>,
        karmanShape: ExpressionShape? = null,
    ): DhatuOperation? {
        val compatible = resolveAll(upadesha)
            .filter { operation -> operation.trigger.matches(GrammaticalFeatures()) }
            .filter { operation ->
            operation.signature.requirements.all { requirement ->
                requirement.karaka in providedKarakas &&
                    (requirement.karaka != Karaka.KARMAN || requirement.shape == null || requirement.shape == karmanShape)
            }
        }
        val strongest = compatible.maxOfOrNull { it.signature.specificity } ?: return null
        return compatible.filter { it.signature.specificity == strongest }.singleOrNull()
    }

    fun duplicateUpadeshas(): Map<String, List<Dhatu>> = dhatus()
        .groupBy(Dhatu::upadesha)
        .filterValues { candidates -> candidates.map(Dhatu::id).distinct().size > 1 }

    companion object {
        val default: OperationCatalog = OperationCatalog(
            operationsForUpadesha = { upadesha ->
                DhatuPatha.all.filter { it.upadesha == upadesha }.flatMap(Dhatu::operations)
            },
            dhatus = { DhatuPatha.all },
        )
    }
}
