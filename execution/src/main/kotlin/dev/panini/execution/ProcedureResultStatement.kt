package dev.panini.execution

import dev.panini.core.*
import dev.panini.execution.binding.PhalaReference
import dev.panini.execution.binding.CanonicalDhatuIdentity
import dev.panini.execution.binding.canonicalDhatuIdentity
import dev.panini.vyakaranam.ast.*

/** A procedure-owned nominative result predication, not a lexical return command. */
object ProcedureResultStatement {
    data class Relation(val role: SubantaPada, val value: Pada)

    data class Diagnostic(
        override val code: String,
        override val message: String,
        override val span: DocumentSourceSpan?,
        override val offendingText: String,
    ) : SemanticDiagnostic<String>

    sealed interface Analysis {
        data object NotResult : Analysis
        data class Resolved(val relation: Relation) : Analysis
        data class Invalid(val diagnostic: Diagnostic) : Analysis
    }

    fun analyze(node: Invocation, roleShadowed: Boolean = false): Analysis {
        if (roleShadowed) return Analysis.NotResult
        val sentence = node.vakya as? AkhyataVakya ?: return Analysis.NotResult
        val verb = sentence.tinganta
        if (verb.canonicalDhatuIdentity() != CanonicalDhatuIdentity.AS || verb.lakara != Lakara.LAT ||
            verb.upasargas.isNotEmpty() || verb.dhatu.sanadiPratyayas.isNotEmpty()) return Analysis.NotResult
        val roles = sentence.padas.filterIsInstance<SubantaPada>().filter {
            PhalaReference.isReference(it) && it.sup.supports(Vibhakti.PRATHAMA, Vacana.EKAVACANA)
        }
        if (roles.isEmpty()) return Analysis.NotResult
        val values = sentence.padas.filter { it !== verb && roles.none { role -> role === it } }
        // Instrumental/ablative comparisons and modified history references are other constructions.
        if (values.any { it !is SupBearingPada || !it.sup.supports(Vibhakti.PRATHAMA) }) return Analysis.NotResult
        fun invalid(code: String, message: String) = Analysis.Invalid(Diagnostic(
            code, message, node.relationSpan(), node.sourceText))
        if (roles.size != 1 || values.size != 1) return invalid(
            "PROCEDURE_RESULT_CARDINALITY",
            "A procedure-result statement requires one result role and one value.",
        )
        if (verb.ting.affix != TingAffix.TIP) return invalid(
            "PROCEDURE_RESULT_AGREEMENT",
            "The singular result role requires a third-person singular copula.",
        )
        return Analysis.Resolved(Relation(roles.single(), values.single()))
    }

    fun resolve(node: Invocation, roleShadowed: Boolean = false): Relation? = when (
        val analysis = analyze(node, roleShadowed)
    ) {
        Analysis.NotResult -> null
        is Analysis.Resolved -> analysis.relation
        is Analysis.Invalid -> throw PvmSemanticException(ExecutionDiagnostic.from(analysis.diagnostic))
    }
}
