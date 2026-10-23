package dev.panini.actions.collection

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionExpression
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue
import dev.panini.execution.semanticallyEquals

/** Groups collection members by the typed result of a unary key operation. */
object ListGroupByAction : DhatuAction("सूचीवर्गीकरणम्", "कुञ्जीफलानुसारं सूचीसदस्यानां वर्गीकरणम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val collectionExpression = context.bindings[Karaka.KARMAN]
            ?: return failure("Group-by requires one list or group in KARMAN.")
        val collection = context.resolveValues(collectionExpression).singleOrNull()
            ?: return failure("Group-by requires exactly one list or group.")
        val members = when (collection) {
            is SanskritValue.Suchi -> collection.items
            is SanskritValue.Gana -> collection.elements
            else -> return failure("Group-by cannot use a scalar collection operand.")
        }

        val keyExpression = context.bindings[Karaka.KARANA]
            ?: return failure("Group-by requires a unary key operation in KARANA.")
        val keyName = context.resolve(keyExpression).singleOrNull()?.trim()
            ?: return failure("The group-by key operation must resolve to exactly one name.")
        val keyOperation = context.operationCatalog.resolve(
            keyName,
            setOf(Karaka.KARMAN),
            dev.panini.execution.ExpressionShape.LITERAL,
        ) ?: return ExecutionResult.Failure(
            ExecutionError.ACTION_FAILED,
            "Group-by key operation '$keyName' is not registered as a unary operation.",
        )

        data class Bucket(val key: SanskritValue, val members: MutableList<SanskritValue>)
        val buckets = mutableListOf<Bucket>()
        val trace = mutableListOf("Grouping ${members.size} member(s) by '$keyName'.")
        members.forEachIndexed { index, member ->
            val innerContext = ExecutionContext(
                bindings = mapOf(Karaka.KARMAN to ExecutionExpression.Reference("group_member")),
                selectedOperation = keyOperation.name,
                variables = context.variables + ("group_member" to member),
                metadata = context.metadata,
                stateStore = context.stateStore,
                externalDispatcher = context.externalDispatcher,
                sutraRegistry = context.sutraRegistry,
                currentGrantha = context.currentGrantha,
                operationCatalog = context.operationCatalog,
                linguisticServices = context.linguisticServices,
                sankhyaRenderer = context.sankhyaRenderer,
            )
            val key = when (val result = keyOperation.action.execute(innerContext, keyOperation)) {
                is ExecutionResult.Success -> result.typedValue
                    ?: SanskritValue.of(result.value, keyOperation.resultSamjnas)
                is ExecutionResult.Failure -> return result.copy(
                    message = "Group-by key failed at member ${index + 1}: ${result.message}",
                    trace = trace + result.trace,
                )
                else -> return ExecutionResult.Failure(
                    ExecutionError.ACTION_FAILED,
                    "Group-by key returned an unsupported state at member ${index + 1}.",
                    trace + result.trace,
                )
            }
            val bucket = buckets.firstOrNull { it.key.semanticallyEquals(key) }
                ?: Bucket(key, mutableListOf()).also(buckets::add)
            bucket.members += member
            trace += "Member ${index + 1} joined key '${key.toDisplayText()}'."
        }

        val grouped = SanskritValue.Suchi(buckets.map { bucket ->
            SanskritValue.Rupa(
                schema = "वर्ग",
                fields = linkedMapOf(
                    "कुञ्जी" to bucket.key,
                    "सदस्यसूची" to SanskritValue.Suchi(
                        bucket.members.toList(),
                        (collection as? SanskritValue.Suchi)?.memberType,
                    ),
                ),
            )
        })
        return ExecutionResult.Success(grouped.toDisplayText(), operation.name, trace, grouped)
    }

    private fun failure(message: String) = ExecutionResult.Failure(ExecutionError.INVALID_VALUE, message)
}
