package dev.panini.execution

import dev.panini.core.Karaka
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti

fun interface DhatuAction {
    fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult
}

/** Addition over a coordinated expression of canonical Sanskrit number words. */
object SanskritAdditionAction : DhatuAction {
    const val ID = "sankhya.yoga"

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)

        val values = operands.map { operand ->
            SanskritNumbers.valueOf(operand) ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "'$operand' is not a supported canonical Sanskrit number word.",
                listOf("Selected operation ${operation.id}."),
            )
        }
        val sum = values.sum()
        val result = SanskritNumbers.wordFor(sum) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "The result $sum is outside the supported Sanskrit number vocabulary.",
            listOf("Resolved ${operands.joinToString(" + ")}.")
        )
        return ExecutionResult.Success(
            result,
            operation.id,
            listOf(
                "Selected operation ${operation.id}.",
                "Resolved ${operands.joinToString(" + ")}.",
                "Produced $result.",
            ),
        )
    }
}

/** Subtraction over a coordinated expression of canonical Sanskrit number words. */
object SanskritSubtractionAction : DhatuAction {
    const val ID = "sankhya.viyoga"

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)

        val values = operands.map { operand ->
            SanskritNumbers.valueOf(operand) ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "'$operand' is not a supported canonical Sanskrit number word.",
                listOf("Selected operation ${operation.id}."),
            )
        }
        if (values.size < 2) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Subtraction requires at least 2 number operands.",
                listOf("Selected operation ${operation.id}."),
            )
        }
        val diff = values.drop(1).fold(values.first()) { acc, v -> acc - v }
        val result = SanskritNumbers.wordFor(diff) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "The result $diff is outside the supported Sanskrit number vocabulary.",
            listOf("Resolved ${operands.joinToString(" - ")}.")
        )
        return ExecutionResult.Success(
            result,
            operation.id,
            listOf(
                "Selected operation ${operation.id}.",
                "Resolved ${operands.joinToString(" - ")}.",
                "Produced $result.",
            ),
        )
    }
}

/** Division over a coordinated expression of canonical Sanskrit number words. */
object SanskritDivisionAction : DhatuAction {
    const val ID = "sankhya.harana"

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)

        val values = operands.map { operand ->
            SanskritNumbers.valueOf(operand) ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "'$operand' is not a supported canonical Sanskrit number word.",
                listOf("Selected operation ${operation.id}."),
            )
        }
        if (values.size < 2) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Division requires at least 2 number operands.",
                listOf("Selected operation ${operation.id}."),
            )
        }
        if (values.drop(1).any { it == 0 }) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Division by zero (शून्य) is undefined.",
                listOf("Selected operation ${operation.id}."),
            )
        }
        val quotient = values.drop(1).fold(values.first()) { acc, v -> acc / v }
        val result = SanskritNumbers.wordFor(quotient) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "The result $quotient is outside the supported Sanskrit number vocabulary.",
            listOf("Resolved ${operands.joinToString(" / ")}.")
        )
        return ExecutionResult.Success(
            result,
            operation.id,
            listOf(
                "Selected operation ${operation.id}.",
                "Resolved ${operands.joinToString(" / ")}.",
                "Produced $result.",
            ),
        )
    }
}

/** Multiplication over a coordinated expression of canonical Sanskrit number words. */
object SanskritMultiplicationAction : DhatuAction {
    const val ID = "sankhya.gunana"

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)

        val values = operands.map { operand ->
            SanskritNumbers.valueOf(operand) ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "'$operand' is not a supported canonical Sanskrit number word.",
                listOf("Selected operation ${operation.id}."),
            )
        }
        if (values.size < 2) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Multiplication requires at least 2 number operands.",
                listOf("Selected operation ${operation.id}."),
            )
        }
        val product = values.fold(1) { acc, v -> acc * v }
        val result = SanskritNumbers.wordFor(product) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "The result $product is outside the supported Sanskrit number vocabulary.",
            listOf("Resolved ${operands.joinToString(" * ")}.")
        )
        return ExecutionResult.Success(
            result,
            operation.id,
            listOf(
                "Selected operation ${operation.id}.",
                "Resolved ${operands.joinToString(" * ")}.",
                "Produced $result.",
            ),
        )
    }
}

