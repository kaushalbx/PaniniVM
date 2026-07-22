package dev.panini.execution

import dev.panini.core.Karaka
import java.math.BigInteger

internal val sankhyaResultRenderer = SankhyaCountingFormRenderer()

internal fun renderSankhyaResult(value: Int): String? {
    if (value < 0) return null
    return runCatching { sankhyaResultRenderer.render(BigInteger.valueOf(value.toLong())) }.getOrNull()
}

internal fun ExecutionContext.resolveSankhyaValues(expression: ExecutionExpression): List<Int>? {
    val values = resolveValues(expression)
    if (values.any { it !is SanskritValue.Sankhya }) return null
    return values.map { (it as SanskritValue.Sankhya).value.toInt() }
}

/** Addition over a coordinated expression of canonical Sanskrit number words. */
object SanskritAdditionAction : DhatuAction {
    const val ID = "sankhya.yoga"

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)

        val values = context.resolveSankhyaValues(expression) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "The operand is not an annotated saṅkhyā value.",
            listOf("Selected operation ${operation.id}."),
        )
        val sum = values.sum()
        val result = renderSankhyaResult(sum) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "The result $sum is outside the supported Sanskrit number vocabulary.",
            listOf("Resolved ${operands.joinToString(" + ")}.")
        )
        return ExecutionResult.Success(
            result,
            operation.id,
            listOf(
                "Selected operation ${operation.id}.",
                "Resolved ${operands.joinToString(" + ")}.",
                "Produced $result.",
            ),
            SanskritValue.Sankhya(sum.toLong(), result),
        )
    }
}

/** Subtraction over a coordinated expression of canonical Sanskrit number words. */
object SanskritSubtractionAction : DhatuAction {
    const val ID = "sankhya.viyoga"

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)

        val values = context.resolveSankhyaValues(expression) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE, "The operand is not an annotated saṅkhyā value."
        )
        if (values.size < 2) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Subtraction requires at least 2 number operands.",
                listOf("Selected operation ${operation.id}."),
            )
        }
        val diff = values.drop(1).fold(values.first()) { acc, v -> acc - v }
        val result = renderSankhyaResult(diff) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "The result $diff is outside the supported Sanskrit number vocabulary.",
            listOf("Resolved ${operands.joinToString(" - ")}.")
        )
        return ExecutionResult.Success(
            result,
            operation.id,
            listOf(
                "Selected operation ${operation.id}.",
                "Resolved ${operands.joinToString(" - ")}.",
                "Produced $result.",
            ),
            SanskritValue.Sankhya(diff.toLong(), result),
        )
    }
}

/** Division over a coordinated expression of canonical Sanskrit number words. */
object SanskritDivisionAction : DhatuAction {
    const val ID = "sankhya.harana"

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)

        val values = context.resolveSankhyaValues(expression) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE, "The operand is not an annotated saṅkhyā value."
        )
        if (values.size < 2) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Division requires at least 2 number operands.",
                listOf("Selected operation ${operation.id}."),
            )
        }
        if (values.drop(1).any { it == 0 }) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Division by zero (शून्य) is undefined.",
                listOf("Selected operation ${operation.id}."),
            )
        }
        val quotient = values.drop(1).fold(values.first()) { acc, v -> acc / v }
        val result = renderSankhyaResult(quotient) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "The result $quotient is outside the supported Sanskrit number vocabulary.",
            listOf("Resolved ${operands.joinToString(" / ")}.")
        )
        return ExecutionResult.Success(
            result,
            operation.id,
            listOf(
                "Selected operation ${operation.id}.",
                "Resolved ${operands.joinToString(" / ")}.",
                "Produced $result.",
            ),
            SanskritValue.Sankhya(quotient.toLong(), result),
        )
    }
}

/** Multiplication over a coordinated expression of canonical Sanskrit number words. */
object SanskritMultiplicationAction : DhatuAction {
    const val ID = "sankhya.gunana"

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)

        val values = context.resolveSankhyaValues(expression) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE, "The operand is not an annotated saṅkhyā value."
        )
        if (values.size < 2) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Multiplication requires at least 2 number operands.",
                listOf("Selected operation ${operation.id}."),
            )
        }
        val product = values.fold(1) { acc, v -> acc * v }
        val result = renderSankhyaResult(product) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "The result $product is outside the supported Sanskrit number vocabulary.",
            listOf("Resolved ${operands.joinToString(" * ")}.")
        )
        return ExecutionResult.Success(
            result,
            operation.id,
            listOf(
                "Selected operation ${operation.id}.",
                "Resolved ${operands.joinToString(" * ")}.",
                "Produced $result.",
            ),
            SanskritValue.Sankhya(product.toLong(), result),
        )
    }
}

/** Counting elements in a coordinated expression or collection. */
object SanskritCountingAction : DhatuAction {
    const val ID = "sankhya.ganana"

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)
        val count = operands.size
        val result = renderSankhyaResult(count) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "The count $count is outside the supported Sanskrit number vocabulary.",
            listOf("Counted ${operands.size} elements."),
        )
        return ExecutionResult.Success(
            result,
            operation.id,
            listOf(
                "Selected operation ${operation.id}.",
                "Counted ${operands.size} element(s).",
                "Produced $result.",
            ),
            SanskritValue.Sankhya(count.toLong(), result),
        )
    }
}

