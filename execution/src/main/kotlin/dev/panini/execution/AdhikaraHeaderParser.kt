package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.AvyayaFunction
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.KridantaLexicalIdentity
import dev.panini.vyakaranam.ast.KridantaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadikaIdentity
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.parser.PaniniParser

data class ParsedAdhikaraHeader(
    val domainSource: String,
    val domainIdentity: String,
)

/** Recognizes an adhikāra declaration from its parsed nominal construction. */
object AdhikaraHeaderParser {
    private val parser = PaniniParser()

    fun parse(source: String): ParsedAdhikaraHeader? {
        val domain = domainPada(source) ?: return null
        return ParsedAdhikaraHeader(
            // Compatibility projection for rendering and inheritance declarations.
            domainSource = PrakriyaInvocationMatcher.normalizeIdentity(domain.sourceText),
            domainIdentity = domain.pratipadika.prakriyaIdentity(),
        )
    }

    fun domain(source: String): String? = parse(source)?.domainSource

    /** Canonical case-free identity used by registries and module symbols. */
    fun domainIdentity(source: String): String? =
        parse(source)?.domainIdentity

    private fun domainPada(source: String): SubantaPada? {
        val ukti = parser.parseOrNull(source.trim().trimEnd('।', '॥', ' ')) ?: return null
        val padas = ukti.grammaticalVakyas().flatMap { it.padas }
        val markerIndex = padas.indexOfLast { pada ->
            pada is SubantaPada &&
                SupAffix.fromUpadesha(pada.sup.text)?.vibhakti == Vibhakti.PRATHAMA &&
                pada.isAdhikaraMarker()
        }
        if (markerIndex <= 0) return null
        if (padas.take(markerIndex).filterIsInstance<AvyayaPada>().none { it.function == AvyayaFunction.QUOTATIVE }) return null
        val domain = padas.take(markerIndex).filterIsInstance<SubantaPada>().lastOrNull()
            ?.takeIf { SupAffix.fromUpadesha(it.sup.text)?.vibhakti == Vibhakti.PRATHAMA }
            ?: return null
        return domain
    }

    private fun SubantaPada.isAdhikaraMarker(): Boolean = when (val base = pratipadika) {
        is MulaPratipadika -> base.lexicalIdentity == MulaPratipadikaIdentity.ADHIKARA
        is KridantaPratipadika -> base.lexicalIdentity == KridantaLexicalIdentity.ADHIKARA
        else -> false
    }
}
