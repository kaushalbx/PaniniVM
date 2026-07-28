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
import dev.panini.execution.DevanagariDigits
import dev.panini.execution.renderSankhyaResult

object SanskritLoopAction : DhatuAction("अनुवृत्तिः", "क्रियायाः पुनः पुनः अनुष्ठानम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        // 1. Resolve loop count key and expression
        val countKey = if (context.bindings.containsKey(Karaka.KARMAN)) Karaka.KARMAN else Karaka.KARTR
        val countExpression = context.bindings[countKey]
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Loop execution requires a loop count in KARMAN or KARTR."
            )

        val countValues = context.resolveValues(countExpression)
        val countSankhya = countValues.filterIsInstance<SanskritValue.Sankhya>().firstOrNull()
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Loop execution requires a valid saṅkhyā count."
            )

        if (countSankhya.value !in 0L..100_000L) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Loop count ${countSankhya.value} is outside the supported range 0..100000.",
            )
        }
        val loopCount = countSankhya.value.toInt()
        if (loopCount < 0) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Loop count cannot be negative: $loopCount"
            )
        }

        // 2. Resolve target operation key and expression
        val targetKey = if (context.bindings.containsKey(Karaka.KARANA)) Karaka.KARANA else Karaka.ADHIKARANA
        val targetExpression = context.bindings[targetKey]
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Loop execution requires a target action or verbal root in KARANA or ADHIKARANA."
            )

        val targetName = context.resolve(targetExpression).firstOrNull()?.trim()
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Target operation cannot be resolved to a name."
            )


        val resolvedDhatu = DhatuPatha.all.firstOrNull {
            (it.upadesha == targetName || it.sourceSurface == targetName || it.surfaceAliases.contains(targetName)) &&
            it.operations.isNotEmpty()
        }

        // Find the target operation in DhatuPatha registry
        val targetOp = DhatuPatha.all.flatMap { it.operations }
            .firstOrNull { it.name == targetName || it.action.name == targetName }
            ?: resolvedDhatu?.operations?.firstOrNull()
            ?: return ExecutionResult.Failure(
                ExecutionError.ACTION_FAILED,
                "Verbal root or operation '$targetName' not found in registry."
            )

        // 3. Prepare inner bindings (exclude loop count and target action keys, map dynamically if needed)
        val targetRequiredKarakas = targetOp.signature.requirements.map { it.karaka }
        val innerBindings = mutableMapOf<Karaka, ExecutionExpression>()

        // Copy non-conflicting bindings directly
        context.bindings.forEach { (k, v) ->
            if (k != countKey && k != targetKey && k != Karaka.SAMPRADANA && k != Karaka.APADANA) {
                innerBindings[k] = v
            }
        }

        // If target requires a Karaka that we consumed, map the remaining caller binding to it
        targetRequiredKarakas.forEach { required ->
            if (required !in innerBindings) {
                val sourceKey = context.bindings.keys.firstOrNull {
                    it != countKey && it != targetKey && it != Karaka.KARTR && it !in targetRequiredKarakas && it != Karaka.SAMPRADANA && it != Karaka.APADANA
                }
                if (sourceKey != null) {
                    innerBindings[required] = context.bindings.getValue(sourceKey)
                } else if (required == Karaka.KARMAN) {
                    if (targetOp.name == "सूचीसंयोगः" || targetOp.action.name == "सूचीसंयोगः") {
                        innerBindings[required] = ExecutionExpression.Reference("loop_result")
                    } else {
                        innerBindings[required] = ExecutionExpression.Coordination(
                            listOf(
                                ExecutionExpression.Reference("loop_result"),
                                ExecutionExpression.Reference("loop_index")
                            )
                        )
                    }
                }
            }
        }

        // 4. Execute the loop
        var currentResultValue = ""
        val initialStateExpr = context.bindings[Karaka.APADANA] ?: context.bindings[Karaka.SAMPRADANA]
        var currentResultTyped: SanskritValue? = initialStateExpr?.let {
            context.resolveValues(it).firstOrNull()
        }
        val trace = mutableListOf("Selected operation ${operation.name} for loop execution of '${targetOp.name}' $loopCount times.")

        for (i in 1..loopCount) {
            val innerVariables = context.variables.toMutableMap()
            val word = renderSankhyaResult(i.toLong()) ?: DevanagariDigits.render(i)
            innerVariables["loop_index"] = SanskritValue.Sankhya(i.toLong(), word)
            innerVariables["loop_result"] = currentResultTyped ?: SanskritValue.Sankhya(0L, "शून्यम्")

            val innerContext = ExecutionContext(
                bindings = innerBindings,
                selectedOperation = targetOp.name,
                variables = innerVariables,
                metadata = context.metadata,
                stateStore = context.stateStore,
                externalDispatcher = context.externalDispatcher
            )

            when (val result = targetOp.action.execute(innerContext, targetOp)) {
                is ExecutionResult.Success -> {
                    currentResultValue = result.value
                    currentResultTyped = result.typedValue ?: SanskritValue.of(result.value, targetOp.resultSamjnas)
                    trace += "Iteration $i success: ${result.value}"
                }
                is ExecutionResult.Failure -> {
                    return ExecutionResult.Failure(
                        result.error,
                        "Loop failed at iteration $i: ${result.message}",
                        trace + result.trace
                    )
                }
                else -> {
                    return ExecutionResult.Failure(
                        ExecutionError.ACTION_FAILED,
                        "Loop iteration $i returned unsupported state.",
                        trace
                    )
                }
            }
        }

        return ExecutionResult.Success(
            currentResultValue,
            operation.name,
            trace,
            currentResultTyped ?: SanskritValue.of(currentResultValue, targetOp.resultSamjnas)
        )
    }
}
