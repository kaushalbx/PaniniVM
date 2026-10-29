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
        override val corrections: List<SourceCorrection> = emptyList(),
    ) : SemanticDiagnostic<String>

    sealed interface Analysis {
        data object Valid : Analysis
        data class Invalid(val diagnostic: Diagnostic) : Analysis
    }

    private class InvalidAgreement(
        val code: String,
        message: String,
        val corrections: List<SourceCorrection> = emptyList(),
    ) : IllegalArgumentException(message)

    private fun requireAgreement(
        value: Boolean,
        code: String,
        corrections: List<SourceCorrection> = emptyList(),
        message: () -> String,
    ) {
        if (!value) throw InvalidAgreement(code, message(), corrections)
    }

    fun analyze(node: Invocation): Analysis = try {
        validateInternal(node)
        Analysis.Valid
    } catch (error: InvalidAgreement) {
        Analysis.Invalid(Diagnostic(
            error.code,
            error.message.orEmpty(),
            error.corrections.firstOrNull()?.span ?: node.relationSpan(),
            node.sourceText,
            error.corrections,
        ))
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
                "COPULAR_SUBJECT_NUMBER_AGREEMENT",
                listOfNotNull(
                    nominativeCorrection(subjects.single(), verb.vacana),
                    finiteVerbCorrection(sentence.tinganta, subjects.single()),
                )) {
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
                "COPULAR_PREDICATE_NUMBER_AGREEMENT",
                listOfNotNull(nominativeCorrection(pada, verb.vacana))) {
                "Copular predicate and finite verb must agree in grammatical number."
            }
        }
    }

    private fun nominativeCorrection(pada: SubantaPada, vacana: dev.panini.core.Vacana): SourceCorrection? {
        val span = pada.sourceSpan ?: return null
        val expected = dev.panini.core.SupAffix.select(Vibhakti.PRATHAMA, vacana)
        return SourceCorrection(
            span = span,
            replacement = "${pada.pratipadika.sourceText} + ${expected.initialSurface}",
            description = "Make the nominative nominal agree with the finite verb in ${vacana.name.lowercase()}.",
        )
    }

    private fun finiteVerbCorrection(tinganta: TingantaPada, subject: SubantaPada): SourceCorrection? {
        val span = tinganta.sourceSpan ?: return null
        val current = tinganta.ting.affix ?: return null
        val subjectNumber = subject.sup.affixCandidates.map { it.vacana }.distinct().singleOrNull() ?: return null
        val expected = dev.panini.core.TingAffix.select(current.purusha, subjectNumber, current.pada) ?: return null
        val replacement = buildList {
            addAll(tinganta.upasargas)
            add(tinganta.dhatu.sourceText)
            tinganta.vikarana?.let { add(it.upadesha) }
            add(tinganta.lakara.upadesha)
            add(expected.initialSurface)
        }.joinToString(" + ")
        return SourceCorrection(
            span = span,
            replacement = replacement,
            description = "Make the finite verb agree with the subject in ${subjectNumber.name.lowercase()}.",
        )
    }
}
