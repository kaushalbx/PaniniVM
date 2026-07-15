package dev.sanskrit.derivation

import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraPriority
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType
import dev.sanskrit.sutra.SutraVisibility
import dev.sanskrit.ashtadhyayi.Ashtadhyayi

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
)

/**
 * A concrete possible application.  Conflict resolution works on these, not
 * on a permanent rank assigned to a sūtra: antarāṅga, apavāda, and a target
 * are properties of an application in its present derivation context.
 */
internal data class RuleCandidate(
    val sutra: DerivationSutra,
    val change: DerivationChange,
    val delta: DerivationDelta,
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
        a.sutra.sutra in b.sutra.blocks ||
            b.sutra.sutra in a.sutra.blocks ||
            (a.delta.addedSamjnas.intersect(b.delta.addedSamjnas).isNotEmpty())

    private fun preferred(a: RuleCandidate, b: RuleCandidate): RuleCandidate = when {
        b.sutra.sutra in a.sutra.blocks -> a
        a.sutra.sutra in b.sutra.blocks -> b
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
        loser.sutra.sutra in winner.sutra.blocks -> "Explicit niṣedha/apavāda relation"
        isApavadaOf(winner, loser) -> "Apavāda overrides its wider alternative"
        isNishedha(winner) -> "Niṣedha prevents the competing operation"
        winner.sutra.isTripadi() && loser.sutra.isTripadi() -> "Tripādī textual order (pūrvatrāsiddham regime)"
        else -> "Vipratiṣedha tie-breaker (paraṃ kāryam)"
    }
}

/** A rule sees a controlled view of the derivation, rather than a global gate. */
internal object RuleVisibility {
    fun permits(sutra: DerivationSutra, state: DerivationState): Boolean = when (sutra.visibility) {
        SutraVisibility.NORMAL -> true
        SutraVisibility.ASIDDHAVAT -> sutra.isAbhiya()
        SutraVisibility.ASIDDHA -> sutra.isTripadi()
    }
}

