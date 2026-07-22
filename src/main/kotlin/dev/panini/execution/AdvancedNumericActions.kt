package dev.panini.execution

import dev.panini.core.Karaka

/** Modulo (remainder after division) over Sanskrit number words. */
object SanskritModuloAction : DhatuAction {
    const val ID = "sankhya.shesa"

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)
        val values = context.resolveSankhyaValues(expression) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE, "The operand is not an annotated saṅkhyā value."
        )
        if (values.size < 2) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Modulo requires at least 2 number operands.",
                listOf("Selected operation ${operation.id}."),
            )
        }
        val divisor = values[1]
        if (divisor == 0L) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Modulo by zero (शून्य) is undefined.",
                listOf("Selected operation ${operation.id}."),
            )
        }
        val rem = values[0] % divisor
        val result = renderSankhyaResult(rem) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "The result $rem is outside the supported Sanskrit number vocabulary.",
            listOf("Resolved ${operands[0]} % ${operands[1]}.")
        )
        return ExecutionResult.Success(
            result,
            operation.id,
            listOf(
                "Selected operation ${operation.id}.",
                "Resolved ${operands[0]} % ${operands[1]}.",
                "Produced $result.",
            ),
            SanskritValue.Sankhya(rem, result),
        )
    }
}

/** Exponentiation (power / ghāta) over Sanskrit number words. */
object SanskritExponentiationAction : DhatuAction {
    const val ID = "sankhya.ghata"

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)
        val values = context.resolveSankhyaValues(expression) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE, "The operand is not an annotated saṅkhyā value."
        )
        if (values.size < 2) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Exponentiation requires at least 2 number operands (base and exponent).",
                listOf("Selected operation ${operation.id}."),
            )
        }
        val base = values[0]
        val exp = values[1]
        if (exp < 0 || exp > Int.MAX_VALUE) {
            return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Exponent $exp is unsupported.")
        }
        val pow = runCatching {
            var value = 1L
            repeat(exp.toInt()) { value = Math.multiplyExact(value, base) }
            value
        }.getOrElse { return numericOverflow(operation) }
        val result = renderSankhyaResult(pow) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "The result $pow ($base^$exp) is outside the supported Sanskrit number vocabulary.",
            listOf("Resolved $base ^ $exp.")
        )
        return ExecutionResult.Success(
            result,
            operation.id,
            listOf(
                "Selected operation ${operation.id}.",
                "Resolved $base ^ $exp.",
                "Produced $result.",
            ),
            SanskritValue.Sankhya(pow, result),
        )
    }
}

/** Comparison (maximum selection / tulanā) over Sanskrit number words. */
object SanskritComparisonAction : DhatuAction {
    const val ID = "sankhya.tulana"

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)
        val values = context.resolveSankhyaValues(expression) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE, "The operand is not an annotated saṅkhyā value."
        )
        if (values.isEmpty()) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Comparison requires at least 1 number operand.",
                listOf("Selected operation ${operation.id}."),
            )
        }
        val maxVal = values.maxOrNull() ?: 0L
        val result = renderSankhyaResult(maxVal) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "The max result $maxVal is outside the supported Sanskrit number vocabulary.",
            listOf("Compared ${operands.joinToString()}."),
        )
        return ExecutionResult.Success(
            result,
            operation.id,
            listOf(
                "Selected operation ${operation.id}.",
                "Compared ${operands.joinToString()}.",
                "Produced $result.",
            ),
            SanskritValue.Sankhya(maxVal, result),
        )
    }
}

