package dev.panini.execution

/** Inputs visible while one dhātu meaning is executed. */
data class ExecutionContext(
    val bindings: Map<Karaka, ExecutionExpression> = emptyMap(),
    val selectedOperation: String? = null,
    val variables: Map<String, String> = emptyMap(),
    val variableSamjnas: Map<String, Set<ExecutionSamjna>> = emptyMap(),
    val metadata: Map<String, String> = emptyMap(),
) {
    fun resolve(expression: ExecutionExpression): List<String> = when (expression) {
        is ExecutionExpression.Literal -> listOf(expression.value)
        is ExecutionExpression.Coordination -> expression.members.flatMap(::resolve)
        is ExecutionExpression.Reference -> variables[expression.name]?.let(::listOf)
            ?: emptyList()
    }

    fun literals(expression: ExecutionExpression): List<ExecutionExpression.Literal>? = when (expression) {
        is ExecutionExpression.Literal -> listOf(expression)
        is ExecutionExpression.Coordination -> expression.members.fold(emptyList()) { accumulated, member ->
            val resolved = literals(member) ?: return null
            accumulated + resolved
        }
        is ExecutionExpression.Reference -> variables[expression.name]?.let {
            listOf(ExecutionExpression.Literal(it, variableSamjnas[expression.name].orEmpty()))
        }
    }
}
