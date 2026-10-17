package dev.panini.derivation

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.ashtadhyayi.adhyaya6.pada1.SavarnaDirghaSutra
import dev.panini.ashtadhyayi.adhyaya8.pada2.JhalamJashonteSutra
import dev.panini.ashtadhyayi.adhyaya8.pada3.MonusvarahSutra
import dev.panini.ashtadhyayi.adhyaya8.pada4.JhayoHonyatarasyamSutra
import dev.panini.ashtadhyayi.adhyaya8.pada4.VaPadantasyaSutra
import dev.panini.ashtadhyayi.adhyaya8.pada4.KhariCaSutra
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.AbhyantaraPrayatna
import dev.panini.shiksha.Vyanjana

/** Applies the implemented external-sandhi rules to two fully formed padas. */
class SandhiEngine(
    private val engine: DerivationEngine = DerivationEngine(Ashtadhyayi.executableSutras)
) {
    /** Explicit completed-word input; spelling alone cannot identify a dual or a vocative. */
    data class Pada(val term: DerivationTerm, val rupa: Rupa = Rupa(), val samjnas: Set<Samjna> = emptySet(),
        val visargaSource: VisargaSource? = null,
        val sourceDerivation: DerivationResult? = null)

    /** A completed morphological word may still have several internal term carriers.
     * Compose their exact tokens at the input boundary and retain the complete source result. */
    fun padaFromDerivation(result: DerivationResult, id: String): Pada {
        require(id.isNotBlank())
        val state = result.final
        require(state.stage == DerivationStage.FINAL) { "Sandhi requires a completed word derivation." }
        val rupa = state.effectiveContext.rupa
        require(rupa.vibhakti != null || rupa.lakara != null) {
            "A morphological pada is required, not an arbitrary sandhi phrase."
        }
        state.requireCompleteItProcessing()
        val terms = state.terms
        val rootIndex = terms.indexOfFirst { it.kind == TermKind.DHATU }
        val headIndex = if (rootIndex >= 0) rootIndex else terms.indexOfFirst { term ->
            term.kind == TermKind.PRATIPADIKA && state.samjnas.none {
                it.targetId == term.id && it.samjna == Samjna.UPASARGA
            }
        }
        require(headIndex >= 0) { "A morphological head carrier is required." }
        var carrier = terms[headIndex]
        for (prefix in terms.take(headIndex).asReversed()) carrier = carrier.insertDesignatedTerm(prefix, 0)
        for (suffix in terms.drop(headIndex + 1)) carrier = carrier.insertDesignatedTerm(suffix, carrier.varnas.size)
        // Internal root/prefix/affix classifications remain in sourceDerivation;
        // they must not classify the whole completed word as a fresh root or prefix.
        val samjnas = state.samjnas.filter { it.samjna in setOf(Samjna.PADA, Samjna.PRAGRHYA) }
            .map { it.samjna }.toSet()
        return Pada(carrier.copy(id = id, kind = TermKind.PRATIPADIKA, formedPadaRupa = rupa),
            rupa, samjnas, sourceDerivation = result)
    }

    fun join(left: Pada, right: Pada, config: DerivationConfig = DerivationConfig(),
        finalBoundary: SpeechBoundary = SpeechBoundary.CONTINUATION): DerivationResult =
        engine.derive(padaBoundaryState(left, right, finalBoundary), config)

    fun joinAll(left: Pada, right: Pada, finalBoundary: SpeechBoundary = SpeechBoundary.CONTINUATION): List<DerivationResult> =
        engine.deriveAll(padaBoundaryState(left, right, finalBoundary))

    /** Completed padas with unspecified morphology still license external boundary rules. */
    fun joinAll(left: String, right: String, finalBoundary: SpeechBoundary = SpeechBoundary.CONTINUATION): List<DerivationResult> =
        joinAll(unspecifiedPada("sandhi_left", left), unspecifiedPada("sandhi_right", right), finalBoundary)

    private fun unspecifiedPada(id: String, surface: String): Pada {
        require(surface.isNotBlank()) { "Two words are required for sandhi." }
        return Pada(DerivationTerm(id, surface.trim(), TermKind.PRATIPADIKA, upadesha = surface.trim()))
    }

    /** Hiatus is rendered as a word boundary, not as an invented phonological sign. */
    fun render(result: DerivationResult): String = render(result.final)

    /** The same orthographic rendering applies to a completed step and the final result. */
    fun render(state: DerivationState): String =
        if (state.boundaryRuleBlocks.values.any { it in setOf("6.1.125", "8.3.19", "8.3.20") })
            state.terms.filter { it.surface.isNotEmpty() }.joinToString(" ") { it.surface }
        else state.surface

    private fun padaBoundaryState(left: Pada, right: Pada, finalBoundary: SpeechBoundary): DerivationState {
        require(left.term.id != right.term.id) { "External padas must have distinct term identities." }
        require(left.term.surface.isNotBlank() && right.term.surface.isNotBlank())
        val padas = listOf(left, right)
        val state = DerivationState(
            terms = padas.map { it.term.copy(formedPadaRupa = it.rupa,
                completedPadaOrigins = it.term.completedPadaOrigins.ifEmpty { List(it.term.varnas.size) { _ -> setOf(it.term.id) } }) },
            samjnas = padas.flatMap { pada ->
                (pada.samjnas + Samjna.PADA).map { SamjnaAssignment(pada.term.id, it) }
            }.toSet(),
            stage = DerivationStage.PADA_FORMED,
            context = DerivationalContext(finalBoundary = finalBoundary),
        )
        // The left environment is known. Decode the right word only at an
        // explicit pause; continuation supplies no following sound for it.
        val active = if (finalBoundary == SpeechBoundary.AVASANA) padas else listOf(left)
        return active.fold(state) { prepared, pada ->
            val source = pada.visargaSource ?: VisargaSource.ESTABLISHED_RU.takeIf {
                pada.term.varnas.lastOrNull() is dev.panini.shiksha.Ayogavaha &&
                    pada.term.hasRutvaAt(pada.term.varnas.lastIndex)
            }
            source?.let { prepared.withCompletedPadaVisargaSource(pada.term.id, it) } ?: prepared
        }
    }
    /** Apply savarṇa vowel and consonant prefix-boundary sandhi.
     * Completed verbal forms must not undergo a new full derivation. This
     * boundary helper does not yet implement the full vowel-sandhi inventory.
     */
    fun joinPrefix(left: String, right: String): String {
        var state = padaBoundaryState(left, right)
        val initialRight = state.terms.last().varnas.firstOrNull()
        if (SavarnaDirghaSutra.matches(state)) {
            state = SavarnaDirghaSutra.apply(state).state
        }
        if (MonusvarahSutra.matches(state)) state = MonusvarahSutra.apply(state).state
        // Select a written homorganic nasal before a varga consonant. Keep
        // anusvāra before semivowels; the general rule's fallback is not a
        // representation of their nasalized phonetic variants.
        if (initialRight is Vyanjana && initialRight.abhyantaraPrayatna == AbhyantaraPrayatna.SPRSTA &&
            VaPadantasyaSutra.matches(state)) {
            state = VaPadantasyaSutra.apply(state).state
        }
        if (JhayoHonyatarasyamSutra.matches(state)) {
            state = JhayoHonyatarasyamSutra.apply(state).state
        }
        return state.terms.joinToString("") { it.surface }
    }

    fun join(
        left: String,
        right: String,
        config: DerivationConfig = DerivationConfig(),
        finalBoundary: SpeechBoundary = SpeechBoundary.CONTINUATION,
    ): DerivationResult {
        require(left.isNotBlank() && right.isNotBlank()) { "Two words are required for sandhi." }

        return join(unspecifiedPada("sandhi_left", left), unspecifiedPada("sandhi_right", right), config, finalBoundary)
    }

    /** Completed consonant-final compound members: apply only boundary phonology. */
    fun joinConsonantBoundary(left: String, right: String): DerivationResult {
        val initial = padaBoundaryState(left, right)
        var state = initial
        val applications = mutableListOf<DerivationApplication>()
        fun record(rule: dev.panini.sutra.Sutra<*, *>, after: DerivationState, explanation: String) {
            val recorded = after.copy(appliedSutras = state.appliedSutras + rule.number)
            applications.add(DerivationApplication(rule.number, rule.role, rule.action, rule.scope,
                rule.text, state, recorded, explanation))
            state = recorded
        }
        val isolated = singlePadaState(left)
        if (JhalamJashonteSutra.matches(isolated)) {
            val change = JhalamJashonteSutra.apply(isolated)
            val beforeTerm = state.terms.first()
            val afterTerm = change.state.terms.single()
            val after = state.substituteTermVarnas(beforeTerm.id, afterTerm.varnas,
                beforeTerm.varnas.last(), listOf(afterTerm.varnas.last()), JhalamJashonteSutra.sutra)
            record(JhalamJashonteSutra, after, change.explanation)
        }
        if (KhariCaSutra.matches(state)) {
            val change = KhariCaSutra.apply(state)
            record(KhariCaSutra, change.state, change.explanation)
        }
        return DerivationResult(initial, state, applications,
            applications.map { DerivationEvent.RuleApplied(it.sutra, it.before, it.after, it.explanation) } +
                DerivationEvent.Completed(state, applications.size))
    }

    /**
     * Applies external sandhi while retaining the pada boundary in the rendered text.
     * The ordinary [join] result concatenates its terms because that is useful for
     * derivational surfaces; sentence rendering instead needs the transformed padas.
     */
    fun joinPadas(left: String, right: String): String {
        var leftState = singlePadaState(left)
        var state = padaBoundaryState(left, right)
        // Readable sentence rendering currently licenses these two mandatory
        // external operations. Applying the whole derivational catalogue here
        // would reopen the already completed internal phonology of each pada.
        val follower = state.terms.last().varnas.firstOrNull()
        val jashEnvironment = follower != null &&
            Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.ASH, follower)
        if (leftState.terms.single().varnas.lastOrNull() == Vyanjana.TA &&
            jashEnvironment && JhalamJashonteSutra.matches(leftState)) {
            leftState = JhalamJashonteSutra.apply(leftState).state
        }
        state = state.replaceTerm(leftState.terms.single().id, leftState.terms.single())
        if (MonusvarahSutra.matches(state)) state = MonusvarahSutra.apply(state).state
        return state.terms
            .map { it.surface }
            .filter { it.isNotBlank() }
            .joinToString(" ")
    }

    private fun singlePadaState(surface: String): DerivationState {
        val term = DerivationTerm("sandhi_left", surface.trim(), TermKind.PRATIPADIKA, upadesha = surface.trim())
        return DerivationState(
            terms = listOf(term),
            samjnas = setOf(SamjnaAssignment(term.id, Samjna.PADA)),
            stage = DerivationStage.PADA_FORMED,
        )
    }

    private fun padaBoundaryState(left: String, right: String): DerivationState {
        require(left.isNotBlank() && right.isNotBlank()) { "Two words are required for sandhi." }
        val leftTerm = DerivationTerm("sandhi_left", left.trim(), TermKind.PRATIPADIKA, upadesha = left.trim())
        val rightTerm = DerivationTerm("sandhi_right", right.trim(), TermKind.PRATIPADIKA, upadesha = right.trim())
        return DerivationState(
            terms = listOf(leftTerm, rightTerm),
            samjnas = setOf(
                SamjnaAssignment(leftTerm.id, Samjna.PADA),
                SamjnaAssignment(rightTerm.id, Samjna.PADA),
            ),
            stage = DerivationStage.PADA_FORMED,
            context = DerivationalContext(finalBoundary = SpeechBoundary.CONTINUATION),
        )
    }
}
