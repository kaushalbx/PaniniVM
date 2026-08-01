package dev.panini.actions

import dev.panini.core.Karaka
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue
import java.math.BigDecimal
import java.math.RoundingMode

internal fun missingKaraka(operation: DhatuOperation, karaka: Karaka): ExecutionResult.Failure =
    ExecutionResult.Failure(
        ExecutionError.MISSING_KARAKA,
        "${operation.name} requires a value in $karaka.",
        listOf("Selected operation ${operation.name}."),
    )

internal fun approximateNumber(value: Double, scale: Int = 9): SanskritValue.Rational? {
    if (!value.isFinite()) return null
    val decimal = BigDecimal.valueOf(value).setScale(scale, RoundingMode.HALF_UP).stripTrailingZeros()
    val denominator = BigDecimal.TEN.pow(decimal.scale().coerceAtLeast(0))
    val numerator = runCatching { decimal.movePointRight(decimal.scale().coerceAtLeast(0)).longValueExact() }
        .getOrNull() ?: return null
    val denominatorLong = runCatching { denominator.longValueExact() }.getOrNull() ?: return null
    val divisor = gcd(numerator, denominatorLong)
    return SanskritValue.Rational(
        numerator / divisor,
        denominatorLong / divisor,
        decimal.toPlainString(),
    )
}

private tailrec fun gcd(left: Long, right: Long): Long =
    if (right == 0L) kotlin.math.abs(left).coerceAtLeast(1L) else gcd(right, left % right)
