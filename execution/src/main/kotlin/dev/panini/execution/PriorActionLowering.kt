package dev.panini.execution

import dev.panini.core.KrtAffix
import dev.panini.execution.binding.CanonicalDhatuIdentity
import dev.panini.execution.binding.canonicalDhatuIdentity
import dev.panini.vyakaranam.ast.*

/** Shares prior-action ordering between interpreter binding and compiler lowering.
 * Source avyaya derivations remain intact. Finite heads below are internal binding
 * projections, not new source sentences or claims about the nonfinite word's tense.
 */
object PriorActionLowering {
    data class Diagnostic(
        override val code: String,
        override val message: String,
        override val span: DocumentSourceSpan?,
        override val offendingText: String,
    ) : SemanticDiagnostic<String>

    sealed interface Analysis {
        data object NotPriorAction : Analysis
        data class Resolved(val sequence: Sequence) : Analysis
        data class Invalid(val diagnostic: Diagnostic) : Analysis
    }

    private class InvalidPriorAction(val code: String, message: String) : IllegalArgumentException(message)

    fun lower(root: ProgramNode): ProgramNode = object : ProgramNodeTransformer() {
        override fun visitInvocation(node: Invocation): ProgramNode = expand(node) ?: node
    }.transform(root)

    fun analyze(node: Invocation): Analysis = try {
        expandInternal(node)?.let(Analysis::Resolved) ?: Analysis.NotPriorAction
    } catch (error: InvalidPriorAction) {
        Analysis.Invalid(Diagnostic(error.code, error.message.orEmpty(), node.relationSpan(), node.sourceText))
    }

    fun expand(node: Invocation): Sequence? = when (val analysis = analyze(node)) {
        Analysis.NotPriorAction -> null
        is Analysis.Resolved -> analysis.sequence
        is Analysis.Invalid -> throw PvmSemanticException(ExecutionDiagnostic.from(analysis.diagnostic))
    }

    private fun expandInternal(node: Invocation): Sequence? {
        val sentence = node.vakya as? AkhyataVakya ?: return null
        if (sentence.tinganta.priorAction != null || sentence.padas.none {
            ((it as? AvyayaPada)?.derivation as? AvyayaKridantaDerivation)?.krtAffix in setOf(KrtAffix.KTVA, KrtAffix.LYAP)
        }) return null
        // Execution's established range-exclusion interpretation is distinct
        // from the ordinary prior-action operation chain.
        val specialized = NaturalSemanticNormalizer.normalize(node)
        if (specialized is NaturalSemanticNormalizer.Operation.RangeChoice && specialized.usesExclusionAbsolutive) return null
        if (sentence.tinganta.vikarana == Vikarana.YAK) throw InvalidPriorAction(
            "PRIOR_ACTION_PASSIVE_MAIN_ACTION",
            "Execution of a passive main action with prior actions is not yet supported; grammar analysis is separate.")
        val sequence = when (val structure = PriorActionStructure.analyze(node, shareOmittedAgents = true)) {
            PriorActionStructure.Analysis.NotPriorAction -> return null
            is PriorActionStructure.Analysis.Invalid -> throw InvalidPriorAction(structure.diagnostic.code, structure.diagnostic.message)
            is PriorActionStructure.Analysis.Resolved -> structure.sequence
        }
        val statements = sequence.statements.mapIndexed { index, statement ->
            val invocation = statement as Invocation
            val clause = invocation.vakya as AkhyataVakya
            val head = clause.tinganta
            val subjects = PriorActionStructure.nominatives(clause.padas)
            // This discourse completion is execution-only. Grammar frames keep
            // the written operands and do not invent a phala participant.
            val omittedDisplayObject = index > 0 && head.canonicalDhatuIdentity() == CanonicalDhatuIdentity.MUDR &&
                clause.padas.all { it is TingantaPada || it is AvyayaPada || it in subjects }
            val completed = if (!omittedDisplayObject) invocation else invocation.copy(vakya = clause.copy(
                padas = clause.padas.filterNot { it === head } + SubantaPada(
                    "", MulaPratipadika("फल", "फल"), SupPratyaya("अम्", "अम्")) + head))
            when (val member = CollectionMemberMorphology.analyze(completed)) {
                CollectionMemberMorphology.Analysis.Valid -> Unit
                is CollectionMemberMorphology.Analysis.Invalid ->
                    throw InvalidPriorAction(member.diagnostic.code, member.diagnostic.message)
            }
            completed
        }
        return sequence.copy(statements = statements)
    }
}
