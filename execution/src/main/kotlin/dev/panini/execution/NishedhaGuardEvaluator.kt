package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.execution.binding.NumeralPadaBinder
import dev.panini.vyakaranam.ast.AvyayaFunction
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.SamuccitaSubanta
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.invocations
import dev.panini.vyakaranam.ast.semanticKey

/** Evaluates a निषेध sentence after binding its ordinal/named placeholders in the stored AST. */
object NishedhaGuardEvaluator {
    fun isProhibited(
        guard: PvmScriptStatement.Sentence,
        parameters: List<PrakriyaParameter>,
        argumentValues: List<SanskritValue>,
    ): Boolean {
        val program = guard.program ?: return false
        val bound = PrakriyaAstArgumentBinder.bind(program, parameters, argumentValues.size)
        val environment = argumentValues.mapIndexed { index, value ->
            PrakriyaAstArgumentBinder.referenceKey(index) to value
        }.toMap()
        val padas = bound.invocations().flatMap { it.vakya.padas }
        if (padas.filterIsInstance<AvyayaPada>().none { it.function == AvyayaFunction.NISHEDHA }) return false
        val operands = padas.flatMap(::expand)
            .filter(::isAccusative)
            .mapNotNull { numericValue(it, environment) }
        return operands.size == 2 && operands[0] == operands[1]
    }

    /** Compiler-facing guard evaluation that derives literals from retained argument ASTs. */
    fun isProhibitedResolved(
        guard: PvmScriptStatement.Sentence,
        parameters: List<PrakriyaParameter>,
        arguments: List<ResolvedPrakriyaArgument>,
        knownValues: List<SanskritValue?> = emptyList(),
    ): Boolean = isProhibited(
        guard,
        parameters,
        arguments.mapIndexed { index, resolved ->
            knownValues.getOrNull(index)
                ?: resolved.argument.value
                ?: resolved.argument.pada?.let(NumeralPadaBinder::resolveSemanticValue)
                ?: SanskritValue.Shabda(resolved.referenceName)
        },
    )

    private fun expand(pada: Pada): List<Pada> =
        if (pada is SamuccitaSubanta) pada.members else listOf(pada)

    private fun isAccusative(pada: Pada): Boolean {
        val sup = when (pada) {
            is SubantaPada -> pada.sup.text
            is dev.panini.vyakaranam.ast.SankhyaPada -> pada.sup.text
            is dev.panini.vyakaranam.ast.SankhyaPuranaPada -> pada.sup.text
            is dev.panini.vyakaranam.ast.KatapayadiPada -> pada.sup.text
            is dev.panini.vyakaranam.ast.AryabhatiyaPada -> pada.sup.text
            else -> return false
        }
        return SupAffix.fromUpadesha(sup)?.vibhakti == Vibhakti.DVITIYA
    }

    private fun numericValue(pada: Pada, environment: Map<String, SanskritValue>): Long? {
        if (pada is SubantaPada) {
            (environment[pada.pratipadika.semanticKey()] as? SanskritValue.Sankhya)?.let { return it.value }
        }
        return NumeralPadaBinder.resolveSemanticValue(pada)?.value
    }
}
