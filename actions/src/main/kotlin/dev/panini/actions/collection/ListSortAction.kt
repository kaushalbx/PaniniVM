package dev.panini.actions.collection

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue
import java.math.BigInteger

/** Stable typed natural-order sorting. */
sealed class ListSortAction(name: String, private val descending: Boolean) :
    DhatuAction(name, if (descending) "सूचीसदस्यानाम् अवरोहक्रमः" else "सूचीसदस्यानाम् आरोहक्रमः") {

    object Ascending : ListSortAction("सूच्यारोहक्रमः", false)
    object Descending : ListSortAction("सूच्यवरोहक्रमः", true)

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN]
            ?: return failure("Sort requires one list or group in KARMAN.")
        val source = context.resolveValues(expression).singleOrNull()
            ?: return failure("Sort requires exactly one list or group.")
        val members = when (source) {
            is SanskritValue.Suchi -> source.items
            is SanskritValue.Gana -> source.elements
            else -> return failure("Sort cannot use a scalar collection operand.")
        }
        val kind = members.firstOrNull()?.javaClass
        if (members.any { it.javaClass != kind } || members.any { orderKey(it) == null }) {
            return failure("Sort requires members of one comparable typed kind (number, ordinal, rational, text, or truth).")
        }
        val comparator = Comparator<SanskritValue> { left, right -> compareTyped(left, right) }
        val ordered = if (descending) members.sortedWith(comparator.reversed()) else members.sortedWith(comparator)
        val value = SanskritValue.Suchi(ordered, (source as? SanskritValue.Suchi)?.memberType)
        return ExecutionResult.Success(
            value.toDisplayText(), operation.name,
            listOf("Stably sorted ${members.size} member(s) ${if (descending) "descending" else "ascending"}."),
            value,
        )
    }

    private fun orderKey(value: SanskritValue): Any? = when (value) {
        is SanskritValue.Sankhya -> value.value
        is SanskritValue.Purana -> value.value
        is SanskritValue.Rational -> value
        is SanskritValue.Shabda -> value.text
        is SanskritValue.Satya -> value.boolean
        else -> null
    }

    private fun compareTyped(left: SanskritValue, right: SanskritValue): Int = when (left) {
        is SanskritValue.Sankhya -> left.value.compareTo((right as SanskritValue.Sankhya).value)
        is SanskritValue.Purana -> left.value.compareTo((right as SanskritValue.Purana).value)
        is SanskritValue.Rational -> {
            val other = right as SanskritValue.Rational
            BigInteger.valueOf(left.numerator).multiply(BigInteger.valueOf(other.denominator))
                .compareTo(BigInteger.valueOf(other.numerator).multiply(BigInteger.valueOf(left.denominator)))
        }
        is SanskritValue.Shabda -> left.text.compareTo((right as SanskritValue.Shabda).text)
        is SanskritValue.Satya -> left.boolean.compareTo((right as SanskritValue.Satya).boolean)
        else -> error("Unsupported sort kind")
    }

    private fun failure(message: String) = ExecutionResult.Failure(ExecutionError.INVALID_VALUE, message)
}
