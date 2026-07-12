package dev.sanskrit.derivation


/** Conditions and operations shared by sutras whose grammar is data-like. */
fun interface DerivationCondition {
    fun matches(state: DerivationState): Boolean
}

class AllOf(
    private vararg val conditions: DerivationCondition,
) : DerivationCondition {
    override fun matches(state: DerivationState): Boolean = conditions.all { it.matches(state) }
}

class AnyOf(
    private vararg val conditions: DerivationCondition,
) : DerivationCondition {
    override fun matches(state: DerivationState): Boolean = conditions.any { it.matches(state) }
}

class Not(
    private val condition: DerivationCondition,
) : DerivationCondition {
    override fun matches(state: DerivationState): Boolean = !condition.matches(state)
}

fun interface DerivationOperation {
    fun apply(state: DerivationState): DerivationChange
}

interface BranchingDerivationOperation : DerivationOperation {
    fun applyAll(state: DerivationState): List<DerivationChange>

    override fun apply(state: DerivationState): DerivationChange = applyAll(state).first()
}

class OptionalOperation(
    private val operation: DerivationOperation,
    private val skipExplanation: String,
) : BranchingDerivationOperation {
    override fun applyAll(state: DerivationState): List<DerivationChange> = listOf(
        operation.apply(state),
        DerivationChange(state, skipExplanation, applied = false),
    )
}

class SequenceOf(
    private vararg val operations: DerivationOperation,
) : DerivationOperation {
    override fun apply(state: DerivationState): DerivationChange {
        var current = state
        val explanations = mutableListOf<String>()
        operations.forEach { operation ->
            val change = operation.apply(current)
            current = change.state
            explanations += change.explanation
        }
        return DerivationChange(current, explanations.joinToString(separator = " → "))
    }
}

class TermsContainAnyMark(
    private val marks: Set<String>,
    private val missingSamjna: Samjna? = null,
) : DerivationCondition {
    override fun matches(state: DerivationState): Boolean = state.terms.any { term ->
        term.surface.any { mark -> mark.toString() in marks } &&
            (missingSamjna == null || SamjnaAssignment(term.id, missingSamjna) !in state.samjnas)
    }
}

class AssignSamjnaToTermsContaining(
    private val marks: Set<String>,
    private val samjna: Samjna,
    private val explanation: String,
) : DerivationOperation {
    override fun apply(state: DerivationState): DerivationChange {
        val assignments = state.terms
            .filter { term -> term.surface.any { mark -> mark.toString() in marks } }
            .map { term -> SamjnaAssignment(term.id, samjna) }
            .toSet()
        return DerivationChange(state.withSamjnas(assignments), explanation)
    }
}

class HasTermKind(
    private val kind: TermKind,
) : DerivationCondition {
    override fun matches(state: DerivationState): Boolean = state.terms.any { it.kind == kind }
}

class HasItMarker(
    private val marker: ItMarker,
) : DerivationCondition {
    override fun matches(state: DerivationState): Boolean = state.terms.any { marker in it.itMarkers }
}

class HasSemanticFeature(
    private val feature: SemanticFeature,
) : DerivationCondition {
    override fun matches(state: DerivationState): Boolean = feature in state.semanticFeatures
}

class HasSamjna(
    private val samjna: Samjna,
    private val targetId: String? = null,
) : DerivationCondition {
    override fun matches(state: DerivationState): Boolean = state.samjnas.any {
        it.samjna == samjna && (targetId == null || it.targetId == targetId)
    }
}

class AtDerivationStage(
    private val stage: DerivationStage,
) : DerivationCondition {
    override fun matches(state: DerivationState): Boolean = state.stage == stage
}

class HasActiveAdhikara(
    private val sutraNumber: String,
) : DerivationCondition {
    override fun matches(state: DerivationState): Boolean = sutraNumber in state.activeAdhikaras
}

class HasAnuvrtti(
    private val item: String,
) : DerivationCondition {
    override fun matches(state: DerivationState): Boolean = item in state.inheritedAnuvrtti
}

class IntroduceTerm(
    private val term: DerivationTerm,
    private val nextStage: DerivationStage = DerivationStage.PRATYAYA_SELECTED,
    private val explanation: String,
) : DerivationOperation {
    override fun apply(state: DerivationState): DerivationChange = DerivationChange(
        state = state.addTerm(term).copy(stage = nextStage),
        explanation = explanation,
    )
}

class AdvanceDerivationStage(
    private val nextStage: DerivationStage,
    private val explanation: String,
) : DerivationOperation {
    override fun apply(state: DerivationState): DerivationChange = DerivationChange(
        state = state.copy(stage = nextStage),
        explanation = explanation,
    )
}

class ActivateAdhikara(
    private val sutraNumber: String,
    private val explanation: String,
) : DerivationOperation {
    override fun apply(state: DerivationState): DerivationChange = DerivationChange(
        state = state.activateAdhikara(sutraNumber),
        explanation = explanation,
    )
}

class CarryAnuvrtti(
    private val item: String,
    private val explanation: String,
) : DerivationOperation {
    override fun apply(state: DerivationState): DerivationChange = DerivationChange(
        state = state.carryAnuvrtti(item),
        explanation = explanation,
    )
}

class BlockSutra(
    private val targetSutra: String,
    private val blocker: String,
    private val explanation: String,
) : DerivationOperation {
    override fun apply(state: DerivationState): DerivationChange = DerivationChange(
        state = state.blockSutra(targetSutra, blocker),
        explanation = explanation,
    )
}
