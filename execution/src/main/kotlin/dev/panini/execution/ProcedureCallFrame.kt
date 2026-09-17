package dev.panini.execution

import dev.panini.execution.binding.NumeralPadaBinder
import dev.panini.sankhya.SankhyaEvaluator

/** Runtime state owned by one reusable-procedure call. */
data class ProcedureCallFrame(
    val parameterBindings: Map<String, SanskritValue>,
    val arguments: List<SanskritValue>,
    val localScope: ExecutionScope,
    val callerSource: String?,
    var returnValue: SanskritValue? = null,
) {
    companion object {
        private val sankhyaEvaluator = SankhyaEvaluator()

        fun create(
            invocation: SamjnaInvocation,
            orderedTerms: List<String>,
            callerScope: ExecutionScope,
            callerSource: String?,
        ): ProcedureCallFrame {
            val remaining = invocation.arguments.toMutableList()
            val values = orderedTerms.map { term ->
                val normalized = term.substringBefore('+').trim()
                val matchIndex = remaining.indexOfFirst {
                    it.term.substringBefore('+').trim() == normalized
                }
                val argument = if (matchIndex >= 0) remaining.removeAt(matchIndex) else null
                argument?.value
                    ?: callerScope.environment.values[normalized]
                    ?: argument?.pada?.let(NumeralPadaBinder::resolveSemanticValue)
                    ?: runCatching { sankhyaEvaluator.evaluateStems(listOf(normalized)) }
                        .getOrNull()?.let { SanskritValue.Sankhya(it.value, normalized) }
                    ?: SanskritValue.Shabda(term)
            }
            val bindings = invocation.kriya.signature.parameters
                .zip(values)
                .associate { (parameter, value) -> parameter.nameStem to value }
            val referenceBindings = values.mapIndexed { index, value ->
                ProcedureAstArgumentBinder.referenceKey(index) to value
            }.toMap()
            return ProcedureCallFrame(
                parameterBindings = bindings,
                arguments = values,
                localScope = callerScope.copy(
                    environment = ValueEnvironment(callerScope.environment.values + bindings + referenceBindings),
                ),
                callerSource = callerSource,
            )
        }
    }
}
