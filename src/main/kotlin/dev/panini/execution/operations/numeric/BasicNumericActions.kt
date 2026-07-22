package dev.panini.execution

import dev.panini.core.Karaka
import java.math.BigInteger

internal val sankhyaResultRenderer = SankhyaCountingFormRenderer()

internal fun renderSankhyaResult(value: Long): String? {
    if (value < 0) return null
    return runCatching { sankhyaResultRenderer.render(BigInteger.valueOf(value)) }.getOrNull()
}

internal fun ExecutionContext.resolveSankhyaValues(expression: ExecutionExpression): List<Long>? {
    val values = resolveValues(expression)
    if (values.any { it !is SanskritValue.Sankhya }) return null
    return values.map { (it as SanskritValue.Sankhya).value }
}

internal fun numericOverflow(operation: DhatuOperation): ExecutionResult.Failure = ExecutionResult.Failure(
    ExecutionError.INVALID_VALUE,
    "Numeric overflow while executing ${operation.id}.",
    listOf("Selected operation ${operation.id}."),
)

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
        val sum = runCatching { values.fold(0L, Math::addExact) }.getOrElse {
            return numericOverflow(operation)
        }
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
            SanskritValue.Sankhya(sum, result),
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
        val diff = runCatching { values.drop(1).fold(values.first(), Math::subtractExact) }.getOrElse {
            return numericOverflow(operation)
        }
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
            SanskritValue.Sankhya(diff, result),
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
        if (values.drop(1).any { it == 0L }) {
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
            SanskritValue.Sankhya(quotient, result),
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
        val product = runCatching { values.fold(1L, Math::multiplyExact) }.getOrElse {
            return numericOverflow(operation)
        }
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
            SanskritValue.Sankhya(product, result),
        )
    }
}

/** Counting elements in a coordinated expression or collection. */
object SanskritCountingAction : DhatuAction {
    const val ID = "sankhya.ganana"

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)
        val count = operands.size.toLong()
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
            SanskritValue.Sankhya(count, result),
        )
    }
}
