package dev.panini.execution

fun interface SankhyaResultRenderer {
    fun render(value: Long): String?

    companion object {
        var defaultRenderer: SankhyaResultRenderer = SankhyaResultRenderer { null }
    }
}

fun renderSankhyaResult(value: Long): String? {
    if (value < 0L) return null
    return SankhyaResultRenderer.defaultRenderer.render(value)
}

fun ExecutionContext.resolveSankhyaValues(expression: ExecutionExpression): List<Long>? {
    val values = resolveValues(expression)
    if (values.any { it !is SanskritValue.Sankhya }) return null
    return values.map { (it as SanskritValue.Sankhya).value }
}

fun numericOverflow(operation: DhatuOperation): ExecutionResult.Failure = ExecutionResult.Failure(
    ExecutionError.INVALID_VALUE,
    "Numeric overflow while executing ${operation.name}.",
    listOf("Selected operation ${operation.name}."),
)
