package dev.panini.compiler

import dev.panini.execution.ExecutionError
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

    @JvmStatic
    fun listLength(value: SanskritValue): SanskritValue = numeric(collectionItems(value).size.toLong())

    @JvmStatic
    fun listReverse(value: SanskritValue): SanskritValue = SanskritValue.Suchi(collectionItems(value).reversed())

    @JvmStatic
    fun listConcat(left: SanskritValue, right: SanskritValue): SanskritValue =
        SanskritValue.Suchi(collectionItems(left) + collectionItems(right))

    @JvmStatic
    fun listIndex(list: SanskritValue, index: SanskritValue): SanskritValue {
        val items = collectionItems(list)
        val numericIndex = (index as? SanskritValue.Sankhya)?.value
            ?: throw CompiledPaniniExecutionException(
                ExecutionError.INVALID_VALUE,
                "Index must be a valid saṅkhyā value.",
            )
        if (numericIndex !in 1L..items.size.toLong()) {
            throw CompiledPaniniExecutionException(
                ExecutionError.INVALID_VALUE,
                "Index $numericIndex out of bounds for list of size ${items.size}.",
            )
        }
        return items[numericIndex.toInt() - 1]
    }

    @JvmStatic
    fun listContains(list: SanskritValue, query: SanskritValue): SanskritValue = SanskritValue.Satya(
        collectionItems(list).any { item -> equivalent(item, query) },
    )

    @JvmStatic
    fun listAppend(list: SanskritValue, item: SanskritValue): SanskritValue = SanskritValue.Suchi(
        collectionItems(list) + item,
    )

    @JvmStatic
    fun listPop(list: SanskritValue): SanskritValue {
        val items = collectionItems(list)
        if (items.isEmpty()) {
            throw CompiledPaniniExecutionException(
                ExecutionError.INVALID_VALUE,
                "Cannot pop from an empty list.",
            )
        }
        return items.last()
    }

    @JvmStatic
    fun listSlice(list: SanskritValue, start: SanskritValue, end: SanskritValue): SanskritValue {
        val items = collectionItems(list)
        val startLong = (start as? SanskritValue.Sankhya)?.value
            ?: throw CompiledPaniniExecutionException(ExecutionError.INVALID_VALUE, "Start index must be a valid saṅkhyā value.")
        val endLong = (end as? SanskritValue.Sankhya)?.value
            ?: throw CompiledPaniniExecutionException(ExecutionError.INVALID_VALUE, "End index must be a valid saṅkhyā value.")
        if (startLong !in Int.MIN_VALUE.toLong()..Int.MAX_VALUE.toLong() ||
            endLong !in Int.MIN_VALUE.toLong()..Int.MAX_VALUE.toLong()
        ) {
            throw CompiledPaniniExecutionException(ExecutionError.INVALID_VALUE, "Slice indices are outside the supported range.")
        }
        val from = (startLong.toInt() - 1).coerceAtLeast(0)
        val to = endLong.toInt().coerceAtMost(items.size)
        return if (from > to || from >= items.size) {
            SanskritValue.Suchi(emptyList())
        } else {
            SanskritValue.Suchi(items.subList(from, to))
        }
    }

    private fun number(value: SanskritValue): Long = (value as? SanskritValue.Sankhya)?.value
        ?: error("Compiler comparison requires numeric values, but received ${value::class.simpleName}.")

    private fun numeric(value: Long): SanskritValue.Sankhya {
        val word = dev.panini.execution.renderSankhyaResult(value) ?: value.toString()
        return SanskritValue.Sankhya(value, word)
    }

    private fun collectionItems(value: SanskritValue): List<SanskritValue> = when (value) {
        is SanskritValue.Suchi -> value.items
        is SanskritValue.Gana -> value.elements
        else -> error("Compiler collection operation requires a list value, but received ${value::class.simpleName}.")
    }

    private fun equivalent(left: SanskritValue, right: SanskritValue): Boolean =
        if (left is SanskritValue.Sankhya && right is SanskritValue.Sankhya) {
            left.value == right.value
        } else {
            left.toDisplayText() == right.toDisplayText()
        }

}
