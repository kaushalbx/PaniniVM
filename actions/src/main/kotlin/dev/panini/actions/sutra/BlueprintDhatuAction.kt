package dev.panini.actions.sutra

import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.sutra.runtime.SutraBlueprint
import dev.panini.sutra.runtime.SutraBlueprintValidator

/**
 * A dhātu action whose executable meaning is carried by an evaluator-free
 * sūtra blueprint. The interpreter is the only host-language execution
 * boundary; individual actions contain no execution closure.
 */
abstract class BlueprintDhatuAction(
    name: String,
    description: String,
    val blueprint: SutraBlueprint,
) : DhatuAction(name, description) {
    final override fun execute(
        context: ExecutionContext,
        operation: DhatuOperation,
    ): ExecutionResult {
        val diagnostics = SutraBlueprintValidator.validate(blueprint)
        if (diagnostics.isNotEmpty()) {
            return ExecutionResult.Failure(
                ExecutionError.ACTION_FAILED,
                diagnostics.joinToString("; ") { it.message },
                listOf("Rejected invalid dhātu-action sūtra ${blueprint.id}."),
            )
        }
        return executeBlueprint(context, operation)
    }

    protected abstract fun executeBlueprint(
        context: ExecutionContext,
        operation: DhatuOperation,
    ): ExecutionResult
}