/** Square Root (mūla) over Sanskrit number words. */
object SanskritSquareRootAction : DhatuAction {
    const val ID = "sankhya.mula"

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)
        val inputStr = operands.firstOrNull() ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "Square root requires a number operand in KARMAN.",
            listOf("Selected operation ${operation.id}."),
        )
        val value = context.resolveSankhyaValues(expression)?.firstOrNull() ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "'$inputStr' is not an annotated saṅkhyā value.",
            listOf("Selected operation ${operation.id}."),
        )
        if (value < 0) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Square root of negative number $value is undefined.",
                listOf("Selected operation ${operation.id}."),
            )
        }
        val root = kotlin.math.sqrt(value.toDouble()).toLong()
        val result = renderSankhyaResult(root) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "The root result $root is outside the supported Sanskrit number vocabulary.",
            listOf("Sqrt($value).")
        )
        return ExecutionResult.Success(
            result,
            operation.id,
            listOf(
                "Selected operation ${operation.id}.",
                "Calculated sqrt($value).",
                "Produced $result.",
            ),
            SanskritValue.Sankhya(root, result),
        )
    }
}

/** Averaging (sāmyakaraṇa / mādhyama) over Sanskrit number words. */
object SanskritAverageAction : DhatuAction {
    const val ID = "sankhya.samya"

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)
        val values = context.resolveSankhyaValues(expression) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE, "The operand is not an annotated saṅkhyā value."
        )
        if (values.isEmpty()) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Averaging requires at least 1 number operand.",
                listOf("Selected operation ${operation.id}."),
            )
        }
        val sum = runCatching { values.fold(0L, Math::addExact) }.getOrElse {
            return numericOverflow(operation)
        }
        val avg = sum / values.size
        val result = renderSankhyaResult(avg) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "The average result $avg is outside the supported Sanskrit number vocabulary.",
            listOf("Averaged ${operands.joinToString()}."),
        )
        return ExecutionResult.Success(
            result,
            operation.id,
            listOf(
                "Selected operation ${operation.id}.",
                "Averaged ${operands.joinToString()}.",
                "Produced $result.",
            ),
            SanskritValue.Sankhya(avg, result),
        )
    }
}

/** Fraction / Ratio / Proportion (bhāga / trairāśika) over Sanskrit number words. */
object SanskritFractionAction : DhatuAction {
    const val ID = "sankhya.bhaga"

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)
        val values = context.resolveSankhyaValues(expression) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE, "The operand is not an annotated saṅkhyā value."
        )
        if (values.size < 2) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Fraction/Ratio requires at least 2 number operands.",
                listOf("Selected operation ${operation.id}."),
            )
        }
        val res = if (values.size >= 3) {
            val divisor = values[2]
            if (divisor == 0L) return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Division by zero in proportion.", listOf("Selected operation ${operation.id}."))
            val numerator = runCatching { Math.multiplyExact(values[0], values[1]) }
                .getOrElse { return numericOverflow(operation) }
            numerator / divisor
        } else {
            val divisor = values[1]
            if (divisor == 0L) return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Division by zero in fraction.", listOf("Selected operation ${operation.id}."))
            values[0] / divisor
        }
        val result = renderSankhyaResult(res) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "The fraction result $res is outside the supported Sanskrit number vocabulary.",
            listOf("Calculated fraction/proportion."),
        )
        return ExecutionResult.Success(
            result,
            operation.id,
            listOf(
                "Selected operation ${operation.id}.",
                "Calculated ratio/proportion ${operands.joinToString()}.",
                "Produced $result.",
            ),
            SanskritValue.Sankhya(res, result),
        )
    }
}

/** Minimum Selection (kaniṣṭhatva / nyūnatva) over Sanskrit number words. */
object SanskritMinAction : DhatuAction {
    const val ID = "sankhya.nyunatva"

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)
        val values = context.resolveSankhyaValues(expression) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE, "The operand is not an annotated saṅkhyā value."
        )
        if (values.isEmpty()) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Minimum selection requires at least 1 number operand.",
                listOf("Selected operation ${operation.id}."),
            )
        }
        val minVal = values.minOrNull() ?: 0L
        val result = renderSankhyaResult(minVal) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "The min result $minVal is outside the supported Sanskrit number vocabulary.",
            listOf("Calculated minimum of ${operands.joinToString()}."),
        )
        return ExecutionResult.Success(
            result,
            operation.id,
            listOf(
                "Selected operation ${operation.id}.",
                "Found minimum among ${operands.joinToString()}.",
                "Produced $result.",
            ),
            SanskritValue.Sankhya(minVal, result),
        )
    }
}
