package dev.panini.derivation

import dev.panini.analysis.KarakaResolution
import dev.panini.analysis.SamasaResolution
import dev.panini.core.SupAffix
import dev.panini.core.TingAffix
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraPriority
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType
import dev.panini.sutra.SutraVisibility
import dev.panini.shiksha.Samjna

data class DerivationApplication(
    val sutra: String,
    val role: SutraRole,
    val action: SutraAction,
    val scope: SutraScope,
    val trace: String,
    val before: DerivationState,
    val after: DerivationState,
    val explanation: String,
    val conflictTrace: List<String> = emptyList(),
    /** Explicit noncanonical ownership remains available without interpreting the trace ID. */
    val source: dev.panini.sutra.runtime.SutraSource? = null,
) {
    val delta: DerivationDelta = DerivationDelta.between(before, after)
}

sealed interface DerivationEvent {
    data class BranchCreated(val sutra: String, val branchCount: Int) : DerivationEvent
    data class RuleConsidered(val sutra: String, val rank: Int = 0, val status: String = "Eligible") : DerivationEvent
    data class RuleBlocked(val sutra: String, val blocker: String, val reason: String = "Blocked by grammar.") : DerivationEvent
    data class RuleApplied(val sutra: String, val before: DerivationState, val after: DerivationState, val explanation: String) : DerivationEvent
    data class Completed(val finalState: DerivationState, val applicationCount: Int) : DerivationEvent
}

data class DerivationResult(
    val initial: DerivationState,
    val final: DerivationState,
    val applications: List<DerivationApplication>,
    val events: List<DerivationEvent>,
    val karakaResolution: KarakaResolution? = null,
    val svaraResult: SvaraResult? = null,
    val samasaResolution: SamasaResolution? = null,
) {
    init {
        val expectedApplied = initial.appliedSutras + applications.map { it.sutra }
        require(final.appliedSutras == expectedApplied) {
            "Derivation applications and applied-sūtra history diverged: expected $expectedApplied, " +
                "found ${final.appliedSutras}."
        }
    }
}

/**
 * A concrete possible application.  Conflict resolution works on these, not
 * on a permanent rank assigned to a sūtra: antarāṅga, apavāda, and a target
 * are properties of an application in its present derivation context.
 */
internal data class RuleCandidate(
    val sutra: DerivationSutra,
    val change: DerivationChange,
    val delta: DerivationDelta,
    val addedBoundaryBlocks: Set<BoundaryRuleKey> = emptySet(),
) {
    val changedTargetIds: Set<String> = buildSet {
        addAll(delta.changedTerms.map { it.after.id })
        addAll(delta.removedTerms.map { it.id })
        addAll(delta.addedTerms.map { it.id })
    }
}

internal data class RuleConflict(
    val loser: RuleCandidate,
    val winner: RuleCandidate,
    val reason: String,
)

/**
 * The scheduler's only global ordering knowledge.  It must not decide
 * grammatical strength; that is done after actual competing applications are
 * known.  Tripādī candidates are ordered in textual order, other tied rules
 * use paraṁ kāryam only as the last tie-breaker.
 */
internal object RuleConflictResolver {
    fun resolve(candidates: List<RuleCandidate>): List<RuleConflict> = buildList {
        candidates.forEach { candidate ->
            candidates.filter { it != candidate }.forEach { rival ->
                if (!competes(candidate, rival)) return@forEach
                val winner = preferred(candidate, rival)
                val loser = if (winner == candidate) rival else candidate
                if (loser == candidate) add(RuleConflict(loser, winner, reason(winner, loser)))
            }
        }
    }.distinctBy { it.loser.sutra.sutra to it.winner.sutra.sutra }

    private fun competes(a: RuleCandidate, b: RuleCandidate): Boolean =
        explicitlyBlocks(b, a) || explicitlyBlocks(a, b) ||
            // Composition may transfer the same already-established boundary
            // in several possible sound changes. That bookkeeping does not
            // make the transformations grammatical alternatives of each other.
            (a.sutra.role == SutraRole.Samjna && b.sutra.role == SutraRole.Samjna &&
                a.delta.addedSamjnas.intersect(b.delta.addedSamjnas).isNotEmpty())

