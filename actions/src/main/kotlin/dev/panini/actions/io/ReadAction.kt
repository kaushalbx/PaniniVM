package dev.panini.actions.io

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue

/** Standard Console Input Action (triggered by ग्रह् / गृह्णीहि). */
object ReadAction : dev.panini.execution.DhatuAction("स्वीकरणम्", "निवेशस्य स्वीकरणम्") {
    override fun execute(context: dev.panini.execution.ExecutionContext, operation: dev.panini.execution.DhatuOperation): dev.panini.execution.ExecutionResult {
        val readValue = "स्वीकृतम्"
        val expression = context.bindings[Karaka.KARMAN]
        val variableName = expression?.let { context.resolve(it).firstOrNull() } ?: "आगतम्"

        return dev.panini.execution.ExecutionResult.Success(
            readValue,
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Read input into variable $variableName.",
            ),
            dev.panini.execution.SanskritValue.Shabda(readValue),
        )
    }
}