/** Counting elements in a coordinated expression or collection. */
object SanskritCountingAction : DhatuAction {
    const val ID = "sankhya.ganana"

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)
        val count = operands.size
        val result = SanskritNumbers.wordFor(count) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "The count $count is outside the supported Sanskrit number vocabulary.",
            listOf("Counted ${operands.size} elements."),
        )
        return ExecutionResult.Success(
            result,
            operation.id,
            listOf(
                "Selected operation ${operation.id}.",
                "Counted ${operands.size} element(s).",
                "Produced $result.",
            ),
        )
    }
}

/** Sandhi joining (saṃhitā) over text operands. */
object SanskritSandhiAction : DhatuAction {
    const val ID = "samhita.karana"

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)
        if (operands.size < 2) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Sandhi joining requires at least 2 text operands.",
                listOf("Selected operation ${operation.id}."),
            )
        }
        val result = operands.drop(1).fold(operands.first()) { acc, next -> applySandhi(acc, next) }
        return ExecutionResult.Success(
            result,
            operation.id,
            listOf(
                "Selected operation ${operation.id}.",
                "Joined ${operands.joinToString(" + ")}.",
                "Produced $result.",
            ),
        )
    }

    private fun applySandhi(left: String, right: String): String {
        val l = left.trim()
        val r = right.trim()
        if (l.isEmpty()) return r
        if (r.isEmpty()) return l

        val lastChar = l.last()
        val firstChar = r.first()

        // 1. Vowel Sandhi (Ac-sandhi)
        // a/ā endings: अ, आ, or implicit vowel on consonant without halanta
        val extracted = extractFinalVowel(l)
        if (extracted != null) {
            val (lBase, lVowel) = extracted
            val combined = combineVowels(lVowel, firstChar)
            if (combined != null) {
                return lBase + combined + r.substring(1)
            }
        }

        // 2. Anusvara Sandhi (m + consonant -> ṃ + consonant)
        if (l.endsWith("म्") || l.endsWith("म")) {
            val stem = if (l.endsWith("म्")) l.dropLast(2) else l.dropLast(1)
            if (isConsonant(firstChar)) {
                return stem + "ं" + r
            }
        }

        // 3. Stutva / Cchutva (t + c -> cc)
        if (l.endsWith("त्") && firstChar == 'च') {
            return l.dropLast(2) + "च्च" + r.substring(1)
        }

        return l + r
    }

    private fun extractFinalVowel(text: String): Pair<String, Char>? {
        if (text.endsWith("अ") || text.endsWith("आ") || text.endsWith("इ") || text.endsWith("ई") ||
            text.endsWith("उ") || text.endsWith("ऊ") || text.endsWith("ऋ")
        ) {
            return text.dropLast(1) to text.last()
        }
        if (text.endsWith("ा")) return text.dropLast(1) to 'आ'
        if (text.endsWith("ि")) return text.dropLast(1) to 'इ'
        if (text.endsWith("ी")) return text.dropLast(1) to 'ई'
        if (text.endsWith("ु")) return text.dropLast(1) to 'उ'
        if (text.endsWith("ू")) return text.dropLast(1) to 'ऊ'
        if (text.endsWith("ृ")) return text.dropLast(1) to 'ऋ'
        if (text.endsWith("्")) return null // Halanta ending

        // Devanagari consonant with implicit schwa ('अ')
        val lastChar = text.last()
        if (isConsonant(lastChar)) {
            return text to 'अ'
        }
        return null
    }

    private fun combineVowels(v1: Char, v2: Char): String? = when {
        // Savarna-dirgha
        (v1 == 'अ' || v1 == 'आ') && (v2 == 'अ' || v2 == 'आ') -> "ा"
        (v1 == 'इ' || v1 == 'ई') && (v2 == 'इ' || v2 == 'ई') -> "ी"
        (v1 == 'उ' || v1 == 'ऊ') && (v2 == 'उ' || v2 == 'ऊ') -> "ू"
        // Guna
        (v1 == 'अ' || v1 == 'आ') && (v2 == 'इ' || v2 == 'ई' || v2 == 'ि' || v2 == 'ी') -> "े"
        (v1 == 'अ' || v1 == 'आ') && (v2 == 'उ' || v2 == 'ऊ' || v2 == 'ु' || v2 == 'ू') -> "ो"
        // Vriddhi
        (v1 == 'अ' || v1 == 'आ') && (v2 == 'ए' || v2 == 'ऐ') -> "ै"
        (v1 == 'अ' || v1 == 'आ') && (v2 == 'ओ' || v2 == 'औ') -> "ौ"
        else -> null
    }

    private fun isConsonant(c: Char): Boolean = c in '\u0915'..'\u0939'
}

