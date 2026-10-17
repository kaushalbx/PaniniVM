package dev.panini.execution

import dev.panini.vyakaranam.ast.ProgramNode
import dev.panini.vyakaranam.ast.SubantaPada

/** Immutable grammatical facts owned by one reusable procedure definition. */
data class ProcedureBodyAnalysis(
    val signature: PrakriyaSignature,
    val parameterDeclarations: Map<String, SubantaPada>,
    val executableBodies: List<ProgramNode>,
) {
    companion object {
        fun analyze(prakriya: Prakriya): ProcedureBodyAnalysis {
            val signature = prakriya.signature
            return ProcedureBodyAnalysis(
                signature = signature,
                parameterDeclarations = PrakriyaParameterDeclarations.resolve(signature.parameters, prakriya.body),
                executableBodies = prakriya.vidhiSentences.map { sentence ->
                    requireNotNull(sentence.program) {
                        "A reusable प्रक्रिया body must contain a parsed Sanskrit program: '${sentence.text}'."
                    }
                },
            )
        }
    }
}
