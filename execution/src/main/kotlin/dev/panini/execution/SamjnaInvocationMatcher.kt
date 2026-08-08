package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.KridantaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.Pratipadika
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
        val padas = ukti.vakyas.flatMap { it.padas }
        val verbIndex = padas.indexOfFirst { it is TingantaPada }
        if (verbIndex < 0) return null

        val instrumental = padas.withIndex().firstOrNull { (index, pada) ->
            index < verbIndex && pada is SubantaPada &&
                pada.vibhakti() == Vibhakti.TRTIYA &&
                pada.pratipadika.identity() in knownOperationStems
        } ?: return null
        val operationPada = instrumental.value as SubantaPada
        val domainEntry = padas.withIndex().take(instrumental.index)
            .lastOrNull { (_, pada) -> pada is SubantaPada && pada.vibhakti() == Vibhakti.SASTHI }
        val domainPada = domainEntry?.value as? SubantaPada
        val boundaryIndex = domainEntry?.index ?: instrumental.index
        val karmaText = padas.take(boundaryIndex)
            .joinToString(" ") { normalizeIdentity(it.sourceText) }
            .trim()
        return SamjnaInvocationShape(
            operationStem = operationPada.pratipadika.identity(),
            domainStem = domainPada?.pratipadika?.domainIdentity(),
            karmaText = karmaText,
            ukti = ukti,
        )
    }

    internal fun normalizeIdentity(value: String): String =
        value.split('+').joinToString(" + ") { it.trim() }.trim()

    private fun SubantaPada.vibhakti(): Vibhakti? = SupAffix.fromUpadesha(sup.text)?.vibhakti

    private fun Pratipadika.identity(): String = normalizeIdentity(
        when (this) {
            is KridantaPratipadika -> sourceText
            is MulaPratipadika -> text
            else -> sourceText
        },
    )

    private fun Pratipadika.domainIdentity(): String {
        val identity = when (this) {
            is MulaPratipadika -> text
            else -> sourceText
        }
        return normalizeIdentity(identity)
            .substringBefore("+ मतुप्")
            .substringBefore("+ वतुप्")
            .substringBefore("+ वत्")
            .trim()
    }
}
