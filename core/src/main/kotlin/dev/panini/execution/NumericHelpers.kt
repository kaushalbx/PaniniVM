package dev.panini.execution

fun interface SankhyaResultRenderer {
    fun render(value: Long): String?

    companion object {
        var defaultRenderer: SankhyaResultRenderer = SankhyaResultRenderer { null }
    }
}

fun renderSankhyaResult(value: Long): String? {
    if (value < 0L) return null
    val result = SankhyaResultRenderer.defaultRenderer.render(value)
    if (result != null) return result

    return try {
        val clazz = Class.forName("dev.panini.sankhya.SankhyaCountingFormRenderer")
        val initMethod = clazz.getMethod("init")
        initMethod.invoke(null)
        SankhyaResultRenderer.defaultRenderer.render(value)
    } catch (_: Throwable) {
        null
    }
}

fun ExecutionContext.renderSankhyaResult(value: Long): String? =
    if (value < 0L) null else sankhyaRenderer.render(value) ?: renderSankhyaResult(value)

fun ExecutionContext.resolveSankhyaValues(expression: ExecutionExpression): List<Long>? {
    val values = resolveValues(expression)
    if (values.any { it !is SanskritValue.Sankhya }) return null
    return values.map { (it as SanskritValue.Sankhya).value }
}

/** Numeric members retain declared collection type and are not recursively flattened. */
fun numericCollectionMembers(value: SanskritValue): List<Long>? {
    val items = when (value) {
        is SanskritValue.Suchi -> {
            if (value.memberType == ListMemberType.TEXT) return null
            value.items
        }
        is SanskritValue.Gana -> value.elements
        else -> return null
    }
    if (items.any { it !is SanskritValue.Sankhya }) return null
    return items.map { (it as SanskritValue.Sankhya).value }
}

fun numericOverflow(operation: DhatuOperation): ExecutionResult.Failure = ExecutionResult.Failure(
    ExecutionError.INVALID_VALUE,
    "Numeric overflow while executing ${operation.name}.",
    listOf("Selected operation ${operation.name}."),
)