class DerivationEngine(
    private val sutras: List<DerivationSutra> = Ashtadhyayi.executableSutras,
) {
    fun derive(initial: DerivationState, maxSteps: Int = 100): DerivationResult =
        deriveInternal(initial, emptySet(), maxSteps)

    /** Produces both outcomes for each optional sūtra instead of silently choosing one. */
    fun deriveAll(initial: DerivationState, maxSteps: Int = 100): List<DerivationResult> {
        data class Branch(val state: DerivationState, val suppressed: Set<String>, val events: List<DerivationEvent>)
        val pending = ArrayDeque(listOf(Branch(initial, emptySet(), emptyList())))
        val results = mutableListOf<DerivationResult>()
        while (pending.isNotEmpty()) {
            val branch = pending.removeFirst()
            val selection = select(branch.state, branch.suppressed)
            val optional = selection.selected?.takeIf { it.sutra.optional }
            if (optional == null) {
                val result = deriveInternal(branch.state, branch.suppressed, maxSteps)
                results += result.copy(events = branch.events + result.events)
                continue
            }
            val applied = deriveInternal(branch.state, branch.suppressed, maxSteps, firstChange = optional.change)
            val skipped = deriveInternal(branch.state, branch.suppressed + optional.sutra.sutra, maxSteps)
            val event = DerivationEvent.BranchCreated(optional.sutra.sutra, 2)
            results += applied.copy(events = branch.events + event + applied.events)
            results += skipped.copy(events = branch.events + event + skipped.events)
        }
        return results
    }

    private fun deriveInternal(
        initial: DerivationState,
        suppressed: Set<String>,
        maxSteps: Int,
        firstChange: DerivationChange? = null,
    ): DerivationResult {
        var current = initial
        var preselectedChange = firstChange
        val applications = mutableListOf<DerivationApplication>()
        val events = mutableListOf<DerivationEvent>()
        val visited = mutableSetOf(initial.copy(substitutions = emptyList()))

        repeat(maxSteps) {
            val selection = select(current, suppressed)
            events += selection.candidates.map { DerivationEvent.RuleConsidered(it.sutra.sutra) }
            events += selection.conflicts.map { DerivationEvent.RuleBlocked(it.loser.sutra.sutra, it.winner.sutra.sutra, it.reason) }
            
            val blockedEvents = sutras.filter { it.sutra in current.blockedSutras && it.matches(current) }
                .map { DerivationEvent.RuleBlocked(it.sutra, current.blockedSutras[it.sutra]!!, "Blocked by grammar.") }
            events += blockedEvents

            val candidate = selection.selected ?: return completed(initial, current, applications, events)
            val change = preselectedChange ?: candidate.change
            preselectedChange = null
            require(change.applied) { "Non-branching derivation cannot decline selected sutra ${candidate.sutra.sutra}." }
            require(change.state != current) { "Selected sutra ${candidate.sutra.sutra} performed no grammatical operation." }

            val application = DerivationApplication(
                candidate.sutra.sutra, candidate.sutra.role, candidate.sutra.action, candidate.sutra.scope,
                candidate.sutra.renderTrace(), current, change.state, change.explanation,
                selection.conflicts.filter { it.winner == candidate }.map { "Blocked ${it.loser.sutra.sutra}: ${it.reason}" },
            )
            applications += application
            events += DerivationEvent.RuleApplied(application.sutra, current, change.state, change.explanation)
            
            val nextStateKey = change.state.copy(substitutions = emptyList())
            if (nextStateKey in visited) {
                val nextSelection = select(change.state, suppressed)
                require(nextSelection.selected == null) {
                    "Derivation entered a cycle after applying ${candidate.sutra.sutra}. History: ${applications.map { "${it.sutra} (${it.before.surface} -> ${it.after.surface})" }}"
                }
            }
            visited.add(nextStateKey)

            val stateWithSub = change.state.copy(
                substitutions = change.state.substitutions + VarnaSubstitution("", ' ', "", candidate.sutra.sutra)
            )
            current = stateWithSub
        }
        error("Derivation did not reach a fixed point within $maxSteps steps.")
    }

    private fun completed(initial: DerivationState, current: DerivationState, applications: List<DerivationApplication>, events: List<DerivationEvent>): DerivationResult {
        val finalState = if (current.stage == DerivationStage.PADA_FORMED) {
            current.copy(stage = DerivationStage.FINAL)
        } else {
            current
        }
        return DerivationResult(initial, finalState, applications, events + DerivationEvent.Completed(finalState, applications.size))
    }

    private fun select(state: DerivationState, suppressed: Set<String>): RuleSelection {
        val tripadiKramasApplied = state.substitutions.mapNotNull { sub ->
            sutras.find { it.sutra == sub.sutra }?.krama
        }.filter { it >= 820000 }
        val maxTripadiKrama = tripadiKramasApplied.maxOrNull() ?: 0

        val evaluated = sutras.asSequence()
            .filter { it.sutra !in suppressed && RuleVisibility.permits(it, state) }
            .filter { 
                val blocker = state.blockedSutras[it.sutra]
                if (blocker != null) {
                    val blockerSutra = sutras.find { it.sutra == blocker }
                    blockerSutra == null || !blockerSutra.matches(state)
                } else {
                    true
                }
            }
            .filter { 
                if (it.isTripadi()) {
                    it.krama >= maxTripadiKrama
                } else {
                    maxTripadiKrama == 0
                }
            }
            .filter { it.matches(state) }
            .map { sutra ->
                val change = sutra.apply(state)
                RuleCandidate(sutra, change, DerivationDelta.between(state, change.state))
            }
            .toList()
        // A broad catalogue rule may match without changing this state.  It is
        // not a competing operation and therefore cannot starve a real rule.
        // If it is the only match, preserve the contract failure that exposes
        // its incomplete implementation.
        val candidates = evaluated.filter { it.change.state != state }
        val conflicts = RuleConflictResolver.resolve(candidates)
        val losers = conflicts.mapTo(mutableSetOf()) { it.loser }
        val selected = candidates.filterNot { it in losers }.minWithOrNull(candidateOrder) ?: evaluated.firstOrNull()
        return RuleSelection(candidates, conflicts, selected)
    }

    private companion object {
        /**
         * This is an agenda, not a conflict-strength score.  Technical labels
         * must be established before an operation consumes them; after that,
         * textual order is used only to choose the next independent action.
         */
        val candidateOrder = compareBy<RuleCandidate>(
            { agendaDomain(it.sutra) },
            { it.sutra.isTripadi() },
            { if (it.sutra.isTripadi()) it.sutra.krama else -it.sutra.krama },
        )

        fun agendaDomain(sutra: DerivationSutra): Int = when (sutra.role) {
            SutraRole.Samjna, SutraRole.Paribhasha -> 0
            SutraRole.Adhikara, SutraRole.Anuvrtti -> 1
            SutraRole.Nishedha, SutraRole.Niyama, SutraRole.Apavada -> 2
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
