package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.execution.binding.NumeralPadaBinder
import dev.panini.sankhya.SankhyaEvaluator
import dev.panini.vyakaranam.ast.AvyayaFunction
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.SamuccitaSubanta
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.invocations

/** Evaluates a निषेध sentence after binding its ordinal/named placeholders in the stored AST. */
object NishedhaGuardEvaluator {
    private val sankhyaEvaluator = SankhyaEvaluator()

    fun isProhibited(
        guard: PvmScriptStatement.Sentence,
        parameters: List<PrakriyaParameter>,
        argumentTerms: List<String>,
        argumentValues: List<SanskritValue?> = emptyList(),
    ): Boolean {
        val program = guard.program ?: return false
        val bound = PrakriyaAstArgumentBinder.bind(program, parameters, argumentTerms.size)
        val values = argumentTerms.mapIndexed { index, term ->
            argumentValues.getOrNull(index) ?: materialize(term)
        }
        val environment = values.mapIndexed { index, value ->
            PrakriyaAstArgumentBinder.referenceKey(index) to value
        }.toMap()
        val padas = bound.invocations().flatMap { it.vakya.padas }
        if (padas.filterIsInstance<AvyayaPada>().none { it.function == AvyayaFunction.NISHEDHA }) return false
        val operands = padas.flatMap(::expand)
            .filter(::isAccusative)
            .mapNotNull { numericValue(it, environment) }
        return operands.size == 2 && operands[0] == operands[1]
    }

    private fun materialize(term: String): SanskritValue {
        val normalized = term.substringBefore('+').trim()
        return runCatching { sankhyaEvaluator.evaluateStems(listOf(normalized)) }.getOrNull()
            ?.let { SanskritValue.Sankhya(it.value, normalized) }
            ?: SanskritValue.Shabda(term)
    }

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
            (environment[pada.pratipadika.sourceText] as? SanskritValue.Sankhya)?.let { return it.value }
        }
        return NumeralPadaBinder.resolveSemanticValue(pada)?.value
    }
}