    private fun explicitlyBlocks(blocker: RuleCandidate, blocked: RuleCandidate): Boolean {
        if (blocked.sutra.sutra !in blocker.sutra.blocks) return false
        val scoped = blocker.addedBoundaryBlocks.filter { it.sutra == blocked.sutra.sutra }
        if (scoped.isEmpty()) return true
        return scoped.any { boundary ->
            boundary.leftId in blocked.changedTargetIds ||
                // Some boundary rules operate on the following initial sound,
                // not the preceding final sound. Do not block unrelated
                // internal occurrences in that same following carrier term.
                blocked.change.state.substitutions.lastOrNull()?.let {
                    it.sutra == blocked.sutra.sutra && it.targetId == boundary.rightId && it.sourceVarnaIndex == 0
                } == true || blocked.addedBoundaryBlocks.any {
                it.leftId == boundary.leftId && it.rightId == boundary.rightId
            }
        }
    }

    private fun preferred(a: RuleCandidate, b: RuleCandidate): RuleCandidate = when {
        explicitlyBlocks(a, b) -> a
        explicitlyBlocks(b, a) -> b
        isApavadaOf(a, b) -> a
        isApavadaOf(b, a) -> b
        isNishedha(a) && !isNishedha(b) -> a
        isNishedha(b) && !isNishedha(a) -> b
        a.sutra.isTripadi() && b.sutra.isTripadi() -> if (a.sutra.krama <= b.sutra.krama) a else b
        else -> if (a.sutra.krama >= b.sutra.krama) a else b // 1.4.2, after stronger relations.
    }

    private fun isApavadaOf(candidate: RuleCandidate, other: RuleCandidate): Boolean =
        (candidate.sutra.priority == SutraPriority.APAVADA || candidate.sutra.type == SutraType.APAVADA || candidate.sutra.role == SutraRole.Apavada) &&
            (other.sutra.sutra in candidate.sutra.blocks || other.sutra.priority != SutraPriority.APAVADA)

    private fun isNishedha(candidate: RuleCandidate): Boolean =
        candidate.sutra.role == SutraRole.Nishedha || candidate.sutra.type == SutraType.NISHEDHA

    private fun reason(winner: RuleCandidate, loser: RuleCandidate): String = when {
        explicitlyBlocks(winner, loser) -> "Explicit niṣedha/apavāda relation"
        isApavadaOf(winner, loser) -> "Apavāda overrides its wider alternative"
        isNishedha(winner) -> "Niṣedha prevents the competing operation"
        winner.sutra.isTripadi() && loser.sutra.isTripadi() -> "Tripādī textual order (pūrvatrāsiddham regime)"
        else -> "Vipratiṣedha tie-breaker (paraṃ kāryam)"
    }
}

/** A rule sees a controlled view of the derivation, rather than a global gate. */
object RuleVisibility {
    fun permits(sutra: DerivationSutra, state: DerivationState): Boolean = when (sutra.visibility) {
        SutraVisibility.NORMAL -> true
        SutraVisibility.ASIDDHAVAT -> sutra.isAbhiya()
        SutraVisibility.ASIDDHA -> sutra.isTripadi()
    }

