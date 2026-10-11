package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.AkhyataVakya
import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.Sequence
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.KridantaPratipadika
import dev.panini.vyakaranam.ast.SequenceConnector
import dev.panini.vyakaranam.ast.morphologicalKey
import dev.panini.execution.binding.NamedActionResultReferenceResolver
import dev.panini.execution.binding.CanonicalDhatuIdentity
import dev.panini.execution.binding.canonicalDhatuIdentity

data class DirectResultAssignmentSuggestion(
    val offset: Int,
    val length: Int,
    val replacement: String,
)

/** Recognizes verbose `क्रियाफलम् ... देहि` clauses that can consume the preceding ततः result. */
object DirectResultAssignment {
    fun suggestions(source: String): List<DirectResultAssignmentSuggestion> {
        val statements = runCatching { PvmScript.parse(source) }.getOrDefault(emptyList())
        val candidates = statements.filterIsInstance<PvmScriptStatement.Sentence>().flatMap { sentence ->
            val sequence = sentence.program as? Sequence ?: return@flatMap emptyList()
            sequence.statements.drop(1).mapIndexedNotNull { index, stage ->
                if (sequence.connectorKinds.getOrNull(index) != SequenceConnector.ANANTARYA) return@mapIndexedNotNull null
                val previous = sequence.statements[index] as? Invocation ?: return@mapIndexedNotNull null
                verboseAssignment(stage as? Invocation ?: return@mapIndexedNotNull null, previous)
            }
        }
        val sourceMap = SourceTextMap(source)
        var searchFrom = 0
        return candidates.mapNotNull { (invocation, target) ->
            val needle = invocation.sourceText.filterNot(Char::isWhitespace).trimEnd('।', '॥')
            val located = sourceMap.locate(needle, searchFrom) ?: return@mapNotNull null
            searchFrom = located.nextCompactOffset
            DirectResultAssignmentSuggestion(
                offset = located.span.start,
                length = located.span.length,
                replacement = "$target दा + लोट् + सिप्",
            )
        }
    }

    private fun verboseAssignment(invocation: Invocation, previous: Invocation): Pair<Invocation, String>? {
        val vakya = invocation.vakya as? AkhyataVakya ?: return null
        if (vakya.tinganta.canonicalDhatuIdentity() != CanonicalDhatuIdentity.DA) return null
        val head = vakya.tinganta
        if (head.upasargas.isNotEmpty() || head.dhatu.sanadiPratyayas.isNotEmpty() ||
            head.lakara.upadesha != "लोट्" || head.ting.text != "सिप्") return null
        val reference = NamedActionResultReferenceResolver.resolve(vakya.padas).singleOrNull() ?: return null
        if (reference.hasOrderingQualifier) return null
        val modifier = reference.modifier.pratipadika as? KridantaPratipadika ?: return null
        val previousHead = (previous.vakya as? AkhyataVakya)?.tinganta ?: return null
        val identity = modifier.dhatu.canonicalDhatuIdentity() ?: return null
        if (previousHead.canonicalDhatuIdentity() != identity) return null
        val subantas = vakya.padas.filterIsInstance<SubantaPada>()
        val phala = subantas.indexOfFirst {
            NaturalSemanticNormalizer.isPriorResult(it) &&
                it.vibhakti() == Vibhakti.DVITIYA
        }
        if (phala <= 0 || subantas.take(phala).none { it.vibhakti() == Vibhakti.SASTHI }) return null
        val target = subantas.drop(phala + 1).singleOrNull { it.vibhakti() == Vibhakti.CHATURTHI }
            ?: return null
        if (vakya.padas.any { it !== head && it !== reference.result && it !== reference.modifier && it !== target }) return null
        return invocation to "${target.pratipadika.morphologicalKey()} + ${target.sup.text}"
    }

    private fun SubantaPada.vibhakti(): Vibhakti? = SupAffix.fromUpadesha(sup.text)?.vibhakti

}
