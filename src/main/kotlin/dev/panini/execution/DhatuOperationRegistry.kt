package dev.panini.execution

import dev.panini.dhatupatha.Dhatu
import dev.panini.dhatupatha.DhatuPatha

class DhatuOperationRegistry internal constructor(
    private val registrations: Map<String, List<DhatuOperation>>,
) {
    init {
        registrations.forEach { (dhatuId, operations) ->
            require(dhatuId.isNotBlank()) { "A registry entry requires a dhātu id." }
            require(DhatuPatha.find(dhatuId) != null) { "Unknown dhātu id: $dhatuId." }
            require(operations.isNotEmpty()) { "Dhātu $dhatuId must register at least one operation." }
            require(operations.map { it.id }.distinct().size == operations.size) {
                "Dhātu $dhatuId registers duplicate operation ids."
            }
            require(operations.groupBy { it.signature to it.trigger }.none { it.value.size > 1 }) {
                "Dhātu $dhatuId has indistinguishable operations."
            }
        }
    }

    fun operationsFor(dhatu: Dhatu): List<DhatuOperation> =
        registrations[dhatu.id].orEmpty()

    fun isExecutable(dhatu: Dhatu): Boolean = dhatu.id in registrations

    companion object {
        val DEFAULT = DhatuOperationRegistry(
            registrations(
                NumericOperationRegistrations.all,
                LinguisticOperationRegistrations.all,
                StateOperationRegistrations.all,
                ExternalOperationRegistrations.all,
            ),
        )
    }
}
