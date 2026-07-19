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

