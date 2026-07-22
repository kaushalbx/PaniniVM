package dev.panini.execution

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
    "Numeric overflow while executing ${operation.name}.",
    listOf("Selected operation ${operation.name}."),
)
