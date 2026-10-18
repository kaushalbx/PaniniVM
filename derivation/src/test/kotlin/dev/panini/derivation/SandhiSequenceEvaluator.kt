package dev.panini.derivation

import dev.panini.shiksha.SanskritText
import dev.panini.shiksha.renderWithOrthographicSigns

/**
 * Corpus evaluation policy: resolve adjacent completed padas from left to right.
 * Only the surviving rightmost pada participates in the next boundary. Earlier
 * padas remain distinct; a rendered phrase must never become a single input pada.
 * This is an evaluation policy, not a claim of exhaustive sentence derivation.
 */
internal class SandhiSequenceEvaluator(private val engine: SandhiEngine = SandhiEngine(),
    private val lexicon: DerivedVisargaLexicon? = null) {
    class InvalidPadaInput(val wordIndex: Int, val word: String, cause: Exception) :
        IllegalArgumentException("Input pada ${wordIndex + 1} '$word' cannot be parsed: ${cause.message}", cause)

    data class Outcome(
        val padas: List<SandhiEngine.Pada>,
        val hiatusBefore: Set<String> = emptySet(),
        val steps: List<DerivationResult> = emptyList(),
        /** Original input carriers retain every selected nominal proof across later boundaries. */
        val inputs: List<SandhiEngine.Pada> = emptyList(),
    ) {
        val surface: String get() {
            val groups = mutableListOf<MutableList<SandhiEngine.Pada>>()
            for (pada in padas) {
                if (groups.isEmpty() || pada.term.id in hiatusBefore) groups += mutableListOf<SandhiEngine.Pada>()
                groups.last() += pada
            }
            return groups.joinToString(" ") { group ->
                var offset = 0
                val signs = group.flatMap { pada ->
                    val term = pada.term
                    val projected = (term.orthographicSigns + term.phonologicalText.sourceOrthographicSigns)
                        .distinct().map { it.copy(afterVarnaCount = it.afterVarnaCount + offset) }
                    offset += term.varnas.size
                    projected
                }
                SanskritText(group.flatMap { it.term.phonologicalText.effectiveVarnas })
                    .renderWithOrthographicSigns(signs)
            }
        }
    }

    fun evaluate(words: List<String>, alternatives: Boolean = false, maxOutcomes: Int = 4096,
        finalBoundary: SpeechBoundary = SpeechBoundary.CONTINUATION): List<Outcome> =
        evaluateCandidates(words.mapIndexed { index, word ->
            // Validate only at the corpus input boundary. Never strip commentary,
            // retry with a modified spelling, or relabel a derivation exception.
            try {
                SanskritText.parse(word)
            } catch (failure: IllegalStateException) {
                throw InvalidPadaInput(index, word, failure)
            } catch (failure: IllegalArgumentException) {
                throw InvalidPadaInput(index, word, failure)
            }
            val id = "corpus_pada_$index"
            val candidates = lexicon?.padaCandidates(word).orEmpty()
            if (candidates.isEmpty()) listOf(SandhiEngine.Pada(DerivationTerm(id, word, TermKind.PRATIPADIKA, upadesha = word)))
            else candidates.map { candidate ->
                engine.padaFromDerivation(candidate.derivation, id).copy(visargaSource = candidate.pada.visargaSource)
            }
        }, alternatives, maxOutcomes, finalBoundary)

    fun evaluatePadas(
        inputs: List<SandhiEngine.Pada>, alternatives: Boolean = false, maxOutcomes: Int = 4096,
        finalBoundary: SpeechBoundary = SpeechBoundary.CONTINUATION,
    ): List<Outcome> = evaluateCandidates(inputs.map { listOf(it) }, alternatives, maxOutcomes, finalBoundary)

    private fun evaluateCandidates(inputs: List<List<SandhiEngine.Pada>>, alternatives: Boolean,
        maxOutcomes: Int, finalBoundary: SpeechBoundary): List<Outcome> {
        require(inputs.size >= 2 && inputs.all { choices -> choices.isNotEmpty() && choices.all { it.term.surface.isNotBlank() } })
        require(inputs.all { choices -> choices.map { it.term.id }.distinct().size == 1 })
        require(inputs.map { it.first().term.id }.distinct().size == inputs.size)
        require(maxOutcomes > 0)
        var outcomes = inputs.first().map { Outcome(listOf(it), inputs = listOf(it)) }
        check(outcomes.size <= maxOutcomes) { "Corpus alternative limit exceeded ($maxOutcomes); no branches silently discarded" }
        for (index in 1..inputs.lastIndex) {
            val endpoint = if (index == inputs.lastIndex) finalBoundary else SpeechBoundary.CONTINUATION
            val next = mutableListOf<Outcome>()
            for (outcome in outcomes) for (right in inputs[index]) {
                val left = outcome.padas.last()
                val results = if (alternatives) engine.joinAll(left, right, endpoint)
                    else listOf(engine.join(left, right, finalBoundary = endpoint))
                for (result in results) {
                    check(result.final.stage == DerivationStage.FINAL) { "Incomplete corpus boundary derivation" }
                    val survivors = result.final.terms.filter { it.varnas.isNotEmpty() }.map { term ->
                        val endingOrigins = term.completedPadaOrigins.lastOrNull().orEmpty()
                        val sources = listOf(left, right).filter { input ->
                            input.term.id in endingOrigins &&
                                term.varnas.lastOrNull() is dev.panini.shiksha.Ayogavaha &&
                                term.varnas.lastOrNull() == input.term.varnas.lastOrNull()
                        }.mapNotNull { it.visargaSource }.distinct()
                        SandhiEngine.Pada(term, term.formedPadaRupa ?: Rupa(), result.final.samjnas
                            .filter { it.targetId == term.id }.map { it.samjna }.toSet(),
                            visargaSource = sources.singleOrNull())
                    }
                    check(survivors.isNotEmpty()) { "Sandhi consumed all participating padas" }
                    var gaps = outcome.hiatusBefore - left.term.id
                    if (left.term.id in outcome.hiatusBefore) gaps += survivors.first().term.id
                    if (survivors.size > 1 && result.final.boundaryRuleBlocks.values.any {
                        it in setOf("6.1.125", "8.3.19", "8.3.20")
                    }) gaps += survivors.last().term.id
                    next += Outcome(outcome.padas.dropLast(1) + survivors, gaps, outcome.steps + result, outcome.inputs + right)
                    check(next.size <= maxOutcomes) { "Corpus alternative limit exceeded ($maxOutcomes); no branches silently discarded" }
                }
            }
            outcomes = next
        }
        return outcomes
    }
}