/** Morphological subanta derivation from nominal prātipadika stem. */
object SanskritSubantaDerivationAction : DhatuAction {
    const val ID = "padanishpatti"

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)
        val stem = operands.firstOrNull() ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "Nominal derivation requires a prātipadika stem in KARMAN.",
            listOf("Selected operation ${operation.id}."),
        )
        return try {
            val engine = dev.panini.derivation.SubantaEngine()
            val request = dev.panini.derivation.SubantaDerivationRequest(
                pratipadika = stem,
                vibhakti = Vibhakti.PRATHAMA,
                vacana = Vacana.EKAVACANA,
                stemClass = dev.panini.derivation.SubantaStemClass.guess(stem),
            )
            val result = engine.derive(request).final.surface
            ExecutionResult.Success(
                result,
                operation.id,
                listOf(
                    "Selected operation ${operation.id}.",
                    "Derived subanta for prātipadika '$stem'.",
                    "Produced $result.",
                ),
            )
        } catch (e: Exception) {
            ExecutionResult.Failure(
                ExecutionError.ACTION_FAILED,
                "Subanta derivation failed for stem '$stem': ${e.message}",
                listOf("Selected operation ${operation.id}."),
            )
        }
    }
}

/** Modulo (remainder after division) over Sanskrit number words. */
object SanskritModuloAction : DhatuAction {
    const val ID = "sankhya.shesa"

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)
        val values = operands.map { operand ->
            SanskritNumbers.valueOf(operand) ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "'$operand' is not a supported canonical Sanskrit number word.",
                listOf("Selected operation ${operation.id}."),
            )
        }
        if (values.size < 2) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Modulo requires at least 2 number operands.",
                listOf("Selected operation ${operation.id}."),
            )
        }
        val divisor = values[1]
        if (divisor == 0) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Modulo by zero (शून्य) is undefined.",
                listOf("Selected operation ${operation.id}."),
            )
        }
        val rem = values[0] % divisor
        val result = SanskritNumbers.wordFor(rem) ?: return ExecutionResult.Failure(
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
        )
    }
}

/** Exponentiation (power / ghāta) over Sanskrit number words. */
object SanskritExponentiationAction : DhatuAction {
    const val ID = "sankhya.ghata"

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)
        val values = operands.map { operand ->
            SanskritNumbers.valueOf(operand) ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "'$operand' is not a supported canonical Sanskrit number word.",
                listOf("Selected operation ${operation.id}."),
            )
        }
        if (values.size < 2) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Exponentiation requires at least 2 number operands (base and exponent).",
                listOf("Selected operation ${operation.id}."),
            )
        }
        val base = values[0]
        val exp = values[1]
        var pow = 1
        for (i in 0 until exp) {
            pow *= base
        }
        val result = SanskritNumbers.wordFor(pow) ?: return ExecutionResult.Failure(
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
        )
    }
}

