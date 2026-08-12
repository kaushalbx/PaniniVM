package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.TingantaPada
import dev.panini.vyakaranam.ast.Ukti
import dev.panini.vyakaranam.parser.PaniniParser

data class SamjnaInvocationShape(
    val operationStem: String,
    val domainStem: String?,
    val karmaText: String,
    val ukti: Ukti,
)

/** Extracts invocation identity and grammatical roles from the parsed utterance. */
object SamjnaInvocationMatcher {
    private val parser = PaniniParser()

    fun match(
        sentenceText: String,
        knownOperationStems: Set<String>,
        preParsedUkti: Ukti? = null,
    ): SamjnaInvocationShape? {
        val ukti = preParsedUkti ?: runCatching { parser.parse(sentenceText.trim()) }.getOrNull() ?: return null
        val padas = ukti.grammaticalVakyas().flatMap { it.padas }
        val verbIndex = padas.indexOfFirst { it is TingantaPada }
        if (verbIndex < 0) return null

        val instrumental = padas.withIndex().firstOrNull { (index, pada) ->
            index < verbIndex && pada is SubantaPada &&
                pada.vibhakti() == Vibhakti.TRTIYA &&
                pada.pratipadika.samjnaIdentity() in knownOperationStems
        } ?: return null
        val operationPada = instrumental.value as SubantaPada
        val domainEntry = padas.withIndex().take(instrumental.index)
            .lastOrNull { (index, pada) ->
                pada is SubantaPada && pada.vibhakti() == Vibhakti.SASTHI &&
                    (padas.getOrNull(index + 1) as? SubantaPada)?.vibhakti() != Vibhakti.DVITIYA
            }
        val domainPada = domainEntry?.value as? SubantaPada
        val boundaryIndex = domainEntry?.index ?: instrumental.index
        val karmaText = padas.take(boundaryIndex)
            .joinToString(" ") { normalizeIdentity(it.sourceText) }
            .trim()
        return SamjnaInvocationShape(
            operationStem = operationPada.pratipadika.samjnaIdentity(),
            domainStem = domainPada?.pratipadika?.samjnaDomainIdentity(),
            karmaText = karmaText,
            ukti = ukti,
        )
    }

    internal fun normalizeIdentity(value: String): String =
        value.split('+').joinToString(" + ") { it.trim() }.trim()

    private fun SubantaPada.vibhakti(): Vibhakti? = SupAffix.fromUpadesha(sup.text)?.vibhakti

}
