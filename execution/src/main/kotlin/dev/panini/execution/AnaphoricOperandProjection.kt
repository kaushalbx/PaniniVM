package dev.panini.execution

import dev.panini.core.Linga
import dev.panini.vyakaranam.ast.*

/** AST-only projection of supported anaphors; सर्वनाम classification alone is not enough. */
object AnaphoricOperandProjection {
    data class Operand<T>(val name: String, val identity: T, val original: SubantaPada)
    data class Projection<T>(val invocation: Invocation, val operands: List<Operand<T>>)

    enum class DiagnosticCode { AMBIGUOUS_ANTECEDENT, INCOMPLETE_AGREEMENT, MISSING_ANTECEDENT }
    data class Diagnostic<T>(
        override val code: DiagnosticCode,
        override val message: String,
        val anaphor: SubantaPada,
        val candidates: List<T> = emptyList(),
        override val span: DocumentSourceSpan? = anaphor.sourceSpan,
        override val offendingText: String = anaphor.sourceText,
    ) : SemanticDiagnostic<DiagnosticCode>
    sealed interface Analysis<out T> {
        data class Resolved<T>(val projection: Projection<T>) : Analysis<T>
        data class Invalid<T>(val diagnostic: Diagnostic<T>) : Analysis<T>
    }

    class ResolutionException(val diagnostic: Diagnostic<*>) : IllegalArgumentException(diagnostic.message)

    fun agreement(pada: SubantaPada): ReferentAgreement? {
        val stem = pada.pratipadika as? MulaPratipadika ?: return null
        if (stem.text != "तद्" || stem.vikaras.isNotEmpty()) return null
        // Segmented तद् does not specify one gender. Preserve all possibilities;
        // resolution selects an antecedent identity, not a guessed gender.
        return ReferentAgreement(pada.sup.affixCandidates.mapTo(mutableSetOf()) { it.vacana },
            Linga.entries.toSet())
    }

    fun <T> analyze(invocation: Invocation, candidates: List<AgreementCandidate<T>>,
        prefix: String = "__pvm_anaphor_", protectedPadas: Set<Pada> = emptySet(),
        referenceName: ((T) -> String)? = null): Analysis<T> {
        val operands = mutableListOf<Operand<T>>()
        val replacements = java.util.IdentityHashMap<SubantaPada, SubantaPada>()
        var invalid: Diagnostic<T>? = null
        fun nominal(pada: SubantaPada): SubantaPada {
            replacements[pada]?.let { return it }
            if (invalid != null) return pada
            if (protectedPadas.any { it === pada }) return pada
            val agreement = agreement(pada) ?: return pada
            val identity = when (val resolution = ReferentAgreementResolver.resolve(agreement, candidates)) {
                is ReferentAgreementResolution.Selected -> resolution.identity
                is ReferentAgreementResolution.Ambiguous -> {
                    invalid = Diagnostic(DiagnosticCode.AMBIGUOUS_ANTECEDENT,
                        "Ambiguous तद् antecedent: ${resolution.candidates.size} compatible referents.",
                        pada, resolution.candidates)
                    return pada
                }
                is ReferentAgreementResolution.IncompleteEvidence -> {
                    invalid = Diagnostic(DiagnosticCode.INCOMPLETE_AGREEMENT,
                        "Incomplete grammatical evidence for the तद् antecedent.", pada,
                        listOf(resolution.identity))
                    return pada
                }
                ReferentAgreementResolution.Missing -> {
                    invalid = Diagnostic(DiagnosticCode.MISSING_ANTECEDENT,
                        "No compatible antecedent is available for तद्.", pada)
                    return pada
                }
            }
            val name = referenceName?.invoke(identity) ?: "$prefix${operands.size}"
            operands += Operand(name, identity, pada)
            return pada.copy(sourceText = "$name + ${pada.sup.text}",
                pratipadika = MulaPratipadika(name, name)).inheritLocation(pada)
                .also { replacements[pada] = it }
        }
        val coordinated = java.util.IdentityHashMap<SamuccitaSubanta, SamuccitaSubanta>()
        fun projectPada(pada: Pada): Pada =
            if (protectedPadas.any { it === pada }) pada else when (pada) {
                is SubantaPada -> nominal(pada)
                is SamuccitaSubanta -> coordinated.getOrPut(pada) {
                    pada.copy(members = pada.members.map { if (it is SubantaPada) nominal(it) else it })
                }
                else -> pada
            }
        val padas = invocation.vakya.padas.map(::projectPada)
        val vakya = when (val original = invocation.vakya) {
            is AkhyataVakya -> original.copy(padas = padas)
            is NamaVakya -> original.copy(padas = padas)
        }
        val implicit = invocation.implicitValuePada?.let(::projectPada)
        invalid?.let { return Analysis.Invalid(it) }
        return Analysis.Resolved(Projection(
            invocation.copy(vakya = vakya, implicitValuePada = implicit), operands))
    }

    /** Compatibility boundary for callers not yet migrated to typed analysis. */
    fun <T> project(invocation: Invocation, candidates: List<AgreementCandidate<T>>,
        prefix: String = "__pvm_anaphor_", protectedPadas: Set<Pada> = emptySet(),
        referenceName: ((T) -> String)? = null): Projection<T> =
        when (val analysis = analyze(invocation, candidates, prefix, protectedPadas, referenceName)) {
            is Analysis.Resolved -> analysis.projection
            is Analysis.Invalid -> throw ResolutionException(analysis.diagnostic)
        }
}
