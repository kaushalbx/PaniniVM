package dev.panini.execution

import dev.panini.core.Linga
import dev.panini.vyakaranam.ast.*

/** AST-only projection of supported anaphors; सर्वनाम classification alone is not enough. */
object AnaphoricOperandProjection {
    data class Operand<T>(val name: String, val identity: T, val original: SubantaPada)
    data class Projection<T>(val invocation: Invocation, val operands: List<Operand<T>>)

    fun agreement(pada: SubantaPada): ReferentAgreement? {
        val stem = pada.pratipadika as? MulaPratipadika ?: return null
        if (stem.text != "तद्" || stem.vikaras.isNotEmpty()) return null
        // Segmented तद् does not specify one gender. Preserve all possibilities;
        // resolution selects an antecedent identity, not a guessed gender.
        return ReferentAgreement(pada.sup.affixCandidates.mapTo(mutableSetOf()) { it.vacana },
            Linga.entries.toSet())
    }

    fun <T> project(invocation: Invocation, candidates: List<AgreementCandidate<T>>,
        prefix: String = "__pvm_anaphor_", protectedPadas: Set<Pada> = emptySet(),
        referenceName: ((T) -> String)? = null): Projection<T> {
        val operands = mutableListOf<Operand<T>>()
        val replacements = java.util.IdentityHashMap<SubantaPada, SubantaPada>()
        fun nominal(pada: SubantaPada): SubantaPada {
            replacements[pada]?.let { return it }
            if (protectedPadas.any { it === pada }) return pada
            val agreement = agreement(pada) ?: return pada
            val identity = when (val resolution = ReferentAgreementResolver.resolve(agreement, candidates)) {
                is ReferentAgreementResolution.Selected -> resolution.identity
                is ReferentAgreementResolution.Ambiguous -> throw IllegalArgumentException(
                    "Ambiguous तद् antecedent: ${resolution.candidates.size} compatible referents.")
                is ReferentAgreementResolution.IncompleteEvidence -> throw IllegalArgumentException(
                    "Incomplete grammatical evidence for the तद् antecedent.")
                ReferentAgreementResolution.Missing -> throw IllegalArgumentException(
                    "No compatible antecedent is available for तद्.")
            }
            val name = referenceName?.invoke(identity) ?: "$prefix${operands.size}"
            operands += Operand(name, identity, pada)
            return pada.copy(sourceText = "$name + ${pada.sup.text}", pratipadika = MulaPratipadika(name, name))
                .also { replacements[pada] = it }
        }
        val coordinated = java.util.IdentityHashMap<SamuccitaSubanta, SamuccitaSubanta>()
        fun projectPada(pada: Pada): Pada =
            if (protectedPadas.any { it === pada }) pada else when (pada) {
                is SubantaPada -> nominal(pada)
                is SamuccitaSubanta -> coordinated.getOrPut(pada) {
                    pada.copy(members = pada.members.map(::nominal))
                }
                else -> pada
            }
        val padas = invocation.vakya.padas.map(::projectPada)
        val vakya = when (val original = invocation.vakya) {
            is AkhyataVakya -> original.copy(padas = padas)
            is NamaVakya -> original.copy(padas = padas)
        }
        val implicit = invocation.implicitValuePada?.let(::projectPada)
        return Projection(invocation.copy(vakya = vakya, implicitValuePada = implicit), operands)
    }
}
