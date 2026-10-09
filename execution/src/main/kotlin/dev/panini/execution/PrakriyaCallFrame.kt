package dev.panini.execution

import dev.panini.execution.binding.NumeralPadaBinder
import dev.panini.sankhya.SankhyaEvaluator

/** Runtime state owned by one reusable prakriyā call. */
data class PrakriyaCallFrame(
    val parameterBindings: Map<String, SanskritValue>,
    val arguments: List<SanskritValue>,
    val localScope: ExecutionScope,
) {
    companion object {
        private val sankhyaEvaluator = SankhyaEvaluator()

        /** Builds a call frame directly from grammar-resolved arguments without matching source fragments. */
        fun createResolved(
            invocation: PrakriyaInvocation,
            resolvedArguments: List<ResolvedPrakriyaArgument>,
            callerScope: ExecutionScope,
            resolveValue: (String) -> SanskritValue? = { null },
        ): PrakriyaCallFrame {
            val values = resolvedArguments.map { resolved ->
                val argument = resolved.argument
                val referenceName = resolved.referenceName
                argument.value
                    ?: callerScope.environment.values[referenceName]
                    ?: resolveValue(referenceName)
                    ?: argument.pada?.let(NumeralPadaBinder::resolveSemanticValue)
                    ?: runCatching { sankhyaEvaluator.evaluateStems(listOf(referenceName)) }
                        .getOrNull()?.let { SanskritValue.Sankhya(it.value, referenceName) }
                    ?: SanskritValue.Shabda(argument.term)
            }
            val bindings = invocation.kriya.signature.parameters
                .zip(values)
                .associate { (parameter, value) -> parameter.nameStem to value }
            val referenceBindings = values.mapIndexed { index, value ->
                PrakriyaAstArgumentBinder.referenceKey(index) to value
            }.toMap()
            return PrakriyaCallFrame(
                parameterBindings = bindings,
                arguments = values,
                localScope = callerScope.copy(
                    environment = ValueEnvironment(callerScope.environment.values + bindings + referenceBindings),
                ),
            )
        }
    }
}
