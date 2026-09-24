package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.AkhyataVakya
import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.Sequence
import dev.panini.vyakaranam.ast.SubantaPada

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
            sequence.statements.drop(1).filterIsInstance<Invocation>().mapNotNull(::verboseAssignment)
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
                replacement = "$target + ङे दा + लोट् + सिप्",
            )
        }
    }

    private fun verboseAssignment(invocation: Invocation): Pair<Invocation, String>? {
        val vakya = invocation.vakya as? AkhyataVakya ?: return null
        if (vakya.tinganta.dhatu.mulaDhatu != "दा") return null
        val subantas = vakya.padas.filterIsInstance<SubantaPada>()
        val phala = subantas.indexOfFirst {
            it.stem() == "फल" && it.vibhakti() == Vibhakti.DVITIYA
        }
        if (phala <= 0 || subantas.take(phala).none { it.vibhakti() == Vibhakti.SASTHI }) return null
        val target = subantas.drop(phala + 1).singleOrNull { it.vibhakti() == Vibhakti.CHATURTHI }
            ?: return null
        return invocation to (target.stem() ?: return null)
    }

    private fun SubantaPada.stem(): String? = (pratipadika as? MulaPratipadika)?.text

    private fun SubantaPada.vibhakti(): Vibhakti? = SupAffix.fromUpadesha(sup.text)?.vibhakti

}
