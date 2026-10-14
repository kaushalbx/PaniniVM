package dev.panini.execution

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
            analysis: ProcedureBodyAnalysis = invocation.kriya.bodyAnalysis,
        ): PrakriyaCallFrame {
            val sourceValues = resolvedArguments.map { resolved ->
                resolved.semanticOperand().resolve(
                    lookup = { name -> callerScope.environment.values[name] ?: resolveValue(name) },
                    unresolved = { reference ->
                        runCatching { sankhyaEvaluator.evaluateStems(listOf(reference.name)) }
                            .getOrNull()?.let { SanskritValue.Sankhya(it.value, reference.name) }
                            ?: SanskritValue.Shabda(resolved.argument.term)
                    },
                )
            }
            val signature = analysis.signature
            val values = if (signature.hasTypedParticipants)
                TypedParticipantBinding.orderValues(signature.parameters.map { it.type }, sourceValues)
            else sourceValues
            val bindings = invocation.kriya.signature.parameters
                .zip(values)
                .associate { (parameter, value) -> parameter.nameStem to value }
            val localBindings = ProcedureLocalBindings.create(invocation.kriya.signature.parameters, values,
                callerScope.procedureBindings,
                analysis.parameterDeclarations)
            return PrakriyaCallFrame(
                parameterBindings = bindings,
                arguments = values,
                localScope = callerScope.copy(
                    environment = callerScope.environment.mergedWith(ValueEnvironment.live(localBindings.values)),
                    procedureBindings = localBindings,
                ),
            )
        }
    }
}
