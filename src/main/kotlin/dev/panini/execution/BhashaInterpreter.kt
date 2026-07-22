package dev.panini.execution

import dev.panini.core.Karaka

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
                bindings[Karaka.KARTR] = ExecutionExpression.Pada(context.listener)
            }
            invocation.copy(bindings = bindings)
        }
        return UktiInterpretation.Understood(
            ukti.copy(invocations = resolvedInvocations),
            listOf(
                "Resolved speaker ${ukti.speaker} and listener ${ukti.listener}.",
                "Interpreted purpose as ${ukti.prayojana} with ${ukti.polarity} polarity.",
            ),
        )
    }
}
