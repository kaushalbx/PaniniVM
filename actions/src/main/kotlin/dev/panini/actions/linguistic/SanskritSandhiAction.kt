package dev.panini.actions.linguistic

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult

/** Sandhi joining (saṃhitā) over text operands using the Panini Ashtadhyayi rules via DerivationEngine handler. */
object SanskritSandhiAction : DhatuAction("संहिताकरणम्", "पदानां सन्धियोगः") {
    var sandhiHandler: ((String, String) -> String)? = null

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN]
            ?: return dev.panini.actions.missingKaraka(operation, Karaka.KARMAN)
        val padas = context.literals(expression)
        val operands = if (padas != null && padas.size >= 2) {
            padas.map { it.prakriti }
        } else {
            context.resolve(expression)
        }

        if (operands.size < 2) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Sandhi joining requires at least 2 text operands.",
                listOf("Selected operation ${operation.name}."),
            )
        }

        val handler = sandhiHandler ?: { a, b -> a + b }
        val result = operands.drop(1).fold(operands.first()) { acc, next ->
            handler(acc, next)
        }

        return ExecutionResult.Success(
            result,
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Joined ${operands.joinToString(" + ")} via DerivationEngine.",
                "Produced $result.",
            ),
        )
    }
}
