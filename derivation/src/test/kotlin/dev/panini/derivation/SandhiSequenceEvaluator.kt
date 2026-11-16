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

    class Outcome(
        val padas: List<SandhiEngine.Pada>,
        val hiatusBefore: Set<String> = emptySet(),
        /** Original input carriers retain every selected nominal proof across later boundaries. */
        val inputs: List<SandhiEngine.Pada> = emptyList(),
        /** Index of the chosen boundary result at each join; the proof is re-derived from it. */
        internal val trail: List<Int> = emptyList(),
        private val replay: ((Outcome) -> List<DerivationResult>)? = null,
    ) {
        /** Every boundary derivation, re-derived on demand so live branches hold no step states. */
        val steps: List<DerivationResult> get() = replay?.invoke(this).orEmpty()
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
            val plain = SandhiEngine.Pada(DerivationTerm(id, word, TermKind.PRATIPADIKA, upadesha = word))
            // A plain reading infers nothing when the word does not end in visarga, so a proven
            // analysis (a dual name मृगी, pragṛhya by 1.1.11) adds to it and never hides the ordinary word.
            val keepsPlain = plain.term.varnas.lastOrNull() !is dev.panini.shiksha.Ayogavaha
            if (candidates.isEmpty()) listOf(plain)
            else (if (keepsPlain) listOf(plain) else emptyList()) + candidates.map { candidate ->
                engine.padaFromDerivation(candidate.derivation, id).copy(visargaSource = candidate.pada.visargaSource)
            // Analyses with the same varṇas, rūpa, saṃjñās and visarga source meet every boundary
            // rule alike; one stands for all, so ambiguity does not multiply branches.
            }.distinctBy { listOf(it.term.varnas, it.rupa, it.samjnas, it.visargaSource) }
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
        fun joins(left: SandhiEngine.Pada, right: SandhiEngine.Pada, endpoint: SpeechBoundary) =
            if (alternatives) engine.joinAll(left, right, endpoint) else listOf(engine.join(left, right, finalBoundary = endpoint))
        val replay: (Outcome) -> List<DerivationResult> = { outcome ->
            var left = outcome.inputs.first()
            outcome.trail.mapIndexed { step, choice ->
                val right = outcome.inputs[step + 1]
                val endpoint = if (step + 1 == inputs.lastIndex) finalBoundary else SpeechBoundary.CONTINUATION
                joins(left, right, endpoint)[choice].also { left = survivorsOf(left, right, it).last() }
            }.also {
                check(left.term == outcome.padas.last().term) { "Replayed proof diverged from its branch" }
            }
        }
        var outcomes = inputs.first().map { Outcome(listOf(it), inputs = listOf(it), replay = replay) }
        check(outcomes.size <= maxOutcomes) { "Corpus alternative limit exceeded ($maxOutcomes); no branches silently discarded" }
        for (index in 1..inputs.lastIndex) {
            val endpoint = if (index == inputs.lastIndex) finalBoundary else SpeechBoundary.CONTINUATION
            // Branches that reach the same padas (full term state, rūpa, saṃjñās, visarga source) and
            // hiatus continue identically; the first proof stands for them. An option declined at one
            // boundary and taken at the next (8.4.47 in an earlier word) otherwise repeats a branch.
            val next = LinkedHashMap<List<Any?>, Outcome>()
            for (outcome in outcomes) for (right in inputs[index]) {
                val left = outcome.padas.last()
                val results = joins(left, right, endpoint)
                for ((choice, result) in results.withIndex()) {
                    check(result.final.stage == DerivationStage.FINAL) { "Incomplete corpus boundary derivation" }
                    val survivors = survivorsOf(left, right, result)
                    check(survivors.isNotEmpty()) { "Sandhi consumed all participating padas" }
                    var gaps = outcome.hiatusBefore - left.term.id
                    if (left.term.id in outcome.hiatusBefore) gaps += survivors.first().term.id
                    if (survivors.size > 1 && result.final.boundaryRuleBlocks.values.any {
                        it in setOf("6.1.125", "6.1.127", "6.1.128", "8.3.19", "8.3.20")
                    }) gaps += survivors.last().term.id
                    val padas = outcome.padas.dropLast(1) + survivors
                    val key = padas.map { listOf(it.term, it.rupa, it.samjnas, it.visargaSource) } + listOf(gaps)
                    next.putIfAbsent(key, Outcome(padas, gaps, outcome.inputs + right, outcome.trail + choice, replay))
                    check(next.size <= maxOutcomes) { "Corpus alternative limit exceeded ($maxOutcomes); no branches silently discarded" }
                }
            }
            outcomes = next.values.toList()
        }
        return outcomes
    }

    private fun survivorsOf(left: SandhiEngine.Pada, right: SandhiEngine.Pada, result: DerivationResult) =
        result.final.terms.filter { it.varnas.isNotEmpty() }.map { term ->
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
}
