package dev.panini.derivation

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.ashtadhyayi.adhyaya6.pada1.SavarnaDirghaSutra
import dev.panini.ashtadhyayi.adhyaya8.pada2.JhalamJashonteSutra
import dev.panini.ashtadhyayi.adhyaya8.pada3.MonusvarahSutra
import dev.panini.ashtadhyayi.adhyaya8.pada4.JhayoHonyatarasyamSutra
import dev.panini.ashtadhyayi.adhyaya8.pada4.AnusvarasyaYayiParasavarnahSutra
import dev.panini.ashtadhyayi.adhyaya8.pada4.KhariCaSutra
import dev.panini.shiksha.Varnamala
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Samjna

/** Applies the implemented external-sandhi rules to two fully formed padas. */
class SandhiEngine(
    private val engine: DerivationEngine = DerivationEngine(Ashtadhyayi.executableSutras)
) {
    /** Apply savarṇa vowel and consonant prefix-boundary sandhi.
     * Completed verbal forms must not undergo a new full derivation. This
     * boundary helper does not yet implement the full vowel-sandhi inventory.
     */
    fun joinPrefix(left: String, right: String): String {
        var state = padaBoundaryState(left, right)
        if (SavarnaDirghaSutra.matches(state)) {
            state = SavarnaDirghaSutra.apply(state).state
        }
        if (MonusvarahSutra.matches(state)) state = MonusvarahSutra.apply(state).state
        // Select a written homorganic nasal before a varga consonant. Keep
        // anusvāra before semivowels; the general rule's fallback is not a
        // representation of their nasalized phonetic variants.
        if (right.firstOrNull()?.let { Varnamala.getVargaInfo(it) } != null &&
            AnusvarasyaYayiParasavarnahSutra.matches(state)) {
            state = AnusvarasyaYayiParasavarnahSutra.apply(state).state
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
    ): DerivationResult {
        require(left.isNotBlank() && right.isNotBlank()) { "Two words are required for sandhi." }

        val initial = padaBoundaryState(left, right)
        return engine.derive(initial, config)
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
        // Readable sentence rendering currently licenses these two mandatory
        // external operations. Applying the whole derivational catalogue here
        // would reopen the already completed internal phonology of each pada.
        val follower = right.trim().firstOrNull()
        val jashEnvironment = follower != null &&
            Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.ASH, follower)
        if (left.trim().endsWith("त्") && jashEnvironment && JhalamJashonteSutra.matches(leftState)) {
            leftState = JhalamJashonteSutra.apply(leftState).state
        }
        var state = padaBoundaryState(leftState.terms.single().surface, right)
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
        )
    }
}
