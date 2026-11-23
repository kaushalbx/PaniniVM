package dev.panini.execution

import dev.panini.execution.binding.NominalGenderAgreement
import dev.panini.execution.binding.NumeralAstNormalizer
import dev.panini.vyakaranam.ast.*

/**
 * Top-level तद् refers back to a participant of the immediately preceding top-level sentence
 * (तद् is पूर्वप्रक्रान्तपरामर्शी: it takes up what was just introduced).
 *
 * Candidates are that sentence's nominal participants, excluding फल, which always names the latest
 * result, and literal numerals. Number comes from each antecedent's own sup and gender only from
 * lexical evidence ([NominalGenderAgreement]). Selection uses the same [ReferentAgreementResolver] as
 * procedure and foreach scopes: an ambiguous or under-evidenced antecedent is an error, never a guess by
 * recency. When nothing in that sentence agrees, the तद् is left to the procedure, foreach or session scope,
 * which resolves it or reports it missing. A resolved तद् is rewritten to the antecedent's name with its own sup, so it names the
 * same live variable in both backends.
 *
 * Loops, foreach, and reductions are left untouched: their तद् is resolved in their own local scope.
 */
internal object TopLevelAnaphora {
    /** Rewrites each top-level sentence's तद् against the sentence written just before it. */
    fun resolve(items: List<VyakaranamNode>): List<VyakaranamNode> {
        var previous: Ukti? = null
        return items.map { item ->
            val resolved = if (item is Ukti) resolveSentence(item, previous) else item
            previous = resolved as? Ukti
            resolved
        }
    }

    /**
     * The lexical genders of the antecedents the sentence's top-level तद् resolve to, for rendering the
     * pronoun in its antecedent's gender (सङ्ख्या … ताम्). Empty when nothing resolves.
     */
    fun antecedentLingas(sentence: Ukti, previous: Ukti?): Set<dev.panini.core.Linga> {
        if (!isLinear(sentence.body) || anaphors(sentence.body).isEmpty()) return emptySet()
        val candidates = previous?.let { antecedents(it.body) }.orEmpty()
        val genders = candidates.associate { it.identity to it.agreement.genders }
        return invocations(sentence.body).flatMapTo(mutableSetOf()) { invocation ->
            when (val analysis = AnaphoricOperandProjection.analyze(invocation, candidates, referenceName = { it })) {
                is AnaphoricOperandProjection.Analysis.Resolved -> analysis.projection.operands
                    .mapNotNull { genders[it.identity]?.singleOrNull() }
                is AnaphoricOperandProjection.Analysis.Invalid -> emptyList()
            }
        }
    }

    private fun resolveSentence(sentence: Ukti, previous: Ukti?): Ukti {
        if (!isLinear(sentence.body) || anaphors(sentence.body).isEmpty()) return sentence
        val candidates = previous?.let { antecedents(it.body) }.orEmpty()
        return sentence.copy(body = rewrite(sentence.body, candidates))
    }

    private fun isLinear(node: ProgramNode): Boolean = when (node) {
        is Invocation -> true
        is Sequence -> node.statements.all(::isLinear)
        is Conditional -> isLinear(node.condition) && isLinear(node.consequent) &&
            (node.alternate?.let(::isLinear) ?: true) && node.conjuncts.all(::isLinear)
        else -> false
    }

    private fun invocations(node: ProgramNode): List<Invocation> = when (node) {
        is Invocation -> listOf(node)
        is Sequence -> node.statements.flatMap(::invocations)
        is Conditional -> invocations(node.condition) + node.conjuncts + invocations(node.consequent) +
            node.alternate?.let(::invocations).orEmpty()
        else -> emptyList()
    }

    private fun nominals(invocation: Invocation): List<SubantaPada> = invocation.vakya.padas.flatMap { pada ->
        when (pada) {
            is SubantaPada -> listOf(pada)
            is SamuccitaSubanta -> pada.members.filterIsInstance<SubantaPada>()
            else -> emptyList()
        }
    }

    private fun anaphors(node: ProgramNode): List<SubantaPada> =
        invocations(node).flatMap(::nominals).filter { AnaphoricOperandProjection.agreement(it) != null }

    /** One candidate per named participant; its numbers are those its sups in that sentence allow. */
    private fun antecedents(node: ProgramNode): List<AgreementCandidate<String>> {
        val byName = linkedMapOf<String, MutableList<SubantaPada>>()
        invocations(node).flatMap(::nominals).forEach { pada ->
            val stem = pada.pratipadika as? MulaPratipadika ?: return@forEach
            if (stem.vikaras.isNotEmpty() || stem.text == "फल" || stem.text == "तद्") return@forEach
            if (NumeralAstNormalizer.resolve(stem) != null) return@forEach
            byName.getOrPut(stem.text) { mutableListOf() } += pada
        }
        return byName.map { (name, occurrences) ->
            AgreementCandidate(name, ReferentAgreement(
                occurrences.flatMapTo(mutableSetOf()) { pada -> pada.sup.affixCandidates.map { it.vacana } },
                NominalGenderAgreement.genders(occurrences.first()),
            ))
        }
    }

    private fun rewrite(node: ProgramNode, candidates: List<AgreementCandidate<String>>): ProgramNode = when (node) {
        is Invocation -> when (val analysis = AnaphoricOperandProjection.analyze(node, candidates,
            referenceName = { it })) {
            is AnaphoricOperandProjection.Analysis.Resolved -> analysis.projection.invocation
            is AnaphoricOperandProjection.Analysis.Invalid -> when (analysis.diagnostic.code) {
                // Nothing in the preceding sentence agrees: the तद् is not a top-level reference here,
                // so the procedure, foreach or session scope resolves it, or reports it missing.
                AnaphoricOperandProjection.DiagnosticCode.MISSING_ANTECEDENT -> node
                else -> throw PvmSemanticException(ExecutionDiagnostic(
                    code = analysis.diagnostic.code.name,
                    message = analysis.diagnostic.message +
                        " A top-level तद् refers to a participant of the preceding sentence" +
                        analysis.diagnostic.candidates.takeIf { it.isNotEmpty() }
                            ?.joinToString(", ", prefix = " (candidates: ", postfix = ")").orEmpty() + ".",
                    span = analysis.diagnostic.span,
                    offendingText = analysis.diagnostic.offendingText,
                ))
            }
        }
        is Sequence -> node.copy(statements = node.statements.map { rewrite(it, candidates) })
        is Conditional -> node.copy(
            condition = rewrite(node.condition, candidates),
            consequent = rewrite(node.consequent, candidates),
            alternate = node.alternate?.let { rewrite(it, candidates) },
            conjuncts = node.conjuncts.map { rewrite(it, candidates) as Invocation },
        )
        else -> node
    }
}
