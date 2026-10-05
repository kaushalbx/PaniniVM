package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.SamuccitaSubanta
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.TingantaPada
import dev.panini.vyakaranam.ast.Ukti

data class PrakriyaInvocationShape(
    val operationStem: String,
    val domainStem: String?,
    val karmaText: String,
    val argumentPadas: List<Pada>,
    val ukti: Ukti,
)

/** Extracts invocation identity and grammatical roles from the parsed utterance. */
object PrakriyaInvocationMatcher {
    /** Matches invocation identity directly from the canonical AST. */
    fun match(
        ukti: Ukti,
        knownOperationStems: Set<String>,
    ): PrakriyaInvocationShape? {
        val padas = ukti.grammaticalVakyas().flatMap { it.padas }
        val verbIndex = padas.indexOfFirst { it is TingantaPada }
        if (verbIndex < 0) return null

        val instrumental = padas.withIndex().firstOrNull { (index, pada) ->
            index < verbIndex && pada is SubantaPada &&
                pada.vibhakti() == Vibhakti.TRTIYA &&
                pada.pratipadika.prakriyaIdentity() in knownOperationStems
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
        return PrakriyaInvocationShape(
            operationStem = operationPada.pratipadika.prakriyaIdentity(),
            domainStem = domainPada?.pratipadika?.prakriyaDomainIdentity(),
            karmaText = karmaText,
            argumentPadas = padas.take(boundaryIndex).flatMap(::argumentPadas),
            ukti = ukti,
        )
    }

    internal fun normalizeIdentity(value: String): String =
        value.split('+').joinToString(" + ") { it.trim() }.trim()

    private fun SubantaPada.vibhakti(): Vibhakti? = SupAffix.fromUpadesha(sup.text)?.vibhakti

    private fun argumentPadas(pada: Pada): List<Pada> = when (pada) {
        is SamuccitaSubanta -> pada.members
        else -> listOf(pada)
    }

}
