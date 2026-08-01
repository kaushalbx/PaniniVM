package dev.panini.actions.collection

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionExpression
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue
import dev.panini.execution.DevanagariDigits
import dev.panini.execution.renderSankhyaResult

/** Fold a list using a binary accumulator operation and an initial state. */
object ListFoldAction : DhatuAction("सूचीसङ्क्षेपः", "सूच्याः सङ्क्षेपः (फोल्ड्-क्रिया)") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val listExpr = context.bindings[Karaka.KARMAN]
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "List fold requires a list in KARMAN."
            )
        val targetExpr = context.bindings[Karaka.KARANA]
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "List fold requires a binary target operation name in KARANA."
            )
        val initialExpr = context.bindings[Karaka.SAMPRADANA]
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "List fold requires an initial/seed value in SAMPRADANA."
            )

        val list = context.resolveValues(listExpr)
        val listItems = if (list.size == 1 && list.first() is SanskritValue.Suchi) {
            (list.first() as SanskritValue.Suchi).items
        } else {
            list
        }

        val targetName = context.resolve(targetExpr).firstOrNull()?.trim()
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Target operation cannot be resolved to a name."
            )

        val targetOp = context.operationCatalog.resolve(
            targetName,
            setOf(Karaka.KARMAN),
            dev.panini.execution.ExpressionShape.COORDINATION,
        )
            ?: return ExecutionResult.Failure(
                ExecutionError.ACTION_FAILED,
                "Target operation '$targetName' not found in registry."
            )

        val initialValue = context.resolveValues(initialExpr).firstOrNull()
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Initial fold value not found."
            )

        var accumulator = initialValue
        val trace = mutableListOf("Folding list of size ${listItems.size} with operation '$targetName'.")

        listItems.forEachIndexed { i, element ->
            val innerVariables = context.variables.toMutableMap()
            val word = context.renderSankhyaResult((i + 1).toLong()) ?: DevanagariDigits.render(i + 1)
            innerVariables["loop_index"] = SanskritValue.Sankhya((i + 1).toLong(), word)
            innerVariables["loop_element"] = element
            innerVariables["loop_result"] = accumulator

            // Bind accumulator and current element to the target operation.
            val innerBindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    listOf(
                        ExecutionExpression.Reference("loop_result"),
                        ExecutionExpression.Reference("loop_element")
                    )
                )
            )

            val innerContext = ExecutionContext(
                bindings = innerBindings,
                selectedOperation = targetOp.name,
                variables = innerVariables,
                metadata = context.metadata,
                stateStore = context.stateStore,
                externalDispatcher = context.externalDispatcher,
                sutraRegistry = context.sutraRegistry,
                currentGrantha = context.currentGrantha,
                operationCatalog = context.operationCatalog,
                linguisticServices = context.linguisticServices,
                sankhyaRenderer = context.sankhyaRenderer,
            )

            when (val result = targetOp.action.execute(innerContext, targetOp)) {
                is ExecutionResult.Success -> {
                    accumulator = result.typedValue ?: SanskritValue.of(result.value, targetOp.resultSamjnas)
                    trace += "Step ${i + 1} accumulator: ${accumulator.toDisplayText()}"
                }
                is ExecutionResult.Failure -> {
                    return ExecutionResult.Failure(
                        result.error,
                        "Fold failed at step ${i + 1}: ${result.message}",
                        trace + result.trace
                    )
                }
                else -> {
                    return ExecutionResult.Failure(
                        ExecutionError.ACTION_FAILED,
                        "Fold returned invalid state at step ${i + 1}.",
                        trace
                    )
                }
            }
        }

        return ExecutionResult.Success(accumulator.toDisplayText(), operation.name, trace, accumulator)
    }
}
