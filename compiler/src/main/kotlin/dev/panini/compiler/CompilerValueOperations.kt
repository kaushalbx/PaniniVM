package dev.panini.compiler

import dev.panini.execution.semanticallyEquals

import dev.panini.execution.ExecutionError
import dev.panini.execution.SanskritValue

/** Backend helper for explicit value IR comparisons. */
internal object CompilerValueOperations {
    @JvmStatic
    fun foldNumericOperands(values: Array<SanskritValue>, groups: String, operator: String,
        minimumOperands: Int = 1): SanskritValue {
        require(minimumOperands > 0) { "Numeric folding requires a positive operand minimum." }
        require(values.size == groups.length && groups.all { it == '0' || it == '1' }) { "Invalid operand group flags." }
        val operands = values.flatMapIndexed { index, value -> expandParticipant(value, groups[index] == '1') }
        if (operands.size < minimumOperands) throw CompiledPaniniExecutionException(ExecutionError.INVALID_VALUE,
            "Numeric folding requires at least $minimumOperands number operands.")
        // Validate every input, including a single remembered participant.
        val numbers = operands.map(::number)
        val operation = ArithmeticOperator.valueOf(operator)
        require(operation in setOf(ArithmeticOperator.ADD, ArithmeticOperator.SUBTRACT,
            ArithmeticOperator.MULTIPLY, ArithmeticOperator.DIVIDE, ArithmeticOperator.REMAINDER)) {
            "Unsupported numeric-fold operator: $operation"
        }
        val result = try {
            numbers.drop(1).fold(numbers.first()) { left, right ->
                when (operation) {
                    ArithmeticOperator.ADD -> Math.addExact(left, right)
                    ArithmeticOperator.SUBTRACT -> Math.subtractExact(left, right)
                    ArithmeticOperator.MULTIPLY -> Math.multiplyExact(left, right)
                    ArithmeticOperator.DIVIDE -> left / right
                    ArithmeticOperator.REMAINDER -> left % right
                    else -> error("Unsupported numeric-fold operator: $operation")
                }
            }
        } catch (_: ArithmeticException) {
            throw CompiledPaniniExecutionException(ExecutionError.INVALID_VALUE,
                "Numeric folding overflow or division by zero.")
        }
        return numeric(result)
    }

    @JvmStatic
    fun packOperands(values: Array<SanskritValue>, groups: String): SanskritValue {
        val operands = packParticipantGroup(values, groups).elements
        return if (operands.size == 1) operands.single() else SanskritValue.Suchi(operands)
    }

    @JvmStatic
    fun packParticipantGroup(values: Array<SanskritValue>, groups: String): SanskritValue.Gana {
        require(values.size == groups.length && groups.all { it == '0' || it == '1' }) { "Invalid operand group flags." }
        val operands = values.flatMapIndexed { index, value -> expandParticipant(value, groups[index] == '1') }
        if (operands.isEmpty()) throw CompiledPaniniExecutionException(dev.panini.execution.ExecutionError.INVALID_VALUE,
            "Operand packing requires at least one participant.")
        return SanskritValue.Gana(operands)
    }

    fun expandParticipant(value: SanskritValue, group: Boolean): List<SanskritValue> =
        if (!group) listOf(value) else (value as? SanskritValue.Gana)?.elements
            ?: throw CompiledPaniniExecutionException(ExecutionError.INVALID_VALUE,
                "Participant expansion requires a grammatical group wrapper.")
    @JvmStatic
    fun listSnapshot(value: SanskritValue): SanskritValue {
        if (value !is SanskritValue.Suchi) throw CompiledPaniniExecutionException(
            ExecutionError.INVALID_VALUE, "Distributive iteration requires a list.")
        return value.copy(items = value.items.toList())
    }
    @JvmStatic
    fun checkNumericProhibition(left: SanskritValue, right: SanskritValue) {
        if (left is SanskritValue.Sankhya && right is SanskritValue.Sankhya && left.value == right.value) {
            throw CompiledPaniniExecutionException(ExecutionError.ACTION_FAILED,
                "निषेध-प्रतिषेधः: Numeric procedure prohibition triggered.")
        }
    }

    @JvmStatic
    fun requireArgumentKind(value: SanskritValue, kind: String) {
        requireValueKind(value, kind, "argument")
    }

    @JvmStatic
    fun requireResultKind(value: SanskritValue, kind: String) {
        requireValueKind(value, kind, "result")
    }

    private fun requireValueKind(value: SanskritValue, kind: String, role: String) {
        val valid = when (CompilerValueKind.valueOf(kind)) {
            CompilerValueKind.VALUE, CompilerValueKind.UNKNOWN -> true
            CompilerValueKind.NUMBER -> dev.panini.execution.OperationValueType.NUMBER.accepts(value)
            CompilerValueKind.ORDINAL -> value is SanskritValue.Purana
            CompilerValueKind.BOOLEAN -> value is SanskritValue.Satya
            CompilerValueKind.TEXT -> value is SanskritValue.Shabda
            CompilerValueKind.LIST -> dev.panini.execution.OperationValueType.COLLECTION.accepts(value)
            CompilerValueKind.RECORD -> value is SanskritValue.Rupa
            CompilerValueKind.RANGE -> value is SanskritValue.Range
        }
        if (!valid) throw CompiledPaniniExecutionException(ExecutionError.INVALID_VALUE,
            "Procedure $role requires $kind, not ${value::class.simpleName}.")
    }

