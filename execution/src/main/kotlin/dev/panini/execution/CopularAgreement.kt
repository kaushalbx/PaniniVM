package dev.panini.execution

import dev.panini.core.Vibhakti
import dev.panini.execution.binding.CanonicalDhatuIdentity
import dev.panini.execution.binding.canonicalDhatuIdentity
import dev.panini.vyakaranam.ast.*

/** Checks written predicate agreement without inferring runtime payload cardinality. */
object CopularAgreement {
    data class Diagnostic(
        override val code: String,
        override val message: String,
        override val span: DocumentSourceSpan?,
        override val offendingText: String,
    ) : SemanticDiagnostic<String>

    sealed interface Analysis {
        data object Valid : Analysis
        data class Invalid(val diagnostic: Diagnostic) : Analysis
    }

    private class InvalidAgreement(val code: String, message: String) : IllegalArgumentException(message)

    private fun requireAgreement(value: Boolean, code: String, message: () -> String) {
        if (!value) throw InvalidAgreement(code, message())
    }

    fun analyze(node: Invocation): Analysis = try {
        validateInternal(node)
        Analysis.Valid
    } catch (error: InvalidAgreement) {
        Analysis.Invalid(Diagnostic(error.code, error.message.orEmpty(), node.relationSpan(), node.sourceText))
    }

    fun validate(node: Invocation) {
        val analysis = analyze(node)
        if (analysis is Analysis.Invalid) throw PvmSemanticException(
            ExecutionDiagnostic.from(analysis.diagnostic),
        )
    }

    private fun validateInternal(node: Invocation) {
        val sentence = node.vakya as? AkhyataVakya ?: return
        if (sentence.tinganta.canonicalDhatuIdentity() != CanonicalDhatuIdentity.AS) return
        val verb = sentence.tinganta.ting.affix ?: return
        val predicates = sentence.padas.filterIsInstance<SubantaPada>().filter { pada ->
            (pada.pratipadika as? MulaPratipadika)?.lexicalIdentity in setOf(
                MulaPratipadikaIdentity.SAMA, MulaPratipadikaIdentity.NYUNA, MulaPratipadikaIdentity.ADHIKA) &&
                pada.sup.supports(Vibhakti.PRATHAMA)
        }
        if (predicates.isEmpty()) return
        val subjects = sentence.padas.filterIsInstance<SubantaPada>().filter { pada ->
            pada !in predicates && pada.sup.supports(Vibhakti.PRATHAMA)
        }
        if (subjects.size == 1) {
            requireAgreement(subjects.single().sup.supports(Vibhakti.PRATHAMA, verb.vacana),
                "COPULAR_SUBJECT_NUMBER_AGREEMENT") {
                "Copular subject and finite verb must agree in grammatical number."
            }
            requireAgreement(predicates.all {
                dev.panini.execution.binding.NominalGenderAgreement.compatible(it, subjects.single())
            }, "COPULAR_PREDICATE_GENDER_AGREEMENT") {
                "Copular predicate and subject have incompatible established gender evidence."
            }
        }
        for (pada in predicates) {
            val nominal = pada.pratipadika as? MulaPratipadika ?: continue
            if (nominal.lexicalIdentity !in setOf(MulaPratipadikaIdentity.SAMA,
                    MulaPratipadikaIdentity.NYUNA, MulaPratipadikaIdentity.ADHIKA)) continue
            requireAgreement(pada.sup.supports(Vibhakti.PRATHAMA, verb.vacana),
                "COPULAR_PREDICATE_NUMBER_AGREEMENT") {
                "Copular predicate and finite verb must agree in grammatical number."
            }
        }
    }
}
