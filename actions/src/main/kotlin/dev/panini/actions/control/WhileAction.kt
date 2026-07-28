package dev.panini.actions.control

import dev.panini.core.Karaka
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionExpression
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue

/** While conditional loop action. */
object WhileAction : DhatuAction("यावदवृत्तिः", "यावत्-तावत् लूप-क्रिया (व्हाइल्-लूप)") {
    private fun normalize(name: String): String {
        return name.removeSuffix("म्").trimEnd('्', 'ँ', 'ः')
    }

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val condExpr = context.bindings[Karaka.KARMAN]
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "While loop requires a condition predicate action in KARMAN."
            )
        val bodyExpr = context.bindings[Karaka.KARANA]
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "While loop requires a body action in KARANA."
            )
        val stateExpr = context.bindings[Karaka.SAMPRADANA]
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "While loop requires an initial state value in SAMPRADANA."
            )

        val condName = context.resolve(condExpr).firstOrNull()?.trim()
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Condition predicate action cannot be resolved to a name."
            )
        val bodyName = context.resolve(bodyExpr).firstOrNull()?.trim()
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Body action cannot be resolved to a name."
            )

        // Find condition and body operations in registry
        val normCond = normalize(condName)
        val condOp = DhatuPatha.all.flatMap { it.operations }
            .firstOrNull { 
                it.name == condName || it.action.name == condName ||
                normalize(it.name) == normCond ||
                normalize(it.action.name) == normCond
            }
            ?: DhatuPatha.all.firstOrNull {
                it.upadesha == condName || it.sourceSurface == condName || it.surfaceAliases.contains(condName) ||
                normalize(it.upadesha) == normCond ||
                normalize(it.sourceSurface) == normCond ||
                it.surfaceAliases.any { alias -> normalize(alias) == normCond }
            }?.operations?.firstOrNull()
            ?: return ExecutionResult.Failure(
                ExecutionError.ACTION_FAILED,
                "Condition predicate '$condName' not found in registry."
            )

        val normBody = normalize(bodyName)
        val bodyOp = DhatuPatha.all.flatMap { it.operations }
            .firstOrNull { 
                it.name == bodyName || it.action.name == bodyName ||
                normalize(it.name) == normBody ||
                normalize(it.action.name) == normBody
            }
            ?: DhatuPatha.all.firstOrNull {
                it.upadesha == bodyName || it.sourceSurface == bodyName || it.surfaceAliases.contains(bodyName) ||
                normalize(it.upadesha) == normBody ||
                normalize(it.sourceSurface) == normBody ||
                it.surfaceAliases.any { alias -> normalize(alias) == normBody }
            }?.operations?.firstOrNull()
            ?: return ExecutionResult.Failure(
                ExecutionError.ACTION_FAILED,
                "Body action '$bodyName' not found in registry."
            )

        var state = context.resolveValues(stateExpr).firstOrNull()
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Initial state value not found."
            )

        val trace = mutableListOf("Starting while loop with initial state: ${state.toDisplayText()}.")
        var iterations = 0
        val maxIterations = 100 // Safety break

        while (iterations < maxIterations) {
            // Evaluate condition predicate with current state in KARMAN
            val condVariables = context.variables.toMutableMap()
            condVariables["loop_element"] = state
            val condBindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Reference("loop_element")
            )
            val condContext = ExecutionContext(
                bindings = condBindings,
                selectedOperation = condOp.name,
                variables = condVariables,
                metadata = context.metadata,
                stateStore = context.stateStore,
                externalDispatcher = context.externalDispatcher
            )

            val condResult = condOp.action.execute(condContext, condOp)
            val isTrue = when (condResult) {
                is ExecutionResult.Success -> {
                    val v = condResult.typedValue
                    v is SanskritValue.Satya && v.boolean || condResult.value == "सत्यम्"
                }
                is ExecutionResult.Failure -> {
                    return ExecutionResult.Failure(
                        condResult.error,
                        "While loop condition failed at iteration ${iterations + 1}: ${condResult.message}",
                        trace + condResult.trace,
                    )
                }
                else -> {
                    return ExecutionResult.Failure(
                        ExecutionError.ACTION_FAILED,
                        "While loop condition returned an invalid state.",
                        trace + condResult.trace,
                    )
                }
            }

            if (!isTrue) {
                trace += "Condition evaluated to false. Exited loop."
                break
            }

            // Execute loop body with current state in KARMAN
            val bodyVariables = context.variables.toMutableMap()
            bodyVariables["loop_element"] = state
            val bodyBindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Reference("loop_element")
            )
            val bodyContext = ExecutionContext(
                bindings = bodyBindings,
                selectedOperation = bodyOp.name,
                variables = bodyVariables,
                metadata = context.metadata,
                stateStore = context.stateStore,
                externalDispatcher = context.externalDispatcher
            )

            when (val bodyResult = bodyOp.action.execute(bodyContext, bodyOp)) {
                is ExecutionResult.Success -> {
                    state = bodyResult.typedValue ?: SanskritValue.of(bodyResult.value, bodyOp.resultSamjnas)
                    iterations++
                    trace += "Iteration $iterations state updated: ${state.toDisplayText()}"
                }
                is ExecutionResult.Failure -> {
                    return ExecutionResult.Failure(
                        bodyResult.error,
                        "While loop body failed at iteration ${iterations + 1}: ${bodyResult.message}",
                        trace + bodyResult.trace
                    )
                }
                else -> {
                    return ExecutionResult.Failure(
                        ExecutionError.ACTION_FAILED,
                        "While loop body returned invalid state.",
                        trace
                    )
                }
            }
        }

        if (iterations >= maxIterations) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "While loop safety limit exceeded ($maxIterations iterations)."
            )
        }

        return ExecutionResult.Success(state.toDisplayText(), operation.name, trace, state)
    }
}
