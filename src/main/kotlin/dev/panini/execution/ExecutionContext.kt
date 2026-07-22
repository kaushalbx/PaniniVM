package dev.panini.execution

import dev.panini.core.Karaka

/** Inputs visible while one dhātu meaning is executed. */
data class ExecutionContext(
    val bindings: Map<Karaka, ExecutionExpression> = emptyMap(),
    val selectedOperation: String? = null,
    val variables: Map<String, SanskritValue> = emptyMap(),
    val metadata: Map<String, String> = emptyMap(),
) {
    constructor(
        bindings: Map<Karaka, ExecutionExpression> = emptyMap(),
        selectedOperation: String? = null,
        rawVariables: Map<String, String>,
        variableSamjnas: Map<String, Set<ExecutionSamjna>> = emptyMap(),
        metadata: Map<String, String> = emptyMap(),
    ) : this(
        bindings = bindings,
        selectedOperation = selectedOperation,
        variables = rawVariables.mapValues { (key, value) ->
            SanskritValue.of(value, variableSamjnas[key].orEmpty())
        },
        metadata = metadata,
    )

    val variableSamjnas: Map<String, Set<ExecutionSamjna>>
        get() = variables.mapValues { it.value.samjnas }

    fun resolveValues(expression: ExecutionExpression): List<SanskritValue> = when (expression) {
        is ExecutionExpression.Pada -> listOf(
            expression.value ?: SanskritValue.of(expression.prakriti, expression.samjnas),
        )
        is ExecutionExpression.Coordination -> expression.members.flatMap(::resolveValues)
        is ExecutionExpression.Reference -> variables[expression.name]?.let(::listOf)
            ?: emptyList()
    }

    fun resolve(expression: ExecutionExpression): List<String> =
        resolveValues(expression).map { it.toDisplayText() }

    fun literals(expression: ExecutionExpression): List<ExecutionExpression.Pada>? = when (expression) {
        is ExecutionExpression.Pada -> listOf(expression)
        is ExecutionExpression.Coordination -> expression.members.fold(emptyList()) { accumulated, member ->
            val resolved = literals(member) ?: return null
            accumulated + resolved
        }
        is ExecutionExpression.Reference -> variables[expression.name]?.let { typed ->
            listOf(ExecutionExpression.Pada(typed.toDisplayText(), typed.samjnas, typed))
        }
    }
}