/** Comparison (maximum selection / tulanā) over Sanskrit number words. */
object SanskritComparisonAction : DhatuAction {
    const val ID = "sankhya.tulana"

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)
        val values = operands.map { operand ->
            SanskritNumbers.valueOf(operand) ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "'$operand' is not a supported canonical Sanskrit number word.",
                listOf("Selected operation ${operation.id}."),
            )
        }
        if (values.isEmpty()) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Comparison requires at least 1 number operand.",
                listOf("Selected operation ${operation.id}."),
            )
        }
        val maxVal = values.maxOrNull() ?: 0
        val result = SanskritNumbers.wordFor(maxVal) ?: return ExecutionResult.Failure(
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
        val value = SanskritNumbers.valueOf(inputStr) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "'$inputStr' is not a supported canonical Sanskrit number word.",
            listOf("Selected operation ${operation.id}."),
        )
        if (value < 0) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Square root of negative number $value is undefined.",
                listOf("Selected operation ${operation.id}."),
            )
        }
        val root = kotlin.math.sqrt(value.toDouble()).toInt()
        val result = SanskritNumbers.wordFor(root) ?: return ExecutionResult.Failure(
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
        )
    }
}

/** Averaging (sāmyakaraṇa / mādhyama) over Sanskrit number words. */
object SanskritAverageAction : DhatuAction {
    const val ID = "sankhya.samya"

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)
        val values = operands.map { operand ->
            SanskritNumbers.valueOf(operand) ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "'$operand' is not a supported canonical Sanskrit number word.",
                listOf("Selected operation ${operation.id}."),
            )
        }
        if (values.isEmpty()) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Averaging requires at least 1 number operand.",
                listOf("Selected operation ${operation.id}."),
            )
        }
        val avg = values.sum() / values.size
        val result = SanskritNumbers.wordFor(avg) ?: return ExecutionResult.Failure(
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
        )
    }
}

/** Fraction / Ratio / Proportion (bhāga / trairāśika) over Sanskrit number words. */
object SanskritFractionAction : DhatuAction {
    const val ID = "sankhya.bhaga"

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)
        val values = operands.map { operand ->
            SanskritNumbers.valueOf(operand) ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "'$operand' is not a supported canonical Sanskrit number word.",
                listOf("Selected operation ${operation.id}."),
            )
        }
        if (values.size < 2) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Fraction/Ratio requires at least 2 number operands.",
                listOf("Selected operation ${operation.id}."),
            )
        }
        val res = if (values.size >= 3) {
            val divisor = values[2]
            if (divisor == 0) return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Division by zero in proportion.", listOf("Selected operation ${operation.id}."))
            (values[0] * values[1]) / divisor
        } else {
            val divisor = values[1]
            if (divisor == 0) return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Division by zero in fraction.", listOf("Selected operation ${operation.id}."))
            values[0] / divisor
        }
        val result = SanskritNumbers.wordFor(res) ?: return ExecutionResult.Failure(
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
        )
    }
}

/** Minimum Selection (kaniṣṭhatva / nyūnatva) over Sanskrit number words. */
object SanskritMinAction : DhatuAction {
    const val ID = "sankhya.nyunatva"

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)
        val values = operands.map { operand ->
            SanskritNumbers.valueOf(operand) ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "'$operand' is not a supported canonical Sanskrit number word.",
                listOf("Selected operation ${operation.id}."),
            )
        }
        if (values.isEmpty()) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Minimum selection requires at least 1 number operand.",
                listOf("Selected operation ${operation.id}."),
            )
        }
        val minVal = values.minOrNull() ?: 0
        val result = SanskritNumbers.wordFor(minVal) ?: return ExecutionResult.Failure(
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
        )
    }
}