    @JvmStatic
    fun randomActiveRange(value: SanskritValue, excluded: SanskritValue?): SanskritValue {
        val range = value as? SanskritValue.Range ?: throw CompiledPaniniExecutionException(
            ExecutionError.INVALID_VALUE, "Choice requires a preceding सीमा declaration.")
        return if (excluded == null) PaniniRuntime.randomRange(range.minimum.value, range.maximum.value)
            else PaniniRuntime.randomRangeExcluding(excluded, range.minimum.value, range.maximum.value)
    }

    @JvmStatic
    fun listSum(value: SanskritValue): SanskritValue {
        return dev.panini.execution.sumNumericCollection(value) { dev.panini.execution.renderSankhyaResult(it) }
            ?: throw CompiledPaniniExecutionException(ExecutionError.INVALID_VALUE,
                "Member summation requires valid numeric members and a representable result.")
    }

    @JvmStatic
    fun listNumberMemberSum(value: SanskritValue): SanskritValue {
        if (value !is SanskritValue.Suchi) throw CompiledPaniniExecutionException(
            ExecutionError.INVALID_VALUE, "Member summation requires one list as its genitive whole.")
        return listSum(value)
    }

    @JvmStatic
    fun renderText(values: Array<SanskritValue>): SanskritValue =
        SanskritValue.Shabda(values.joinToString(" ") { it.toDisplayText() })

    @JvmStatic
    fun isEven(value: SanskritValue): SanskritValue = SanskritValue.Satya(number(value) % 2L == 0L)

    @JvmStatic
    fun booleanize(value: SanskritValue): Boolean = (value as? SanskritValue.Satya)?.boolean
        ?: throw CompiledPaniniExecutionException(
            ExecutionError.INVALID_VALUE,
            "A compiled condition must produce सत्य/असत्य.",
        )

    @JvmStatic
    fun cardinalize(value: SanskritValue): SanskritValue {
        val number = number(value)
        val word = dev.panini.sankhya.SankhyaGenerator().cardinal(number).final.surface
        return SanskritValue.Sankhya(number, word)
    }

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
    fun minimum(left: SanskritValue, right: SanskritValue): SanskritValue = numeric(minOf(number(left), number(right)))

    @JvmStatic
    fun power(left: SanskritValue, right: SanskritValue): SanskritValue {
        val base = number(left)
        val exponent = number(right)
        require(exponent in 0..Int.MAX_VALUE.toLong()) { "Compiler exponent is unsupported: $exponent" }
        var result = 1L
        repeat(exponent.toInt()) { result = Math.multiplyExact(result, base) }
        return numeric(result)
    }

    @JvmStatic
    fun scaleDouble(value: SanskritValue): SanskritValue = numeric(number(value) * 2L)

    @JvmStatic
    fun exactSquareRoot(value: SanskritValue): SanskritValue {
        val number = number(value)
        if (number < 0L) {
            throw CompiledPaniniExecutionException(
                ExecutionError.INVALID_VALUE,
                "Square root of negative number $number is undefined.",
            )
        }
        val root = kotlin.math.sqrt(number.toDouble()).toLong()
        if ((root > 0L && root > Long.MAX_VALUE / root) || root * root != number) {
            throw CompiledPaniniExecutionException(
                ExecutionError.INVALID_VALUE,
                "Square root of $number is not an exact integer in the current Sanskrit number model.",
            )
        }
        return numeric(root)
    }

    @JvmStatic
    fun hypotenuse(left: SanskritValue, right: SanskritValue): SanskritValue {
        val first = number(left)
        val second = number(right)
        val squared = Math.addExact(Math.multiplyExact(first, first), Math.multiplyExact(second, second))
        return exactSquareRoot(numeric(squared))
    }

    @JvmStatic
    fun equal(left: SanskritValue, right: SanskritValue): Boolean = left.semanticallyEquals(right)

    @JvmStatic
    fun notEqual(left: SanskritValue, right: SanskritValue): Boolean = !left.semanticallyEquals(right)

    @JvmStatic
    fun lessThan(left: SanskritValue, right: SanskritValue): Boolean = numericOrder(left, right) < 0

    @JvmStatic
    fun lessThanOrEqual(left: SanskritValue, right: SanskritValue): Boolean = numericOrder(left, right) <= 0

    @JvmStatic
    fun greaterThan(left: SanskritValue, right: SanskritValue): Boolean = numericOrder(left, right) > 0

