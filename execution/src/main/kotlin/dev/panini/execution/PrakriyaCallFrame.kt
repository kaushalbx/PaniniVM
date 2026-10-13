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
        ): PrakriyaCallFrame {
            val sourceValues = resolvedArguments.map { resolved ->
                val argument = resolved.argument
                val referenceName = resolved.referenceName
                argument.value
                    ?: argument.pada?.let(PrakriyaValueClassifier::literalValue)
                    ?: callerScope.environment.values[referenceName]
                    ?: resolveValue(referenceName)
                    ?: argument.pada?.let(NominalValueAnalysis::fallbackValue)
                    ?: runCatching { sankhyaEvaluator.evaluateStems(listOf(referenceName)) }
                        .getOrNull()?.let { SanskritValue.Sankhya(it.value, referenceName) }
                    ?: SanskritValue.Shabda(argument.term)
            }
            val signature = invocation.kriya.signature
            val values = if (signature.hasTypedParticipants)
                TypedParticipantBinding.orderValues(signature.parameters.map { it.type }, sourceValues)
            else sourceValues
            val bindings = invocation.kriya.signature.parameters
                .zip(values)
                .associate { (parameter, value) -> parameter.nameStem to value }
            val localBindings = ProcedureLocalBindings.create(invocation.kriya.signature.parameters, values,
                callerScope.procedureBindings, invocation.kriya.body
                    .filterIsInstance<PvmScriptStatement.Sentence>().mapNotNull { sentence ->
                        val parameter = PrakriyaSignatureDeclarationParser.parameter(sentence) ?: return@mapNotNull null
                        PrakriyaSignatureDeclarationParser.parameterDeclaration(sentence)?.let { parameter.nameStem to it }
                    }.toMap() + if (signature.hasTypedParticipants)
                    TypedParticipantAnalysis.declarations(invocation.kriya.body) else emptyMap())
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
