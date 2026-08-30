package dev.panini.derivation

import dev.panini.analysis.KarakaResolution
import dev.panini.analysis.SamasaResolution
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
                        val index = term.surface.lastIndexOf(sub.replacement)
                        if (index >= 0) {
                            val newSurface = term.surface.substring(0, index) + sub.source + term.surface.substring(index + sub.replacement.length)
                            term.copy(surface = newSurface)
                        } else {
                            term
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
    val optionalRuleSelector: (String) -> Boolean = { true }
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

    /** Produces both outcomes for each optional sūtra instead of silently choosing one. */
    fun deriveAll(initial: DerivationState, maxSteps: Int = 100): List<DerivationResult> {
        val pending = ArrayDeque<Set<String>>().apply { add(emptySet()) }
        val visitedSuppressions = mutableSetOf<Set<String>>()
        val branchedSutras = linkedSetOf<String>()
        val results = mutableListOf<DerivationResult>()
        while (pending.isNotEmpty()) {
            val suppressed = pending.removeFirst()
            if (!visitedSuppressions.add(suppressed)) continue

            val result = deriveInternal(
                initial = initial,
                suppressed = suppressed,
                config = DerivationConfig(OptionalRulePolicy.APPLY_ALL),
                maxSteps = maxSteps,
            )
            results += result

            result.applications
                .asSequence()
                .mapNotNull { application -> sutraMap[application.sutra]?.takeIf { it.optional }?.sutra }
                .forEach { optionalSutra ->
                    branchedSutras += optionalSutra
                    pending += suppressed + optionalSutra
                }
        }

        val branchEvents = branchedSutras.map { DerivationEvent.BranchCreated(it, 2) }
        return results
            .distinctBy { it.final to it.applications.map(DerivationApplication::sutra) }
            .map { result -> result.copy(events = branchEvents + result.events) }
    }

    private fun deriveInternal(
        initial: DerivationState,
        suppressed: Set<String>,
        config: DerivationConfig = DerivationConfig(),
        maxSteps: Int,
        firstChange: DerivationChange? = null,
    ): DerivationResult {
        var current = initial
        var preselectedChange = firstChange
        val applications = mutableListOf<DerivationApplication>()
        val events = mutableListOf<DerivationEvent>()
        val visited = mutableSetOf(initial.copy(substitutions = emptyList()))
        val suppressedRules = suppressed.toMutableSet()

        repeat(maxSteps) {
            val selection = select(current, suppressedRules)
            events += selection.candidates.map { DerivationEvent.RuleConsidered(it.sutra.sutra) }
            events += selection.conflicts.map { DerivationEvent.RuleBlocked(it.loser.sutra.sutra, it.winner.sutra.sutra, it.reason) }

            val blockedEvents = sutras.filter {
                it.sutra in current.blockedSutras && isDerivationEligible(it, current) && it.matches(current)
            }
                .map { DerivationEvent.RuleBlocked(it.sutra, current.blockedSutras[it.sutra]!!, "Blocked by grammar.") }
            events += blockedEvents

            val candidate = selection.selected ?: return completed(initial, current, applications, events)

            val shouldApply = if (candidate.sutra.optional) {
                when (config.optionalRulePolicy) {
                    OptionalRulePolicy.APPLY_ALL -> true
                    OptionalRulePolicy.SKIP_ALL -> false
                    OptionalRulePolicy.CUSTOM -> config.optionalRuleSelector(candidate.sutra.sutra)
                }
            } else {
                true
            }

            if (!shouldApply) {
                suppressedRules.add(candidate.sutra.sutra)
                return@repeat
            }

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
                val nextSelection = select(change.state, suppressedRules)
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
        error("Derivation did not reach a fixed point within $maxSteps steps. History: ${applications.takeLast(20).map { "${it.sutra} (${it.before.surface} -> ${it.after.surface})" }}")
    }

    private fun completed(initial: DerivationState, current: DerivationState, applications: List<DerivationApplication>, events: List<DerivationEvent>): DerivationResult {
        val finalState = if (current.stage == DerivationStage.PADA_FORMED) {
            current.copy(stage = DerivationStage.FINAL)
        } else {
            current
        }
        val svara = if (finalState.surface.isNotBlank()) {
            val isNitOrNnit = finalState.allEffectiveTerms.any { 
                it.itMarkers.contains(dev.panini.core.ItMarker.NIT) || it.itMarkers.contains(dev.panini.core.ItMarker.NGIT) 
            }
            val isPitOrSup = finalState.allEffectiveTerms.any { 
                it.itMarkers.contains(dev.panini.core.ItMarker.P) || it.kind == TermKind.PRATYAYA 
            }
            SvaraEngine.computeSvara(finalState.surface, isNitOrNnit = isNitOrNnit, isPitOrSup = isPitOrSup)
        } else {
            null
        }
        return DerivationResult(initial, finalState, applications, events + DerivationEvent.Completed(finalState, applications.size), svaraResult = svara)
    }

    private fun select(state: DerivationState, suppressed: Set<String>): RuleSelection {
        val tripadiKramasApplied = state.substitutions.mapNotNull { sub ->
            sutraMap[sub.sutra]?.krama
        }.filter { it >= 820000 }
        val maxTripadiKrama = tripadiKramasApplied.maxOrNull() ?: 0

        val evaluated = sutras.asSequence()
            .filter { it.sutra !in suppressed && RuleVisibility.permits(it, state) }
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
                        (it.stage == SutraStage.IT_PROCESSING && state.terms.any { term -> term.itProcessingPending })
                }
            }
            .filter {
                val visibleState = RuleVisibility.view(it, state, sutraMap)
                isDerivationEligible(it, visibleState) &&
                it.matches(visibleState)
            }
            .map { sutra ->
                val change = sutra.apply(state)
                RuleCandidate(sutra, change, DerivationDelta.between(state, change.state))
            }
            .toList()
        val candidates = evaluated.filter { it.change.state != state }
        val conflicts = RuleConflictResolver.resolve(candidates)
        val losers = conflicts.mapTo(mutableSetOf()) { it.loser }
        val selected = candidates.filterNot { it in losers }.minWithOrNull(candidateOrder(state)) ?: evaluated.firstOrNull()
        return RuleSelection(candidates, conflicts, selected)
    }

    private fun isDerivationEligible(sutra: DerivationSutra, state: DerivationState): Boolean {
        if (deferTripadiUntilPada && sutra.isTripadi() && state.stage < DerivationStage.PADA_FORMED) return false
        val isPadaBoundaryDerivation = state.samjnas.count { it.samjna == Samjna.PADA } >= 2 &&
            state.allEffectiveTerms.none { it.kind == TermKind.DHATU || it.kind == TermKind.PRATYAYA }
        if (isPadaBoundaryDerivation && sutra.stage !in SutraStage.sandhiPhases &&
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
                if (candidate.sutra.sutra == "1.3.9" && state.terms.any { it.itProcessingPending }) 1
                else if (candidate.sutra.sutra == "3.4.92" && state.substitutions.any { it.sutra == "7.3.84" }) 2
                else if (candidate.sutra.sutra == "3.4.93" && state.allEffectiveTerms.any { "3.4.92" in it.establishedBySutras }) 2
                else agendaDomain(candidate.sutra)
            },
            { it.sutra.isTripadi() },
            { if (it.sutra.isTripadi()) it.sutra.krama else -it.sutra.krama },
        )

        fun agendaDomain(sutra: DerivationSutra): Int = when {
            sutra.role == SutraRole.Samjna || sutra.role is SutraRole.Paribhasha || sutra.role == SutraRole.Atidesha -> 0
            sutra.role is SutraRole.Adhikara || sutra.role == SutraRole.Anuvrtti -> 1
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
        "$selectionSutra selected $selectedAffix, but $expectedAffixUpadesha was required."
    }
    val appliedSutras = applications.mapTo(mutableSetOf()) { it.sutra }
    require(requiredSutras.all { it in appliedSutras }) {
        "Incomplete derivation for $expectedAffixUpadesha; missing ${requiredSutras - appliedSutras}."
    }
    require(final.stage.ordinal >= expectedStage.ordinal) {
        val recentApplications = applications.takeLast(10).joinToString { it.sutra }
        "Incomplete derivation for $expectedAffixUpadesha; expected at least $expectedStage, reached ${final.stage}. Recent rules: $recentApplications."
    }
}
