package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.AvyayaFunction
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.KridantaLexicalIdentity
import dev.panini.vyakaranam.ast.KridantaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.parser.PaniniParser

/** Recognizes an adhikāra declaration from its parsed nominal construction. */
object AdhikaraHeaderParser {
    private val parser = PaniniParser()

    fun domain(source: String): String? {
        val ukti = parser.parseOrNull(source.trim().trimEnd('।', '॥', ' ')) ?: return null
        val padas = ukti.vakyas.flatMap { it.padas }
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
        return SamjnaInvocationMatcher.normalizeIdentity(domain.sourceText)
    }

    private fun SubantaPada.isAdhikaraMarker(): Boolean = when (val base = pratipadika) {
        is MulaPratipadika -> base.text == "अधिकार"
        is KridantaPratipadika -> base.lexicalIdentity == KridantaLexicalIdentity.ADHIKARA
        else -> false
    }
}
