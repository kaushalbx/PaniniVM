package dev.panini.actions.collection

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

/** Filter list elements based on a predicate (triggered by वृज् / वर्जन). */
object ListFilterAction : DhatuAction("सूचीशोधनम्", "सूचीसंशोधनम् अंशानां निष्कासनम् च") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        // 1. Resolve list (from KARMAN)
        val listExpression = context.bindings[Karaka.KARMAN]
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "List filter execution requires a list in KARMAN."
            )

        val listValues = context.resolveValues(listExpression)
        val firstVal = listValues.firstOrNull() ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "Target list resolves to empty."
        )

        val elements = when (firstVal) {
            is SanskritValue.Suchi -> firstVal.items
            is SanskritValue.Gana -> firstVal.elements
            else -> listValues
        }

        // 2. Resolve target predicate operation (from KARANA or ADHIKARANA)
        val targetKey = if (context.bindings.containsKey(Karaka.KARANA)) Karaka.KARANA else Karaka.ADHIKARANA
        val targetExpression = context.bindings[targetKey]
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "List filter execution requires a target action or verbal root in KARANA or ADHIKARANA."
            )

        val targetName = context.resolve(targetExpression).firstOrNull()?.trim()
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Target operation cannot be resolved to a name."
            )

        val normTarget = targetName.removeSuffix("म्").trimEnd('्', 'ँ')
        val targetOp = DhatuPatha.all.flatMap { it.operations }
            .firstOrNull { 
                it.name == targetName || it.action.name == targetName ||
                it.name.removeSuffix("म्").trimEnd('्', 'ँ') == normTarget ||
                it.action.name.removeSuffix("म्").trimEnd('्', 'ँ') == normTarget
            }
            ?: DhatuPatha.all.firstOrNull {
                it.upadesha == targetName || it.sourceSurface == targetName || it.surfaceAliases.contains(targetName) ||
                it.upadesha.removeSuffix("म्").trimEnd('्', 'ँ') == normTarget ||
                it.sourceSurface.removeSuffix("म्").trimEnd('्', 'ँ') == normTarget ||
                it.surfaceAliases.any { alias -> alias.removeSuffix("म्").trimEnd('्', 'ँ') == normTarget }
            }?.operations?.firstOrNull()
            ?: return ExecutionResult.Failure(
                ExecutionError.ACTION_FAILED,
                "Verbal root or operation '$targetName' not found in registry."
            )

        // 3. Prepare target required Karaka bindings
        val targetRequiredKarakas = targetOp.signature.requirements.map { it.karaka }
        val innerBindings = mutableMapOf<Karaka, ExecutionExpression>()

        // Copy non-conflicting bindings
        context.bindings.forEach { (k, v) ->
            if (k != Karaka.KARMAN && k != targetKey) {
                innerBindings[k] = v
            }
        }

        // Map the first required parameter of the target dynamically to Reference("loop_element")
        targetRequiredKarakas.forEach { required ->
            if (required !in innerBindings) {
                innerBindings[required] = ExecutionExpression.Reference("loop_element")
            }
        }

        // 4. Run filter loop
        val trace = mutableListOf("Selected operation ${operation.name} for filtering list of size ${elements.size} using '${targetOp.name}'.")
        val results = mutableListOf<SanskritValue>()

        elements.forEachIndexed { idx, element ->
            val i = idx + 1
            val innerVariables = context.variables.toMutableMap()
            val word = renderSankhyaResult(i.toLong()) ?: DevanagariDigits.render(i)
            innerVariables["loop_index"] = SanskritValue.Sankhya(i.toLong(), word)
            innerVariables["loop_element"] = element

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
                    val resultTyped = result.typedValue ?: SanskritValue.of(result.value, targetOp.resultSamjnas)
                    val keep = when (resultTyped) {
                        is SanskritValue.Satya -> resultTyped.boolean
                        else -> result.value == "सत्यम्"
                    }
                    if (keep) {
                        results += element
                        trace += "Element $i ('${element.toDisplayText()}') kept (matched)."
                    } else {
                        trace += "Element $i ('${element.toDisplayText()}') filtered out."
                    }
                }
                is ExecutionResult.Failure -> {
                    return ExecutionResult.Failure(
                        result.error,
                        "Filter failed at element $i: ${result.message}",
                        trace + result.trace
                    )
                }
                else -> {
                    return ExecutionResult.Failure(
                        ExecutionError.ACTION_FAILED,
                        "Filter element $i returned unsupported state.",
                        trace
                    )
                }
            }
        }

        val filteredList = SanskritValue.Suchi(results)
        return ExecutionResult.Success(
            filteredList.toDisplayText(),
            operation.name,
            trace,
            filteredList
        )
    }
}
