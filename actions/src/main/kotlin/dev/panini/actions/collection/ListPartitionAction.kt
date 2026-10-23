package dev.panini.actions.collection

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionExpression
import dev.panini.execution.ExecutionResult
import dev.panini.execution.ExpressionShape
import dev.panini.execution.SanskritValue

/** Splits a collection into matching and nonmatching lists using a unary truth operation. */
object ListPartitionAction : DhatuAction("सूचीद्विविभागः", "सत्यासत्यफलाभ्यां सूचीसदस्यानां द्विविभागः") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val collectionExpression = context.bindings[Karaka.KARMAN]
            ?: return failure("Partition requires one list or group in KARMAN.")
        val collection = context.resolveValues(collectionExpression).singleOrNull()
            ?: return failure("Partition requires exactly one list or group.")
        val members = when (collection) {
            is SanskritValue.Suchi -> collection.items
            is SanskritValue.Gana -> collection.elements
            else -> return failure("Partition cannot use a scalar collection operand.")
        }
        val predicateExpression = context.bindings[Karaka.KARANA]
            ?: return failure("Partition requires a unary truth operation in KARANA.")
        val predicateName = context.resolve(predicateExpression).singleOrNull()?.trim()
            ?: return failure("The partition predicate must resolve to exactly one name.")
        val predicate = context.operationCatalog.resolve(
            predicateName, setOf(Karaka.KARMAN), ExpressionShape.LITERAL,
        ) ?: return ExecutionResult.Failure(
            ExecutionError.ACTION_FAILED,
            "Partition predicate '$predicateName' is not registered as a unary operation.",
        )

        val matching = mutableListOf<SanskritValue>()
        val rejected = mutableListOf<SanskritValue>()
        val trace = mutableListOf("Partitioning ${members.size} member(s) with '$predicateName'.")
        members.forEachIndexed { index, member ->
            val inner = ExecutionContext(
                bindings = mapOf(Karaka.KARMAN to ExecutionExpression.Reference("partition_member")),
                selectedOperation = predicate.name,
                variables = context.variables + ("partition_member" to member),
                metadata = context.metadata,
                stateStore = context.stateStore,
                externalDispatcher = context.externalDispatcher,
                sutraRegistry = context.sutraRegistry,
                currentGrantha = context.currentGrantha,
                operationCatalog = context.operationCatalog,
                linguisticServices = context.linguisticServices,
                sankhyaRenderer = context.sankhyaRenderer,
            )
            val truth = when (val result = predicate.action.execute(inner, predicate)) {
                is ExecutionResult.Success -> (result.typedValue
                    ?: SanskritValue.of(result.value, predicate.resultSamjnas)) as? SanskritValue.Satya
                    ?: return failure("Partition predicate must produce typed truth at member ${index + 1}.")
                is ExecutionResult.Failure -> return result.copy(
                    message = "Partition predicate failed at member ${index + 1}: ${result.message}",
                    trace = trace + result.trace,
                )
                else -> return ExecutionResult.Failure(
                    ExecutionError.ACTION_FAILED,
                    "Partition predicate returned an unsupported state at member ${index + 1}.",
                    trace + result.trace,
                )
            }
            (if (truth.boolean) matching else rejected) += member
            trace += "Member ${index + 1} joined the ${if (truth.boolean) "true" else "false"} partition."
        }

        val memberType = (collection as? SanskritValue.Suchi)?.memberType
        val value = SanskritValue.Rupa(
            "द्विविभाग",
            linkedMapOf(
                "सत्यसूची" to SanskritValue.Suchi(matching, memberType),
                "असत्यसूची" to SanskritValue.Suchi(rejected, memberType),
            ),
        )
        return ExecutionResult.Success(value.toDisplayText(), operation.name, trace, value)
    }

    private fun failure(message: String) = ExecutionResult.Failure(ExecutionError.INVALID_VALUE, message)
}
