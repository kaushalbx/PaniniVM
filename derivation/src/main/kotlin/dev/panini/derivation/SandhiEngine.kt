package dev.panini.derivation

import dev.panini.core.Upasarga
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
        val carriers = state.terms + state.droppedTerms
        val trace = result.applications.mapTo(mutableSetOf()) { it.sutra }
        val isDerivedAvyayaPada = carriers.any { it.sourceTaddhitaAffix != null } &&
            "1.1.38" in trace && "1.4.14" in trace
        require(rupa.vibhakti != null || rupa.lakara != null || isDerivedAvyayaPada) {
            "A morphological pada is required, not an arbitrary sandhi phrase; " +
                "carriers=${carriers.map { it.upadesha to it.sourceTaddhitaAffix }} trace=$trace"
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
        finalBoundary: SpeechBoundary = SpeechBoundary.CONTINUATION,
        environments: Set<DerivationalEnvironment> = emptySet()): DerivationResult =
        engine.derive(padaBoundaryState(left, right, finalBoundary, environments), config)

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
        if (state.boundaryRuleBlocks.values.any { it in setOf("6.1.125", "6.1.127", "6.1.128", "8.3.19", "8.3.20") })
            state.terms.filter { it.surface.isNotEmpty() }.joinToString(" ") { it.surface }
        else state.surface

    private fun padaBoundaryState(
        left: Pada, right: Pada, finalBoundary: SpeechBoundary,
        environments: Set<DerivationalEnvironment> = emptySet(),
    ): DerivationState {
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
            context = DerivationalContext(finalBoundary = finalBoundary, environments = environments),
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
    /**
     * Joins an upasarga to the completed verbal form it is joined to (क्रियायोग); the form is
     * not derived again. Dissimilar vowels go through the sūtras with the prefix as उपसर्ग, so
     * 6.1.94 and 6.1.91 apply. 6.1.89 needs the root: pass [dhatu] (the dhātu the form was
     * derived from), otherwise उप + एति stays उपेति. "आ" is taken as the upasarga आङ्.
     */
    fun joinPrefix(left: String, right: String, dhatu: dev.panini.dhatupatha.Dhatu? = null): String {
        // निस् and दुस् take र् before a voiced sound (8.2.66, 8.3.15 does not apply): निर्हर, दुर्गम.
        // निस्, दुस्: the upasarga's final स् goes through the sūtras as any pada-final स् does
        // (8.2.66 रु; then र्, visarga, स्, or ष् by 8.3.41), so निर्गच्छ, निष्क्राम, नीरस, दुराचार.
        if (left.endsWith("स्") && Upasarga.fromUpadesha(left) != null) return join(
            Pada(DerivationTerm("sandhi_left", left, TermKind.PRATIPADIKA, upadesha = left), samjnas = setOf(Samjna.UPASARGA)),
            unspecifiedPada("sandhi_right", right)).final.surface
        var state = padaBoundaryState(left, right)
        val initialRight = state.terms.last().varnas.firstOrNull()
        val finalLeft = state.terms.first().varnas.lastOrNull()
        // With the root known, the whole join goes through the sūtras, so upasarga ṇatva (8.4.14) applies.
        // Only a vowel-final upasarga: सम्'s म् keeps the anusvāra handling below (संयोजय).
        // अन्तर् before हन् (8.4.24) also needs the sūtras.
        val antarHan = left == "अन्तर्" && dhatu?.upadesha == "हनँ"
        if (antarHan || (dhatu != null && finalLeft is dev.panini.shiksha.Svara && Upasarga.fromUpadesha(if (left == "आ") "आङ्" else left) != null) ||
            (finalLeft is dev.panini.shiksha.Svara && initialRight is dev.panini.shiksha.Svara && !SavarnaDirghaSutra.matches(state))) {
            // The sūtras decide: 6.1.94 (उप + एजते → उपेजते), 6.1.91 (प्र + ऋच्छति → प्रार्च्छति),
            // 6.1.89 when the root is known (उप + एति → उपैति), else yaṇ, guṇa, vṛddhi as across any words.
            val upadesha = if (left == "आ") "आङ्" else left
            val prefix = Pada(DerivationTerm("sandhi_left", left, TermKind.PRATIPADIKA, upadesha = upadesha),
                samjnas = if (Upasarga.fromUpadesha(upadesha) != null) setOf(Samjna.UPASARGA) else emptySet())
            val verb = Pada(DerivationTerm("sandhi_right", right.trim(), TermKind.PRATIPADIKA,
                upadesha = right.trim(), sourceDhatuId = dhatu?.id))
            return engine.derive(padaBoundaryState(prefix, verb, SpeechBoundary.CONTINUATION,
                setOf(DerivationalEnvironment.KRIYAYOGA)), DerivationConfig()).final.surface
        }
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

    /**
     * Joins two compound members. Unless [leftIsUttarapada], the left word is a pūrvapada not in an
     * uttarapada, which 8.3.45–8.3.47 need (सर्पिष्कुण्डिका, अयस्कुम्भः, अधस्पदम्).
     */
    fun joinSamasaMembers(left: String, right: String, leftIsUttarapada: Boolean = false,
        environments: Set<DerivationalEnvironment> = emptySet()): DerivationResult =
        engine.derive(padaBoundaryState(unspecifiedPada("sandhi_left", left), unspecifiedPada("sandhi_right", right),
            SpeechBoundary.CONTINUATION,
            environments + (if (leftIsUttarapada) emptySet() else setOf(DerivationalEnvironment.SAMASA_PURVAPADA))), DerivationConfig())

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
