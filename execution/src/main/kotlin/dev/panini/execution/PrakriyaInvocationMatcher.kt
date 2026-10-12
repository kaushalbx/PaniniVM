package dev.panini.execution

import dev.panini.core.Vibhakti
import dev.panini.core.Vacana
import dev.panini.execution.binding.CanonicalDhatuIdentity
import dev.panini.execution.binding.canonicalDhatuIdentity
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
        val head = padas.filterIsInstance<TingantaPada>().singleOrNull() ?: return null
        if (head.canonicalDhatuIdentity() != CanonicalDhatuIdentity.KRU ||
            head.upasargas.isNotEmpty() || head.dhatu.sanadiAffixes.isNotEmpty()) return null

        val instrumentals = padas.withIndex().filter { (_, pada) ->
            pada is SubantaPada && pada.sup.supports(Vibhakti.TRTIYA) &&
                pada.pratipadika.prakriyaIdentity() in knownOperationStems
        }
        if (instrumentals.isEmpty()) return null
        require(instrumentals.size == 1) { "Ambiguous procedure invocation: multiple known instrumental procedure names." }
        val instrumental = instrumentals.single()
        val operationPada = instrumental.value as SubantaPada
        require(operationPada.sup.supports(Vibhakti.TRTIYA, Vacana.EKAVACANA)) {
            "A procedure invocation requires one instrumental singular procedure; dual/plural invocation is not supported."
        }
        val resultModifiers = dev.panini.execution.binding.NamedActionResultReferenceResolver.resolve(padas)
            .map { it.modifier }.toSet()
        val domainEntries = padas.withIndex().filter { (index, pada) ->
                pada is SubantaPada && pada !in resultModifiers && pada.sup.supports(Vibhakti.SASTHI) &&
                    (padas.getOrNull(index + 1) as? SubantaPada)?.sup?.supports(Vibhakti.DVITIYA) != true
        }
        require(domainEntries.size <= 1) { "Ambiguous procedure domain: multiple unassigned genitive modifiers." }
        val domainEntry = domainEntries.singleOrNull()
        val domainPada = domainEntry?.value as? SubantaPada
        require(domainPada == null || domainPada.sup.supports(Vibhakti.SASTHI, Vacana.EKAVACANA)) {
            "A procedure invocation requires one genitive singular domain; dual/plural domains are not supported."
        }
        val operands = padas.filterIndexed { index, pada ->
            index != instrumental.index && index != domainEntry?.index && pada !is TingantaPada
        }
        val karmaText = operands
            .joinToString(" ") { normalizeIdentity(it.sourceText) }
            .trim()
        return PrakriyaInvocationShape(
            operationStem = operationPada.pratipadika.prakriyaIdentity(),
            domainStem = domainPada?.pratipadika?.prakriyaDomainIdentity(),
            karmaText = karmaText,
            argumentPadas = operands.flatMap(::argumentPadas),
            ukti = ukti,
        )
    }

    internal fun normalizeIdentity(value: String): String =
        value.split('+').joinToString(" + ") { it.trim() }.trim()

    private fun argumentPadas(pada: Pada): List<Pada> = when (pada) {
        is SamuccitaSubanta -> pada.members
        else -> listOf(pada)
    }

}
