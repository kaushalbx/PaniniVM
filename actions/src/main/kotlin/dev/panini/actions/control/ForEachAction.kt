package dev.panini.actions.control

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

/** ForEach iteration loop action. */
object ForEachAction : DhatuAction("प्रत्येकवृत्तिः", "सूची-प्रत्येक-भ्रमण-क्रिया (फॉर्-ईच् लूप)") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val listExpr = context.bindings[Karaka.KARMAN]
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "ForEach loop requires a list in KARMAN."
            )
        val bodyExpr = context.bindings[Karaka.KARANA]
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "ForEach loop requires a body action in KARANA."
            )
        val stateExpr = context.bindings[Karaka.SAMPRADANA]

        val list = context.resolveValues(listExpr)
        val listItems = if (list.size == 1 && list.first() is SanskritValue.Suchi) {
            (list.first() as SanskritValue.Suchi).items
        } else {
            list
        }

        val bodyName = context.resolve(bodyExpr).firstOrNull()?.trim()
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Body action cannot be resolved to a name."
            )

        val bodyOp = context.operationCatalog.resolve(
            bodyName,
            setOf(Karaka.KARMAN),
            dev.panini.execution.ExpressionShape.COORDINATION,
        )
            ?: return ExecutionResult.Failure(
                ExecutionError.ACTION_FAILED,
                "Body action '$bodyName' not found in registry."
            )

        var accumulator = stateExpr?.let { context.resolveValues(it).firstOrNull() } ?: SanskritValue.Suchi(emptyList())
        val trace = mutableListOf("Starting ForEach loop over ${listItems.size} items with body action '$bodyName'.")

        listItems.forEachIndexed { i, element ->
            val innerVariables = context.variables.toMutableMap()
            val word = context.renderSankhyaResult((i + 1).toLong()) ?: DevanagariDigits.render(i + 1)
            innerVariables["loop_index"] = SanskritValue.Sankhya((i + 1).toLong(), word)
            innerVariables["loop_element"] = element
            innerVariables["loop_result"] = accumulator

            // Bind required parameters of target action dynamically
            val targetRequiredKarakas = bodyOp.signature.requirements.map { it.karaka }
            val innerBindings = mutableMapOf<Karaka, ExecutionExpression>()

            // Copy non-conflicting bindings
            context.bindings.forEach { (k, v) ->
                if (k != Karaka.KARMAN && k != Karaka.KARANA && k != Karaka.SAMPRADANA) {
                    innerBindings[k] = v
                }
            }

            if (targetRequiredKarakas.size == 1 && targetRequiredKarakas.first() == Karaka.KARMAN) {
                innerBindings[Karaka.KARMAN] = ExecutionExpression.Coordination(
                    listOf(
                        ExecutionExpression.Reference("loop_result"),
                        ExecutionExpression.Reference("loop_element")
                    )
                )
            } else {
                // Map target requirements to loop variables
                targetRequiredKarakas.forEach { required ->
                    if (required !in innerBindings) {
                        if (required == Karaka.KARMAN) {
                            innerBindings[required] = ExecutionExpression.Reference("loop_element")
                        } else {
                            innerBindings[required] = ExecutionExpression.Reference("loop_result")
                        }
                    }
                }

                // Fallback bindings if not mapped
                if (innerBindings.isEmpty() || innerBindings.size < targetRequiredKarakas.size) {
                    innerBindings[Karaka.KARMAN] = ExecutionExpression.Reference("loop_element")
                    if (Karaka.SAMPRADANA !in innerBindings) {
                        innerBindings[Karaka.SAMPRADANA] = ExecutionExpression.Reference("loop_result")
                    }
                }
            }

            val innerContext = ExecutionContext(
                bindings = innerBindings,
                selectedOperation = bodyOp.name,
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

            when (val result = bodyOp.action.execute(innerContext, bodyOp)) {
                is ExecutionResult.Success -> {
                    accumulator = result.typedValue ?: SanskritValue.of(result.value, bodyOp.resultSamjnas)
                    trace += "Iteration ${i + 1} ('${element.toDisplayText()}') result: ${accumulator.toDisplayText()}"
                }
                is ExecutionResult.Failure -> {
                    return ExecutionResult.Failure(
                        result.error,
                        "ForEach loop failed at iteration ${i + 1}: ${result.message}",
                        trace + result.trace
                    )
                }
                else -> {
                    return ExecutionResult.Failure(
                        ExecutionError.ACTION_FAILED,
                        "ForEach loop returned invalid state at iteration ${i + 1}.",
                        trace
                    )
                }
            }
        }

        return ExecutionResult.Success(accumulator.toDisplayText(), operation.name, trace, accumulator)
    }
}