/** Variable Assignment & Value Binding (dā / मूल्यदानम्). */
object SanskritVariableAssignAction : DhatuAction {
    const val ID = "value.assign"

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)
        val value = operands.firstOrNull() ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "Variable assignment requires a value operand in KARMAN.",
            listOf("Selected operation ${operation.id}."),
        )
        return ExecutionResult.Success(
            value,
            operation.id,
            listOf(
                "Selected operation ${operation.id}.",
                "Assigned value '$value'.",
                "Produced $value.",
            ),
        )
    }
}

/** Variable Inspection & Querying (dṛś / मूल्यदर्शनम्). */
object SanskritVariableInspectAction : DhatuAction {
    const val ID = "value.inspect"

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)
        val target = operands.firstOrNull() ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "Variable inspection requires an operand in KARMAN.",
            listOf("Selected operation ${operation.id}."),
        )
        return ExecutionResult.Success(
            target,
            operation.id,
            listOf(
                "Selected operation ${operation.id}.",
                "Inspected target '$target'.",
                "Produced $target.",
            ),
        )
    }
}

/** State Persistence Save Action (smṛ / स्मृतिरक्षणम्). */
object SmritiSaveAction : DhatuAction {
    const val ID = "स्मृतिरक्षणम्"

    var globalStore: dev.panini.execution.persistence.StateStore? = null

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN]
            ?: return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Persistence save requires a key in KARMAN.")
        val operands = context.resolve(expression)
        val key = operands.firstOrNull() ?: "default_session"

        val store = globalStore ?: dev.panini.execution.persistence.FileStateStore(java.io.File(System.getProperty("user.home"), ".paninivm/sessions"))
        val activeContext = SambhashanaContext(
            speaker = "प्रयोक्ता",
            listener = "यन्त्रम्",
            mentionedEntities = context.variables.mapValues { (_, v) ->
                when (v) {
                    is SanskritValue.Sankhya -> v.word
                    is SanskritValue.Shabda -> v.text
                    is SanskritValue.Satya -> if (v.boolean) "सत्यम्" else "असत्यम्"
                    is SanskritValue.Gana -> v.toDisplayText()
                }
            },
        )
        store.save(key, activeContext)

        return ExecutionResult.Success(
            key,
            operation.id,
            listOf("Selected operation ${operation.id}.", "Saved context under session key '$key'."),
        )
    }
}

/** State Persistence Load Action (smṛ / स्मृतिपुनर्प्राप्तिः). */
object SmritiLoadAction : DhatuAction {
    const val ID = "स्मृतिपुनर्प्राप्तिः"

    var globalStore: dev.panini.execution.persistence.StateStore? = null

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN]
            ?: return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Persistence load requires a key in KARMAN.")
        val operands = context.resolve(expression)
        val key = operands.firstOrNull() ?: "default_session"

        val store = globalStore ?: dev.panini.execution.persistence.FileStateStore(java.io.File(System.getProperty("user.home"), ".paninivm/sessions"))
        val loaded = store.load(key) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "No saved context state found for session key '$key'.",
            listOf("Selected operation ${operation.id}."),
        )

        return ExecutionResult.Success(
            key,
            operation.id,
            listOf("Selected operation ${operation.id}.", "Loaded session context from '$key'."),
        )
    }
}

/** External System Dispatch Action (preṣ / बाह्यप्रेषणम्). */
object BahyaSendAction : DhatuAction {
    const val ID = "बाह्यप्रेषणम्"

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN]
            ?: return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "External dispatch requires a payload/command in KARMAN.")
        val operands = context.resolve(expression)
        val payload = operands.joinToString(" ")

        val effect = operation.effects.firstOrNull { it == ExecutionEffect.NETWORK || it == ExecutionEffect.EXECUTE_PROCESS || it == ExecutionEffect.SEND_MESSAGE }
            ?: ExecutionEffect.NETWORK

        val output = dev.panini.execution.external.ExternalCapabilityDispatcher.dispatch(effect, payload)

        return ExecutionResult.Success(
            output,
            operation.id,
            listOf("Selected operation ${operation.id}.", "Dispatched external effect $effect with payload '$payload'."),
        )
    }
}








