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

/** If/Else conditional branching action. */
object IfAction : DhatuAction("निर्णयः", "यदि-तर्हि विकल्पः (इफ्-एल्स्)") {
    private fun normalize(name: String): String {
        return name.removeSuffix("म्").trimEnd('्', 'ँ', 'ः')
    }

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val condExpr = context.bindings[Karaka.APADANA]
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "If condition predicate action must be bound in APADANA."
            )

        val condResult = context.resolve(condExpr).firstOrNull()?.trim()
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Condition predicate cannot be resolved to a value."
            )

        val isTrue = condResult == "सत्यम्" || condResult == "सत्य"

        val targetExpr = if (isTrue) {
            context.bindings[Karaka.KARANA]
        } else {
            context.bindings[Karaka.SAMPRADANA]
        }

        if (targetExpr == null) {
            return ExecutionResult.Success(
                "शून्यम्",
                operation.name,
                listOf("Condition evaluated to false, no else branch executed."),
                SanskritValue.of("शून्यम्")
            )
        }

        val targetName = context.resolve(targetExpr).firstOrNull()?.trim()
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Branch target action cannot be resolved to a name."
            )

        // Find branch target operation in registry
        val normTarget = normalize(targetName)
        val targetOp = DhatuPatha.all.flatMap { it.operations }
            .firstOrNull { 
                it.name == targetName || it.action.name == targetName ||
                normalize(it.name) == normTarget ||
                normalize(it.action.name) == normTarget
            }
            ?: DhatuPatha.all.firstOrNull {
                it.upadesha == targetName || it.sourceSurface == targetName || it.surfaceAliases.contains(targetName) ||
                normalize(it.upadesha) == normTarget ||
                normalize(it.sourceSurface) == normTarget ||
                it.surfaceAliases.any { alias -> normalize(alias) == normTarget }
            }?.operations?.firstOrNull()
            ?: return ExecutionResult.Failure(
                ExecutionError.ACTION_FAILED,
                "Branch target operation '$targetName' not found in registry."
            )

        // Bind remaining inputs to target operation.
        val innerBindings = context.bindings.filterKeys {
            it != Karaka.APADANA && it != Karaka.KARANA && it != Karaka.SAMPRADANA
        }

        val innerContext = ExecutionContext(
            bindings = innerBindings,
            selectedOperation = targetOp.name,
            variables = context.variables,
            metadata = context.metadata,
            stateStore = context.stateStore,
            externalDispatcher = context.externalDispatcher
        )

        return targetOp.action.execute(innerContext, targetOp)
    }
}