    fun view(sutra: DerivationSutra, state: DerivationState, sutraMap: Map<String, DerivationSutra> = emptyMap()): DerivationState {
        if (sutra.visibility != SutraVisibility.ASIDDHAVAT) {
            return state
        }

        // We revert any changes made by other Abhīya rules (sutra krama in 640022..640129, not equal to sutra.sutra).
        val invisibleSutras = mutableSetOf<String>()

        // 1. Revert character substitutions
        val toRevert = state.substitutions.filter { sub ->
            val subSutra = sutraMap[sub.sutra]
            val krama = if (subSutra != null) {
                subSutra.krama
            } else {
                sutraMap[sub.sutra]?.krama ?: return@filter false
            }
            val isOtherAbhiya = krama in 640022..640129 && sub.sutra != sutra.sutra
            if (isOtherAbhiya) {
                invisibleSutras.add(sub.sutra)
                true
            } else {
                false
            }
        }

        // 2. Find any terms added by other Abhīya rules
        val termsToRemove = state.terms.filter { term ->
            term.createdBySutra?.let { createdBy ->
                val subSutra = sutraMap[createdBy]
                val krama = if (subSutra != null) {
                    subSutra.krama
                } else {
                    sutraMap[createdBy]?.krama
                }
                krama != null && krama in 640022..640129 && createdBy != sutra.sutra
            } ?: false
        }

        // 3. Find any terms dropped by other Abhīya rules
        val termsToRestore = state.droppedTerms.filter { dropped ->
            dropped.droppedBySutra?.let { droppedBy ->
                val subSutra = sutraMap[droppedBy]
                val krama = if (subSutra != null) {
                    subSutra.krama
                } else {
                    sutraMap[droppedBy]?.krama
                }
                krama != null && krama in 640022..640129 && droppedBy != sutra.sutra
            } ?: false
        }

        if (toRevert.isEmpty() && termsToRemove.isEmpty() && termsToRestore.isEmpty()) {
            return state
        }

        var visibleTerms = state.terms

        // Remove added terms
        if (termsToRemove.isNotEmpty()) {
            visibleTerms = visibleTerms.filter { it !in termsToRemove }
        }

        // Restore dropped terms (restore them with their original surface)
        if (termsToRestore.isNotEmpty()) {
            termsToRestore.forEach { dropped ->
                // Restore the original surface
                val restored = dropped.copy(
                    surface = dropped.originalSurfaceBeforeDrop ?: "",
                    rutvaOccurrences = dropped.originalRutvaOccurrencesBeforeDrop,
                    droppedBySutra = null,
                    originalSurfaceBeforeDrop = null
                )
                visibleTerms = visibleTerms + restored
            }
        }

        // Revert substitutions
        if (toRevert.isNotEmpty()) {
            toRevert.asReversed().forEach { sub ->
                visibleTerms = visibleTerms.map { term ->
                    if (term.id == sub.targetId) {
                        val originalSurface = sub.originalSurface
                        if (originalSurface != null) {
                            val originalRutva = sub.originalRutvaOccurrences ?: run {
                                require(term.rutvaOccurrences.isEmpty()) {
                                    "Visibility rollback requires an exact ru snapshot for ${term.id}."
                                }
                                emptyList()
                            }
                            term.copy(
                                surface = originalSurface,
                                orthographicSigns = sub.originalOrthographicSigns ?: term.orthographicSigns,
                                rutvaOccurrences = originalRutva,
                            )
                        } else {
                            require(term.rutvaOccurrences.isEmpty()) {
                                "Legacy text rollback cannot project exact ru occurrences on ${term.id}."
                            }
                            val index = term.surface.lastIndexOf(sub.replacement)
                            if (index >= 0) {
                                val newSurface = term.surface.substring(0, index) + sub.source + term.surface.substring(index + sub.replacement.length)
                                term.copy(surface = newSurface)
                            } else {
                                term
                            }
                        }
                    } else {
                        term
                    }
                }
            }
        }

        return state.copy(
            terms = visibleTerms,
            droppedTerms = state.droppedTerms.filter { it !in termsToRestore }
        )
    }
}

enum class OptionalRulePolicy {
    APPLY_ALL,
    SKIP_ALL,
    CUSTOM
}

data class DerivationConfig(
    val optionalRulePolicy: OptionalRulePolicy = OptionalRulePolicy.APPLY_ALL,
    val optionalRuleSelector: (String) -> Boolean = { true },
    /** Staged pipelines validate once at their outer completion boundary. */
    val validateFinalItProcessing: Boolean = true,
    /** Prevents recursive svara completion when this engine is itself executing the SVARA stage. */
    val computeSvara: Boolean = true,
    /** Explicit zero-based choice among a rule's prescribed outcomes. */
    val alternativeChoices: Map<String, Int> = emptyMap(),
)

