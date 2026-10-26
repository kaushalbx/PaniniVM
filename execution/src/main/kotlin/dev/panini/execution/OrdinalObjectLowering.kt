package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.execution.binding.CanonicalDhatuIdentity
import dev.panini.execution.binding.canonicalDhatuIdentity
import dev.panini.vyakaranam.ast.*

/** Projects an agreeing ordinal object phrase onto the existing retrieval frame.
 * The projected locative is internal IR-facing morphology, not required source syntax.
 */
object OrdinalObjectLowering {
    data class Diagnostic(
        override val code: String,
        override val message: String,
        override val span: DocumentSourceSpan?,
        override val offendingText: String,
    ) : SemanticDiagnostic<String>

    sealed interface Analysis {
        data object NotOrdinalObject : Analysis
        data class Resolved(val sequence: Sequence) : Analysis
        data class Invalid(val diagnostic: Diagnostic) : Analysis
    }

    private class InvalidOrdinalObject(val code: String, message: String) : IllegalArgumentException(message)

    private fun requireOrdinal(value: Boolean, code: String, message: () -> String) {
        if (!value) throw InvalidOrdinalObject(code, message())
    }

    fun analyze(node: Invocation): Analysis = try {
        expandInternal(node)?.let(Analysis::Resolved) ?: Analysis.NotOrdinalObject
    } catch (error: InvalidOrdinalObject) {
        Analysis.Invalid(Diagnostic(error.code, error.message.orEmpty(), node.relationSpan(), node.sourceText))
    }

    fun expand(node: Invocation): Sequence? = when (val analysis = analyze(node)) {
        Analysis.NotOrdinalObject -> null
        is Analysis.Resolved -> analysis.sequence
        is Analysis.Invalid -> throw PvmSemanticException(ExecutionDiagnostic.from(analysis.diagnostic))
    }

    private fun expandInternal(node: Invocation): Sequence? {
        val sentence = node.vakya as? AkhyataVakya ?: return null
        if (sentence.tinganta.canonicalDhatuIdentity() != CanonicalDhatuIdentity.GRAH ||
            sentence.tinganta.upasargas.isNotEmpty()) return null
        val head = sentence.padas.filterIsInstance<SubantaPada>().singleOrNull {
            (it.pratipadika as? MulaPratipadika)?.lexicalIdentity == MulaPratipadikaIdentity.MULYA &&
                it.sup.affixCandidates == listOf(SupAffix.AM)
        } ?: return null
        val ordinals = sentence.padas.filter {
            val nominalSup = when (it) {
                is SubantaPada -> it.sup
                is SankhyaPuranaPada -> it.sup
                else -> null
            }
            PuranaPratyayaResolver.ordinalValue(it) != null &&
                nominalSup?.supports(Vibhakti.SASTHI) != true &&
                nominalSup?.supports(Vibhakti.PANCHAMI) != true
        }
        if (ordinals.isEmpty()) return null
        requireOrdinal((head.pratipadika as MulaPratipadika).vikaras.isEmpty(),
            "ORDINAL_OBJECT_DERIVED_HEAD") {
            "Ordinal retrieval requires an underived value object; derived nominals need their own semantics."
        }
        requireOrdinal(ordinals.size == 1, "ORDINAL_OBJECT_CARDINALITY") {
            "Ordinal retrieval requires one unambiguous ordinal modifier."
        }
        val ordinal = ordinals.single()
        val sup = when (ordinal) {
            is SubantaPada -> ordinal.sup
            is SankhyaPuranaPada -> ordinal.sup
            else -> return null
        }
        // Locative position frames remain supported independently of object phrases.
        if (sup.affixCandidates == listOf(SupAffix.NGI)) return null
        requireOrdinal(sup.affixCandidates == head.sup.affixCandidates, "ORDINAL_OBJECT_CASE_NUMBER_AGREEMENT") {
            "The ordinal must agree with its singular accusative object."
        }
        val headGenders = dev.panini.execution.binding.NominalGenderAgreement.genders(head)
        requireOrdinal(dev.panini.core.Linga.NAPUMSAKA in headGenders, "ORDINAL_OBJECT_HEAD_GENDER") {
            "The value object must retain its neuter lexical gender."
        }
        if (ordinal is SubantaPada) {
            requireOrdinal(dev.panini.execution.binding.NominalGenderAgreement.compatible(ordinal, head),
                "ORDINAL_OBJECT_GENDER_AGREEMENT") {
                "The ordinal gender must agree with its value object."
            }
        }
        val source = sentence.padas.filterIsInstance<SubantaPada>().singleOrNull {
            it !== head && it !== ordinal &&
                (it.sup.supports(Vibhakti.SASTHI) || it.sup.supports(Vibhakti.PANCHAMI))
        } ?: return null
        requireOrdinal(sentence.padas.size == 4, "ORDINAL_OBJECT_FRAME_CARDINALITY") {
            "Ordinal retrieval requires one whole, ordinal, object, and verb."
        }
        requireOrdinal(source.sup.affixCandidates == listOf(SupAffix.NGAS) ||
            source.sup.affixCandidates == listOf(SupAffix.NGASI), "ORDINAL_OBJECT_SOURCE_NUMBER") {
            "Ordinal retrieval requires one singular source referent."
        }
        val locative = SupPratyaya("ङि", "ङि")
        val projectedOrdinal = when (ordinal) {
            is SubantaPada -> ordinal.copy(sup = locative,
                sourceText = "${ordinal.pratipadika.sourceText} + ङि").inheritLocation(ordinal)
            is SankhyaPuranaPada -> ordinal.copy(sup = locative,
                sourceText = ordinal.stems.joinToString(" + ") + " + ङि")
            else -> error("Unsupported ordinal")
        }
        val projected = sentence.padas.map {
            when {
                it === ordinal -> projectedOrdinal
                it === source -> source.copy(sup = SupPratyaya("ङसिँ", "ङसिँ"),
                    sourceText = "${source.pratipadika.sourceText} + ङसिँ").inheritLocation(source)
                else -> it
            }
        }
        return Sequence(node.sourceText, listOf(Invocation(sentence.copy(padas = projected))), emptyList())
    }
}
