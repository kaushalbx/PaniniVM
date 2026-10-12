package dev.panini.execution

/** Exact scalar ordering; null denotes a nonnumber or an undefined fraction. */
fun compareNumericValues(left: SanskritValue, right: SanskritValue): Int? {
    fun fraction(value: SanskritValue): Pair<java.math.BigInteger, java.math.BigInteger>? = when (value) {
        is SanskritValue.Sankhya -> java.math.BigInteger.valueOf(value.value) to java.math.BigInteger.ONE
        is SanskritValue.Rational -> if (value.denominator == 0L) null else
            java.math.BigInteger.valueOf(value.numerator) to java.math.BigInteger.valueOf(value.denominator)
        else -> null
    }
    val (ln, ld) = fraction(left) ?: return null
    val (rn, rd) = fraction(right) ?: return null
    return (ln * rd).compareTo(rn * ld) * ld.signum() * rd.signum()
}

fun interface SankhyaResultRenderer {
    fun render(value: Long): String?

    companion object {
        var defaultRenderer: SankhyaResultRenderer = SankhyaResultRenderer { null }
    }
}

fun renderSankhyaResult(value: Long): String? = renderDefaultSankhyaResult(value)

private fun renderDefaultSankhyaResult(value: Long): String? {
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
    if (value < 0L) null else sankhyaRenderer.render(value) ?: renderDefaultSankhyaResult(value)

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

/** Sums immediate numeric members exactly; never flattens nested collections. */
fun sumNumericCollection(value: SanskritValue, render: (Long) -> String?): SanskritValue? {
    val items = when (value) {
        is SanskritValue.Suchi -> if (value.memberType == ListMemberType.TEXT) return null else value.items
        is SanskritValue.Gana -> value.elements
        else -> return null
    }
    if (items.any { !OperationValueType.NUMBER.accepts(it) }) return null
    return runCatching {
        if (items.none { it is SanskritValue.Rational }) {
            val sum = items.fold(0L) { acc, item -> Math.addExact(acc, (item as SanskritValue.Sankhya).value) }
            return render(sum)?.let { SanskritValue.Sankhya(sum, it) }
        }
        var numerator = java.math.BigInteger.ZERO
        var denominator = java.math.BigInteger.ONE
        for (item in items) {
            val n = java.math.BigInteger.valueOf(when (item) {
                is SanskritValue.Sankhya -> item.value
                is SanskritValue.Rational -> item.numerator
                else -> error("Validated numeric member")
            })
            val d = java.math.BigInteger.valueOf((item as? SanskritValue.Rational)?.denominator ?: 1L)
            numerator = numerator * d + n * denominator
            denominator *= d
            val gcd = numerator.gcd(denominator)
            numerator /= gcd
            denominator /= gcd
            if (denominator.signum() < 0) { numerator = -numerator; denominator = -denominator }
        }
        val n = numerator.longValueExact()
        val d = denominator.longValueExact()
        if (d == 1L) render(n)?.let { SanskritValue.Sankhya(n, it) }
        else SanskritValue.Rational(n, d, "$n/$d")
    }.getOrNull()
}

fun numericOverflow(operation: DhatuOperation): ExecutionResult.Failure = ExecutionResult.Failure(
    ExecutionError.INVALID_VALUE,
    "Numeric overflow while executing ${operation.name}.",
    listOf("Selected operation ${operation.name}."),
)