class DerivationEngine(
    private val sutras: List<DerivationSutra>,
    private val deferTripadiUntilPada: Boolean = false,
) {
    private val sutraMap = sutras.associateBy { it.sutra }
    private val adhikaraSutras = sutras.filter { it.role is SutraRole.Adhikara }
    private val sutraActiveAdhikaras: Map<String, List<DerivationSutra>> = activeAdhikarasCache.computeIfAbsent(sutras) { list ->
        val adhikaras = list.filter { it.role is SutraRole.Adhikara }
        list.associate { sutra ->
            val krama = sutra.krama
            val domains = adhikaras.filter { domain ->
                val role = domain.role as SutraRole.Adhikara
                val start = role.customStartKrama ?: domain.krama
                val end = role.endKrama
                krama in start..end
            }
            sutra.sutra to domains
        }
    }

    fun derive(initial: DerivationState, maxSteps: Int = 100): DerivationResult =
        derive(initial, DerivationConfig(), maxSteps)

    fun derive(initial: DerivationState, config: DerivationConfig, maxSteps: Int = 100): DerivationResult =
        deriveInternal(initial, emptySet(), config, maxSteps)

    /** Enumerates prescribed outcomes, including optional application/decline. */
    fun deriveAll(initial: DerivationState, maxSteps: Int = 100): List<DerivationResult> =
        deriveAll(initial, DerivationConfig(), maxSteps)

    fun deriveAll(initial: DerivationState, config: DerivationConfig, maxSteps: Int = 100): List<DerivationResult> {
        val pending = ArrayDeque<Map<RuleAlternativeKey, Int>>().apply { add(emptyMap()) }
        val visitedChoices = mutableSetOf<Map<RuleAlternativeKey, Int>>()
        val branchedSutras = linkedMapOf<String, Int>()
        val results = mutableListOf<DerivationResult>()
        while (pending.isNotEmpty()) {
            val choices = pending.removeFirst()
            if (!visitedChoices.add(choices)) continue
            val decisions = mutableListOf<RuleAlternativeDecision>()

            val result = deriveInternal(
                initial = initial,
                suppressed = emptySet(),
                config = config.copy(optionalRulePolicy = OptionalRulePolicy.APPLY_ALL, alternativeChoices = emptyMap()),
                maxSteps = maxSteps,
                alternativePlan = choices,
                encounteredChoices = decisions,
            )
            results += result

            decisions.forEachIndexed { index, decision ->
                branchedSutras[decision.key.sutra] = maxOf(branchedSutras[decision.key.sutra] ?: 0, decision.count)
                // Changing an earlier choice invalidates later context-dependent
                // decisions. Replay only the established prefix, never stale indices.
                val prefix = decisions.take(index).filter { it.selected != 0 }.associate { it.key to it.selected }
                for (choice in 0 until decision.count) {
                    if (choice == decision.selected) continue
                    pending += if (choice == 0) prefix else prefix + (decision.key to choice)
                }
            }
        }

        val branchEvents = branchedSutras.map { (sutra, count) -> DerivationEvent.BranchCreated(sutra, count) }
        return results
            .distinctBy { it.final to it.applications.map { application -> application.sutra to application.explanation } }
            .map { result -> result.copy(events = branchEvents + result.events) }
    }

    private data class RuleAlternativeKey(val sutra: String, val occurrence: Int)
    private data class RuleAlternativeDecision(val key: RuleAlternativeKey, val count: Int, val selected: Int)

    private fun deriveInternal(
        initial: DerivationState,
        suppressed: Set<String>,
        config: DerivationConfig = DerivationConfig(),
        maxSteps: Int,
        firstChange: DerivationChange? = null,
        alternativePlan: Map<RuleAlternativeKey, Int> = emptyMap(),
        encounteredChoices: MutableList<RuleAlternativeDecision>? = null,
    ): DerivationResult {
        var current = initial
        var preselectedChange = firstChange
        val applications = mutableListOf<DerivationApplication>()
        val events = mutableListOf<DerivationEvent>()
        val visited = mutableSetOf(cycleKey(initial))
        val suppressedRules = suppressed.toMutableSet()
        val occurrences = mutableMapOf<String, Int>()

        repeat(maxSteps) {
            current = completeUndesignatedRawTerms(current)
            val selection = select(current, suppressedRules)
            events += selection.candidates.map { DerivationEvent.RuleConsidered(it.sutra.sutra) }
            events += selection.conflicts.map { DerivationEvent.RuleBlocked(it.loser.sutra.sutra, it.winner.sutra.sutra, it.reason) }

            val blockedEvents = sutras.filter {
                it.sutra in current.blockedSutras && isDerivationEligible(it, current) && it.matches(current)
            }
                .map { DerivationEvent.RuleBlocked(it.sutra, current.blockedSutras[it.sutra]!!, "Blocked by grammar.") }
            events += blockedEvents

            val candidate = selection.selected
            if (candidate?.sutra?.isTripadi() == true) {
                val lifecycleCompleted = advanceCompletedItProcessing(current)
                if (lifecycleCompleted != current) {
                    current = lifecycleCompleted
                    return@repeat
                }
            }
            if (candidate == null) {
                val lifecycleCompleted = advanceCompletedItProcessing(current)
                if (lifecycleCompleted != current) {
                    current = lifecycleCompleted
                    return@repeat
                }
                return completed(initial, current, applications, events, config)
            }

            val change = if (candidate.sutra.hasAlternatives) {
                val alternatives = candidate.sutra.applyAll(current)
                require(alternatives.isNotEmpty()) { "Rule ${candidate.sutra.sutra} has no alternatives." }
                val occurrence = occurrences.getOrDefault(candidate.sutra.sutra, 0)
                occurrences[candidate.sutra.sutra] = occurrence + 1
                val key = RuleAlternativeKey(candidate.sutra.sutra, occurrence)
                val policyChoice = if (!candidate.sutra.optional) 0 else when (config.optionalRulePolicy) {
                    OptionalRulePolicy.APPLY_ALL -> 0
                    OptionalRulePolicy.SKIP_ALL -> alternatives.lastIndex
                    OptionalRulePolicy.CUSTOM -> if (config.optionalRuleSelector(candidate.sutra.sutra)) 0 else alternatives.lastIndex
                }
                val selected = alternativePlan[key] ?: config.alternativeChoices[key.sutra] ?: policyChoice
                require(selected in alternatives.indices) { "Invalid alternative $selected for ${key.sutra}." }
                encounteredChoices?.add(RuleAlternativeDecision(key, alternatives.size, selected))
                alternatives[selected]
            } else preselectedChange ?: candidate.change
            preselectedChange = null
            if (!change.applied) {
                require(candidate.sutra.optional) { "A prescribed alternative cannot decline a mandatory rule." }
                require(change.state == current) { "A declined optional rule cannot mutate the state." }
                suppressedRules.add(candidate.sutra.sutra)
                return@repeat
            }
            if (!candidate.sutra.advancesTripadiFrontier || !change.advancesTripadiFrontier) {
                require(change.state.terms == current.terms) { "An identity-retention alternative cannot change terms." }
            }
            require(change.applied) { "Non-branching derivation cannot decline selected sutra ${candidate.sutra.sutra}." }
            require(change.state != current) { "Selected sutra ${candidate.sutra.sutra} performed no grammatical operation." }

            val application = DerivationApplication(
                candidate.sutra.sutra, candidate.sutra.role, candidate.sutra.action, candidate.sutra.scope,
                candidate.sutra.renderTrace(), current, change.state, change.explanation,
                selection.conflicts.filter { it.winner == candidate }.map { "Blocked ${it.loser.sutra.sutra}: ${it.reason}" },
                source = candidate.sutra.source,
            )
            applications += application
            events += DerivationEvent.RuleApplied(application.sutra, current, change.state, change.explanation)

            val stateWithApplication = change.state.recordAppliedSutra(candidate.sutra.sutra,
                candidate.sutra.advancesTripadiFrontier && change.advancesTripadiFrontier)
            val nextStateKey = cycleKey(stateWithApplication)
            if (nextStateKey in visited) {
                val nextSelection = select(stateWithApplication, suppressedRules)
                require(nextSelection.selected == null) {
                    "Derivation entered a cycle after applying ${candidate.sutra.sutra}. History: ${applications.map { "${it.sutra} (${it.before.surface} -> ${it.after.surface})" }}"
                }
            }
            visited.add(nextStateKey)

            current = stateWithApplication
        }
        error("Derivation did not reach a fixed point within $maxSteps steps. History: ${applications.takeLast(20).map { "${it.sutra} (${it.before.surface} -> ${it.after.surface})" }}")
    }

    /**
     * Closes the engine lifecycle after 1.3.2–1.3.8 have had an opportunity
     * to designate an इत् segment. This is state bookkeeping, not a
     * grammatical operation, so it deliberately creates no trace entry.
     */
    private fun completeUndesignatedRawTerms(state: DerivationState): DerivationState {
        val designationRules = sutras.filter { it.krama in 130002..130008 }
        // A staged pipeline may introduce a raw upadeśa before its dedicated
        // IT_PROCESSING phase. Only that phase may declare the designation
        // pass exhausted; an arbitrary partial rule set must leave it raw.
        if (designationRules.isEmpty()) return state
        val hasExactDeletionPending = state.terms.any {
            it.itDesignations.isNotEmpty() ||
                (it.deferredItDesignations.isNotEmpty() && it.itProcessingPhase != ItProcessingPhase.DEFERRED_SUBSTITUTION)
        }
        if (hasExactDeletionPending) return state

        val designationStillApplicable = designationRules.any { sutra ->
            sutra.sutra !in state.blockedSutras &&
                isDerivationEligible(sutra, state) &&
                sutra.matches(RuleVisibility.view(sutra, state, sutraMap))
        }
        if (designationStillApplicable) return state

        val completedTerms = state.terms.map { term ->
            if (term.itProcessingPhase == ItProcessingPhase.RAW_UPADESHA &&
                term.itMarkers.isEmpty() &&
                term.itDesignations.isEmpty() &&
                term.deferredItDesignations.isEmpty()
            ) term.copy(itProcessingPhase = ItProcessingPhase.PROCESSED) else term
        }
        return if (completedTerms == state.terms) state else state.copy(terms = completedTerms)
    }

    private fun advanceCompletedItProcessing(state: DerivationState): DerivationState {
        val selectedEndingUpadeshas = SupAffix.entries.mapTo(mutableSetOf()) { it.upadesha } +
            TingAffix.entries.map { it.upadesha }
        val selectedEndingIsProcessed = state.stage == DerivationStage.PRATYAYA_SELECTED &&
            state.terms.any { term ->
                (term.id.startsWith("ting-") || selectedEndingUpadeshas.any(term::matchesUpadesha)) &&
                    !term.itProcessingPending
            }
        if (!selectedEndingIsProcessed) return state
        return state.copy(stage = DerivationStage.IT_PROCESSED)
    }

    private fun completed(initial: DerivationState, current: DerivationState, applications: List<DerivationApplication>, events: List<DerivationEvent>, config: DerivationConfig): DerivationResult {
        val finalState = if (current.stage == DerivationStage.PADA_FORMED) {
            current.copy(stage = DerivationStage.FINAL)
        } else {
            current
        }.let { state ->
            if (state.stage == DerivationStage.FINAL && config.validateFinalItProcessing) state.requireCompleteItProcessing() else state
        }
        val svara = if (config.computeSvara && finalState.surface.isNotBlank()) SvaraEngine.derive(finalState) else null
        val completedState = svara?.state ?: finalState
        val completedApplications = applications + svara?.applications.orEmpty()
        val completedEvents = events + svara?.events.orEmpty()
        return DerivationResult(
            initial, completedState, completedApplications,
            completedEvents + DerivationEvent.Completed(completedState, completedApplications.size),
            svaraResult = svara?.result,
        )
    }

    private fun tripadiFrontier(state: DerivationState): Int =
        state.appliedSutras.mapIndexedNotNull { index, number ->
            if (index in state.nonAdvancingTripadiApplications) null
            else sutraMap[number]?.takeIf { rule -> rule.advancesTripadiFrontier }?.krama
        }
            .filter { it >= 820000 }.maxOrNull() ?: 0

    // Restoring a spelling after advancing through Tripādī is not a cycle:
    // the earlier rules are now invisible. Do not retain the whole application
    // history, which would conceal genuine cycles within the same frontier.
    private fun cycleKey(state: DerivationState) =
        state.copy(substitutions = emptyList(), appliedSutras = emptyList(), nonAdvancingTripadiApplications = emptySet()) to tripadiFrontier(state)

    private fun select(state: DerivationState, suppressed: Set<String>): RuleSelection {
        val maxTripadiKrama = tripadiFrontier(state)
        val hasPendingItProcessing = state.terms.any { it.itProcessingPending }

        val evaluated = sutras.asSequence()
            .filter {
                it.sutra !in suppressed &&
                    (RuleVisibility.permits(it, state) ||
                        (hasPendingItProcessing && it.stage == SutraStage.IT_PROCESSING))
            }
            // A newly introduced upadeśa must finish 1.3.2–1.3.9 before
            // rules outside the it-processing domain can inspect its surface.
            .filter { !hasPendingItProcessing || it.stage == SutraStage.IT_PROCESSING }
            .filter {
                val blocker = state.blockedSutras[it.sutra]
                if (blocker != null) {
                    val blockerSutra = sutraMap[blocker]
                    blockerSutra == null || !blockerSutra.matches(state)
                } else {
                    true
                }
            }
            .filter {
                if (it.isTripadi()) {
                    it.krama >= maxTripadiKrama
                } else {
                    maxTripadiKrama == 0 ||
                        (it.stage == SutraStage.IT_PROCESSING && state.terms.any { term -> term.itProcessingPending }) ||
                        // 1.1.12 specifically sees adas mutva despite pūrvatrāsiddham
                        // (Kāśikā/Nyāsa on adaso māt: vacanasāmarthya).
                        (it.sutra == "1.1.12" && state.substitutions.any { change ->
                            change.sutra in setOf("8.2.80", "8.2.81")
                        }) ||
                        (it.sutra in setOf("6.1.113", "6.1.114") && "8.2.66" in state.appliedSutras) ||
                        (it.sutra == "6.3.111" && state.substitutions.any { deletion ->
                            deletion.sutra in setOf("8.3.13", "8.3.14") && deletion.replacement.isEmpty()
                        }) ||
                        (it.sutra == "6.1.87" && state.substitutions.lastOrNull()?.sutra in setOf("6.1.113", "6.1.114")) ||
                        (it.sutra == "6.1.109" && state.substitutions.takeLast(2).let { changes ->
                            changes.size == 2 && changes[0].sutra == "6.1.113" &&
                                changes[1].sutra == "6.1.87" && changes[0].targetId == changes[1].targetId
                        })
                }
            }
            .filter {
                val visibleState = if (hasPendingItProcessing && it.stage == SutraStage.IT_PROCESSING) {
                    state
                } else {
                    RuleVisibility.view(it, state, sutraMap)
                }
                isDerivationEligible(it, visibleState) &&
                it.matches(visibleState)
            }
            .map { sutra ->
                val change = sutra.apply(state)
                val delta = DerivationDelta.between(state, change.state)
                require(sutra.advancesTripadiFrontier ||
                    (delta.changedTerms.isEmpty() && delta.addedTerms.isEmpty() && delta.removedTerms.isEmpty())) {
                    "An identity-retention guard must not change phonological terms: ${sutra.sutra}."
                }
                RuleCandidate(sutra, change, delta,
                    change.state.boundaryRuleBlocks.keys - state.boundaryRuleBlocks.keys)
            }
            .toList()
        val pendingFeminine = state.terms.filter { it.feminineTargetId != null }
        val pendingAdeshas = state.terms.filter { it.adeshaTarget != null }
        val pendingBases = state.terms.filter { it.pendingBaseComposition != null }
        val feminineIntroductions = evaluated.filter { candidate ->
            candidate.change.state.terms.any { suffix -> suffix.feminineTargetId != null &&
                state.terms.none { it.id == suffix.id } }
        }
        // An owned feminine suffix must finish it-processing and compose with
        // its owner before case operations can treat it as an independent stem.
        val permitted = evaluated.filter { candidate ->
            if (pendingAdeshas.isNotEmpty()) candidate.sutra.stage == SutraStage.IT_PROCESSING ||
                pendingAdeshas.any { carrier ->
                    candidate.change.state.terms.none { it.id == carrier.id } &&
                        candidate.change.state.droppedTerms.any {
                            it.id == carrier.id && it.mergedIntoTermId == carrier.adeshaTarget?.termId
                        }
                }
            else if (pendingBases.isNotEmpty()) pendingBases.all { base ->
                candidate.change.state.terms.any { it.id == base.id && it.pendingBaseComposition == null }
            }
            else if (feminineIntroductions.isNotEmpty()) candidate in feminineIntroductions
            else pendingFeminine.isEmpty() ||
                candidate.sutra.stage == SutraStage.IT_PROCESSING || pendingFeminine.any { suffix ->
                    candidate.change.state.terms.none { it.id == suffix.id } &&
                        candidate.change.state.droppedTerms.any {
                            it.id == suffix.id && it.mergedIntoTermId == suffix.feminineTargetId
                        }
                }
        }
        val candidates = permitted.filter { it.change.state != state }
        val conflicts = RuleConflictResolver.resolve(candidates)
        val losers = conflicts.mapTo(mutableSetOf()) { it.loser }
        val selected = candidates.filterNot { it in losers }.minWithOrNull(candidateOrder(state)) ?: permitted.firstOrNull()
        return RuleSelection(candidates, conflicts, selected)
    }

    private fun isDerivationEligible(sutra: DerivationSutra, state: DerivationState): Boolean {
        if (deferTripadiUntilPada && sutra.isTripadi() && state.stage < DerivationStage.PADA_FORMED) return false
        val isPadaBoundaryDerivation = state.samjnas.count { it.samjna == Samjna.PADA } >= 2 &&
            state.allEffectiveTerms.none { it.kind == TermKind.DHATU || it.kind == TermKind.PRATYAYA } &&
            state.terms.none { it.kind == TermKind.ADESHA }
        if (isPadaBoundaryDerivation && sutra.stage !in SutraStage.sandhiPhases &&
            sutra.sutra !in setOf("1.1.11", "1.1.12") && // Licensed pragṛhya prerequisites of external sandhi.
            !(sutra.stage == SutraStage.IT_PROCESSING && state.terms.any { it.itProcessingPending })
        ) return false
        if (!isPadaBoundaryDerivation && sutra.scope == SutraScope.PADA_BOUNDARY) return false

        val activeDomains = sutraActiveAdhikaras[sutra.sutra] ?: emptyList()
        return activeDomains.all { domain ->
            domain.sutra in state.activeAdhikaras || domain.matches(state)
        }
    }

    private companion object {
        private val activeAdhikarasCache = java.util.concurrent.ConcurrentHashMap<List<DerivationSutra>, Map<String, List<DerivationSutra>>>()

        /**
         * This is an agenda, not a conflict-strength score.  Technical labels
         * must be established before an operation consumes them; after that,
         * textual order is used only to choose the next independent action.
         */
        fun candidateOrder(state: DerivationState) = compareBy<RuleCandidate>(
            { candidate ->
                val introducesRequestedRawAffix = candidate.delta.addedTerms.any { term ->
                    term.itProcessingPhase == ItProcessingPhase.RAW_UPADESHA &&
                        term.upadesha in state.effectiveContext.requestedSanadi
                }
                if (introducesRequestedRawAffix && state.stage != DerivationStage.INITIAL) 0 else 1
            },
            { candidate ->
                if (candidate.sutra.sutra == "1.3.9" && state.terms.any {
                        it.itProcessingPending || it.itDesignations.isNotEmpty()
                    }) 4
                else if (candidate.sutra.sutra == "3.4.92" && "7.3.84" in state.appliedSutras) 2
                else if (candidate.sutra.sutra == "3.4.93" && state.allEffectiveTerms.any { "3.4.92" in it.establishedBySutras }) 2
                else agendaDomain(candidate.sutra)
            },
            { it.sutra.isTripadi() },
            { if (it.sutra.isTripadi()) it.sutra.krama else -it.sutra.krama },
        )

        fun agendaDomain(sutra: DerivationSutra): Int = when {
            sutra.role == SutraRole.Samjna || sutra.role is SutraRole.Paribhasha || sutra.role == SutraRole.Atidesha -> 0
            sutra.role is SutraRole.Adhikara || sutra.role == SutraRole.Anuvrtti -> 1
            // Competing apavādas are resolved above. An unrelated later
            // Tripādī apavāda must not jump ahead and hide earlier work.
            sutra.isTripadi() -> 3
            sutra.role == SutraRole.Nishedha || sutra.role == SutraRole.Niyama || sutra.role == SutraRole.Apavada -> 2
            else -> 3
        }

    }
}

