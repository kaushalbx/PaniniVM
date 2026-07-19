package dev.panini.execution

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

/** Initial canonical vocabulary; it can grow independently of dhātu execution. */
object SanskritNumbers {
    private val words = listOf(
        "शून्य", "एक", "द्वि", "त्रि", "चतुर्", "पञ्च", "षट्", "सप्त", "अष्ट", "नव", "दश",
        "एकादश", "द्वादश", "त्रयोदश", "चतुर्दश", "पञ्चदश", "षोडश", "सप्तदश", "अष्टादश", "नवदश", "विंशति",
    )
    private val values = words.withIndex().associate { (value, word) -> word to value }

    fun valueOf(word: String): Int? = values[word]

    fun wordFor(value: Int): String? = words.getOrNull(value)
}
