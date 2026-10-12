package dev.panini.execution

import dev.panini.core.Linga
import dev.panini.core.Vacana

/** Empty sets mean unknown evidence, not a wildcard that proves agreement. */
data class ReferentAgreement(
    val numbers: Set<Vacana> = emptySet(),
    val genders: Set<Linga> = emptySet(),
) {
    companion object {
        fun fromDeclaration(declaration: dev.panini.vyakaranam.ast.SubantaPada?): ReferentAgreement {
            val genders = dev.panini.execution.binding.NominalGenderAgreement.genders(declaration)
            return ReferentAgreement(declaration?.sup?.affixCandidates
                ?.mapTo(mutableSetOf()) { it.vacana }.orEmpty(), genders)
        }
    }
}

data class AgreementCandidate<T>(val identity: T, val agreement: ReferentAgreement)

sealed interface ReferentAgreementResolution<out T> {
    data class Selected<T>(val identity: T) : ReferentAgreementResolution<T>
    data class Ambiguous<T>(val candidates: List<T>) : ReferentAgreementResolution<T>
    data class IncompleteEvidence<T>(val identity: T) : ReferentAgreementResolution<T>
    data object Missing : ReferentAgreementResolution<Nothing>
}

/** Selects identities only; it neither guesses gender nor inspects payload shape/recency. */
object ReferentAgreementResolver {
    fun <T> resolve(reference: ReferentAgreement, candidates: List<AgreementCandidate<T>>): ReferentAgreementResolution<T> {
        require(candidates.map { it.identity }.distinct().size == candidates.size) {
            "Agreement candidates must have distinct referent identities; deduplicate aliases before resolution."
        }
        fun <E> compatible(left: Set<E>, right: Set<E>) =
            left.isEmpty() || right.isEmpty() || left.any { it in right }
        val possible = candidates.filter { candidate ->
            compatible(reference.numbers, candidate.agreement.numbers) &&
                compatible(reference.genders, candidate.agreement.genders)
        }
        if (possible.isEmpty()) return ReferentAgreementResolution.Missing
        if (possible.size > 1) return ReferentAgreementResolution.Ambiguous(possible.map { it.identity })
        val candidate = possible.single()
        return if (reference.numbers.isEmpty() || reference.genders.isEmpty() ||
            candidate.agreement.numbers.isEmpty() || candidate.agreement.genders.isEmpty()) {
            ReferentAgreementResolution.IncompleteEvidence(candidate.identity)
        } else ReferentAgreementResolution.Selected(candidate.identity)
    }
}
