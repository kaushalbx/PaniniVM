package dev.panini.derivation

import dev.panini.core.ItMarker
import dev.panini.core.Kala
import dev.panini.core.Lakara
import dev.panini.core.Linga
import dev.panini.core.Prayoga
import dev.panini.core.Purusha
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.shiksha.Samjna


/** Conditions and operations shared by sutras whose grammar is data-like. */
fun interface DerivationCondition {
    fun matches(state: DerivationState): Boolean
}

class AllOf(
    private vararg val conditions: DerivationCondition,
) : DerivationCondition {
    override fun matches(state: DerivationState): Boolean = conditions.all { it.matches(state) }
}

class Not(
    private val condition: DerivationCondition,
) : DerivationCondition {
    override fun matches(state: DerivationState): Boolean = !condition.matches(state)
}

fun interface DerivationOperation {
    fun apply(state: DerivationState): DerivationChange
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

class HasRequestedMeaning(
    private val meaning: DerivationalMeaning,
) : DerivationCondition {
    override fun matches(state: DerivationState): Boolean =
        state.effectiveContext.requestedMeaning == meaning
}

class HasDerivationalEnvironment(
    private val environment: DerivationalEnvironment,
) : DerivationCondition {
    override fun matches(state: DerivationState): Boolean =
        state.effectiveContext.has(environment)
}

class HasKala(
    private val kala: Kala,
) : DerivationCondition {
    override fun matches(state: DerivationState): Boolean = state.effectiveContext.kala == kala
}

class HasMorphosyntax(
    private val linga: Linga? = null,
    private val vibhakti: Vibhakti? = null,
    private val vacana: Vacana? = null,
    private val purusha: Purusha? = null,
    private val prayoga: Prayoga? = null,
    private val lakara: Lakara? = null,
) : DerivationCondition {
    override fun matches(state: DerivationState): Boolean = state.effectiveContext.rupa.let { morphology ->
        (linga == null || morphology.linga == linga) &&
            (vibhakti == null || morphology.vibhakti == vibhakti) &&
            (vacana == null || morphology.vacana == vacana) &&
            (purusha == null || morphology.purusha == purusha) &&
            (prayoga == null || morphology.prayoga == prayoga) &&
            (lakara == null || morphology.lakara == lakara)
    }
}

class AtDerivationStage(
    private val stage: DerivationStage,
) : DerivationCondition {
    override fun matches(state: DerivationState): Boolean = state.stage == stage
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