internal fun DerivationSutra.isTripadi(): Boolean = krama >= 820000
internal fun DerivationSutra.isAbhiya(): Boolean = krama in 640022..640129

private data class RuleSelection(
    val candidates: List<RuleCandidate>,
    val conflicts: List<RuleConflict>,
    val selected: RuleCandidate?,
)

fun DerivationResult.verifyDerivation(
    selectionSutra: String,
    expectedAffixUpadesha: String,
    requiredSutras: Set<String>,
    expectedStage: DerivationStage
) {
    val selectedAffix = applications
        .singleOrNull { it.sutra == selectionSutra }
        ?.delta
        ?.addedTerms
        ?.singleOrNull()
        ?.upadesha
    require(selectedAffix == expectedAffixUpadesha) {
        val recent = applications.takeLast(20).joinToString { it.sutra }
        "$selectionSutra selected $selectedAffix, but $expectedAffixUpadesha was required. Recent rules: $recent."
    }
    val appliedSutras = applications.mapTo(mutableSetOf()) { it.sutra }
    // The vowel-initial apavāda legitimately replaces the plan's general aṭ requirement.
    val completedAat = "6.4.72" in appliedSutras && "6.1.90" in appliedSutras &&
        final.allEffectiveTerms.any { it.kind == TermKind.AGAMA && it.upadesha == "आट्" &&
            it.createdBySutra == "6.4.72" && it.droppedBySutra == "6.1.90" }
    require(requiredSutras.all { it in appliedSutras || (it == "6.4.71" && completedAat) }) {
        val recentApplications = applications.takeLast(15).joinToString { "${it.sutra} (${it.before} -> ${it.after})" }
        "Incomplete derivation for $expectedAffixUpadesha; missing ${requiredSutras - appliedSutras}. Recent rules: $recentApplications."
    }
    require(final.stage.ordinal >= expectedStage.ordinal) {
        val recentApplications = applications.takeLast(10).joinToString { it.sutra }
        "Incomplete derivation for $expectedAffixUpadesha; expected at least $expectedStage, reached ${final.stage}. Recent rules: $recentApplications."
    }
}
