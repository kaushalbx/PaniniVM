package dev.panini.execution

import dev.panini.core.Karaka
import dev.panini.dhatupatha.Dhatu

class DhatuOperationRegistry private constructor(
    private val registrations: Map<String, List<DhatuOperation>>,
) {
    init {
        registrations.forEach { (dhatuId, operations) ->
            require(dhatuId.isNotBlank()) { "A registry entry requires a dhātu id." }
            require(operations.isNotEmpty()) { "Dhātu $dhatuId must register at least one operation." }
            require(operations.map { it.id }.distinct().size == operations.size) {
                "Dhātu $dhatuId registers duplicate operation ids."
            }
        }
    }

    fun operationsFor(dhatu: Dhatu): List<DhatuOperation> =
        registrations[dhatu.id].orEmpty()

    fun isExecutable(dhatu: Dhatu): Boolean = dhatu.id in registrations

    companion object {
        private val coordinatedNumbers = OperationSignature(
            requirements = listOf(
                KarakaRequirement(
                    karaka = Karaka.KARMAN,
                    minimumMembers = 2,
                    shape = ExpressionShape.COORDINATION,
                    memberSamjnas = setOf(ExecutionSamjna.SANKHYA),
                ),
            ),
        )

        val DEFAULT = DhatuOperationRegistry(
            BuiltInOperationRegistrations.all + mapOf(
                "07.0007" to listOf(
                    DhatuOperation(
                        id = "सङ्ख्यायोजनम्",
                        description = "सङ्ख्यानां योगः",
                        signature = coordinatedNumbers,
                        action = SanskritAdditionAction,
                        trigger = OperationTrigger(forbiddenUpasargas = setOf("वि")),
                        resultSamjnas = setOf(ExecutionSamjna.SANKHYA, ExecutionSamjna.SHABDA),
                    ),
                    DhatuOperation(
                        id = "सङ्ख्यावियोगः",
                        description = "सङ्ख्यानां वियोगः",
                        signature = coordinatedNumbers,
                        action = SanskritSubtractionAction,
                        trigger = OperationTrigger(requiredUpasargas = setOf("वि")),
                        resultSamjnas = setOf(ExecutionSamjna.SANKHYA, ExecutionSamjna.SHABDA),
                    ),
                ),
            ),
        )
    }
}
