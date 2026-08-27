package dev.panini.compiler

import dev.panini.execution.SanskritValue

/** Backend helper for explicit value IR comparisons. */
internal object CompilerValueOperations {
    @JvmStatic
    fun add(left: SanskritValue, right: SanskritValue): SanskritValue =
        numeric(Math.addExact(number(left), number(right)))

    @JvmStatic
    fun subtract(left: SanskritValue, right: SanskritValue): SanskritValue =
        numeric(Math.subtractExact(number(left), number(right)))

    @JvmStatic
    fun multiply(left: SanskritValue, right: SanskritValue): SanskritValue =
        numeric(Math.multiplyExact(number(left), number(right)))

    @JvmStatic
    fun divide(left: SanskritValue, right: SanskritValue): SanskritValue = numeric(number(left) / number(right))

    @JvmStatic
    fun remainder(left: SanskritValue, right: SanskritValue): SanskritValue = numeric(number(left) % number(right))

    @JvmStatic
    fun equal(left: SanskritValue, right: SanskritValue): Boolean = left == right

    @JvmStatic
    fun notEqual(left: SanskritValue, right: SanskritValue): Boolean = left != right

    @JvmStatic
    fun lessThan(left: SanskritValue, right: SanskritValue): Boolean = number(left) < number(right)

    @JvmStatic
    fun lessThanOrEqual(left: SanskritValue, right: SanskritValue): Boolean = number(left) <= number(right)

    @JvmStatic
    fun greaterThan(left: SanskritValue, right: SanskritValue): Boolean = number(left) > number(right)

    @JvmStatic
    fun greaterThanOrEqual(left: SanskritValue, right: SanskritValue): Boolean = number(left) >= number(right)

    private fun number(value: SanskritValue): Long = (value as? SanskritValue.Sankhya)?.value
        ?: error("Compiler comparison requires numeric values, but received ${value::class.simpleName}.")

    private fun numeric(value: Long): SanskritValue.Sankhya {
        val word = dev.panini.execution.renderSankhyaResult(value) ?: value.toString()
        return SanskritValue.Sankhya(value, word)
    }
}
