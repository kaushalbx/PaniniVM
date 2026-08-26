package dev.panini.compiler

import dev.panini.execution.SanskritValue

/** Backend helper for explicit value IR comparisons. */
internal object CompilerValueOperations {
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
}
