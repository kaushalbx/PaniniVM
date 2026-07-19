package dev.panini.execution

object BhashaInterpreter {
    fun interpret(ukti: Ukti, context: SambhashanaContext): UktiInterpretation {
        if (ukti.speaker != context.speaker || ukti.listener != context.listener) {
            return UktiInterpretation.Contradictory(
                "Utterance participants do not agree with the trusted conversation context.",
            )
        }

        val resolvedInvocations = ukti.invocations.map { invocation ->
            val bindings = invocation.bindings.toMutableMap()
            if (ukti.prayojana in setOf(VakyaPrayojana.AJNA, VakyaPrayojana.PRARTHANA) &&
                Karaka.KARTR !in bindings
            ) {
                bindings[Karaka.KARTR] = ExecutionExpression.Literal(context.listener)
            }
            invocation.copy(bindings = bindings)
        }
        val nirdesha = Nirdesha(
            ukti.speaker,
            ukti.listener,
            ukti.prayojana,
            ukti.polarity,
            ukti.lakara,
            resolvedInvocations,
            ukti.text,
        )
        return UktiInterpretation.Understood(
            nirdesha,
            listOf(
                "Resolved speaker ${ukti.speaker} and listener ${ukti.listener}.",
                "Interpreted purpose as ${ukti.prayojana} with ${ukti.polarity} polarity.",
            ),
        )
    }
}

object DispositionResolver {
    fun resolve(nirdesha: Nirdesha): ExecutionDisposition = when (nirdesha.prayojana) {
        VakyaPrayojana.AJNA -> if (nirdesha.polarity == Polarity.NEGATIVE) ExecutionDisposition.CONSTRAIN else ExecutionDisposition.EXECUTE
        VakyaPrayojana.PRARTHANA -> if (nirdesha.polarity == Polarity.NEGATIVE) ExecutionDisposition.CONSTRAIN else ExecutionDisposition.REQUEST_EXECUTION
        VakyaPrayojana.PRASHNA -> ExecutionDisposition.QUERY
        VakyaPrayojana.VIDHANA -> ExecutionDisposition.DECLARE
        VakyaPrayojana.NISHEDHA -> ExecutionDisposition.CONSTRAIN
        VakyaPrayojana.ASHAMSA -> ExecutionDisposition.DESIRE
        VakyaPrayojana.ANUMATI -> ExecutionDisposition.GRANT
        VakyaPrayojana.NIMANTRANA -> ExecutionDisposition.OFFER
    }
}
