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
    sealed interface NumericOperand {
        data class Argument(val index: Int) : NumericOperand
        data class Literal(val value: Long) : NumericOperand
    }

    data class NumericProhibition(val left: NumericOperand, val right: NumericOperand) {
        fun isProhibited(arguments: List<SanskritValue>): Boolean {
            fun resolve(operand: NumericOperand): Long? = when (operand) {
                is NumericOperand.Argument -> (arguments.getOrNull(operand.index) as? SanskritValue.Sankhya)?.value
                is NumericOperand.Literal -> operand.value
            }
            val leftValue = resolve(left) ?: return false
            val rightValue = resolve(right) ?: return false
            return leftValue == rightValue
        }
    }

    /** Typed numeric equality prohibition, retaining argument identity for backends. */
    fun numericProhibition(
        guard: PvmScriptStatement.Sentence,
        parameters: List<PrakriyaParameter>,
        argumentCount: Int,
    ): NumericProhibition? {
        val program = guard.program ?: return null
        val bound = PrakriyaAstArgumentBinder.bind(program, parameters, argumentCount)
        val padas = bound.invocations().flatMap { it.vakya.padas }
        if (padas.filterIsInstance<AvyayaPada>().none { it.function == AvyayaFunction.NISHEDHA }) return null
        val operands = padas.flatMap(::expand).filter(::isAccusative)
        if (operands.size != 2) return null
        fun operand(pada: Pada): NumericOperand? {
            if (pada is SubantaPada) {
                val key = pada.pratipadika.semanticKey()
                (0 until argumentCount).firstOrNull { PrakriyaAstArgumentBinder.referenceKey(it) == key }
                    ?.let { return NumericOperand.Argument(it) }
            }
            return NumeralPadaBinder.resolveSemanticValue(pada)?.value?.let(NumericOperand::Literal)
        }
        return NumericProhibition(operand(operands[0]) ?: return null, operand(operands[1]) ?: return null)
    }

    fun isProhibited(
        guard: PvmScriptStatement.Sentence,
        parameters: List<PrakriyaParameter>,
        argumentValues: List<SanskritValue>,
    ): Boolean {
        return numericProhibition(guard, parameters, argumentValues.size)?.isProhibited(argumentValues) == true
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

}