    @JvmStatic
    fun greaterThanOrEqual(left: SanskritValue, right: SanskritValue): Boolean = numericOrder(left, right) >= 0

    private fun numericOrder(left: SanskritValue, right: SanskritValue): Int =
        dev.panini.execution.compareNumericValues(left, right)
            ?: throw CompiledPaniniExecutionException(ExecutionError.INVALID_VALUE,
                "Ordering requires two defined numeric values.")

    @JvmStatic
    fun listLength(value: SanskritValue): SanskritValue = numeric(collectionItems(value).size.toLong())

    @JvmStatic
    fun listReverse(value: SanskritValue): SanskritValue = typedList(collectionItems(value).reversed(), value)

    @JvmStatic
    fun listFlatten(value: SanskritValue): SanskritValue = typedList(
        collectionItems(value).flatMap { item ->
            if (item is SanskritValue.Suchi) item.items else listOf(item)
        }, value,
    )

    @JvmStatic
    fun listConcat(left: SanskritValue, right: SanskritValue): SanskritValue {
        val leftType = (left as? SanskritValue.Suchi)?.memberType
        val rightType = (right as? SanskritValue.Suchi)?.memberType
        if (leftType != null && rightType != null && leftType != rightType) throw CompiledPaniniExecutionException(
            ExecutionError.INVALID_VALUE, "Cannot concatenate lists with incompatible declared member types.",
        )
        return typedList(collectionItems(left) + collectionItems(right), if (leftType != null) left else right)
    }

    @JvmStatic
    fun listIndex(list: SanskritValue, index: SanskritValue): SanskritValue {
        val items = collectionItems(list)
        val numericIndex = when (index) {
            is SanskritValue.Sankhya -> index.value
            is SanskritValue.Purana -> index.value
            else -> null
        }
            ?: throw CompiledPaniniExecutionException(
                ExecutionError.INVALID_VALUE,
                "Index must be a cardinal or ordinal position.",
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
        collectionItems(list).any { item -> item.semanticallyEquals(query) },
    )

    @JvmStatic
    fun listAppend(list: SanskritValue, item: SanskritValue): SanskritValue = typedList(
        collectionItems(list) + item, list,
    )

    @JvmStatic
    fun listPop(list: SanskritValue): SanskritValue {
        if (list !is SanskritValue.Suchi && list !is SanskritValue.Gana) {
            throw CompiledPaniniExecutionException(
                ExecutionError.INVALID_VALUE, "Collection extraction requires a collection value.",
            )
        }
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
        val from = (startLong - 1L).coerceAtLeast(0L).toInt()
        val to = endLong.toInt().coerceAtMost(items.size)
        return if (from > to || from >= items.size) {
            typedList(emptyList(), list)
        } else {
            typedList(items.subList(from, to), list)
        }
    }

    @JvmStatic
    fun recordField(record: SanskritValue, name: String): SanskritValue {
        val structured = record as? SanskritValue.Rupa
            ?: throw CompiledPaniniExecutionException(
                ExecutionError.INVALID_VALUE,
                "Field access requires a structured value.",
            )
        return structured.fields[name] ?: SanskritValue.Lopa
    }

    private fun typedList(items: List<SanskritValue>, source: SanskritValue): SanskritValue.Suchi {
        val type = (source as? SanskritValue.Suchi)?.memberType
        if (type != null && items.any { !type.accepts(it) }) throw CompiledPaniniExecutionException(
            ExecutionError.INVALID_VALUE, "List members must satisfy the declared $type type.",
        )
        return SanskritValue.Suchi(items, type)
    }

    @JvmStatic
    fun recordFieldOrLopa(record: SanskritValue, name: String): SanskritValue {
        val structured = record as? SanskritValue.Rupa
            ?: throw CompiledPaniniExecutionException(
                ExecutionError.INVALID_VALUE,
                "Field access requires a structured value.",
            )
        return structured.fields[name] ?: SanskritValue.Lopa
    }

    private fun number(value: SanskritValue): Long = (value as? SanskritValue.Sankhya)?.value
        ?: throw CompiledPaniniExecutionException(ExecutionError.INVALID_VALUE,
            "Numeric operation requires an integer value, but received ${value::class.simpleName}.")

    private fun numeric(value: Long): SanskritValue.Sankhya {
        val word = dev.panini.execution.renderSankhyaResult(value) ?: throw CompiledPaniniExecutionException(
            ExecutionError.INVALID_VALUE,
            "The result $value is outside the supported Sanskrit number vocabulary.",
        )
        return SanskritValue.Sankhya(value, word)
    }

    private fun collectionItems(value: SanskritValue): List<SanskritValue> = when (value) {
        is SanskritValue.Suchi -> value.items
        is SanskritValue.Gana -> value.elements
        else -> throw CompiledPaniniExecutionException(
            ExecutionError.INVALID_VALUE,
            "Compiler collection operation requires a collection value, but received ${value::class.simpleName}.",
        )
    }

}
