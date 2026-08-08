package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.KridantaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.Pratipadika
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.TaddhitaVikara
import dev.panini.vyakaranam.parser.PaniniParser

data class SamjnaHeaderIdentity(
    val operationStem: String,
    val domainStem: String?,
)

/** Extracts saṃjñā operation and domain identities from nominal case structure. */
object SamjnaHeaderIdentityParser {
    private val parser = PaniniParser()

    fun parse(source: String): SamjnaHeaderIdentity? {
        val ukti = parser.parseOrNull(source.trim().trimEnd('।', '॥', ' ')) ?: return null
        val subantas = ukti.vakyas.flatMap { it.padas }.filterIsInstance<SubantaPada>()
        val operation = subantas.lastOrNull()
            ?.takeIf { it.vibhakti() == Vibhakti.PRATHAMA }
            ?: return null
        val domain = subantas.dropLast(1).lastOrNull { it.vibhakti() == Vibhakti.SASTHI }
        return SamjnaHeaderIdentity(
            operationStem = operation.pratipadika.samjnaIdentity(),
            domainStem = domain?.pratipadika?.samjnaDomainIdentity(),
        )
    }

    private fun SubantaPada.vibhakti(): Vibhakti? =
        SupAffix.fromUpadesha(sup.text)?.vibhakti
}

internal fun Pratipadika.samjnaIdentity(): String = SamjnaInvocationMatcher.normalizeIdentity(
    when (this) {
        is MulaPratipadika -> text
        is KridantaPratipadika -> sourceText
        else -> sourceText
    },
)

internal fun Pratipadika.samjnaDomainIdentity(): String {
    if (this is MulaPratipadika) return SamjnaInvocationMatcher.normalizeIdentity(text)
    return taddhitaVikaras().asReversed().fold(samjnaIdentity()) { identity, vikara ->
        identity.removeSuffix(" + ${vikara.pratyaya}")
    }
}

private fun Pratipadika.taddhitaVikaras(): List<TaddhitaVikara> = when (this) {
    is MulaPratipadika -> vikaras.filterIsInstance<TaddhitaVikara>()
    is KridantaPratipadika -> vikaras.filterIsInstance<TaddhitaVikara>()
    is dev.panini.vyakaranam.ast.UnadyantaPratipadika -> vikaras.filterIsInstance<TaddhitaVikara>()
    is dev.panini.vyakaranam.ast.SamasaPratipadika -> vikaras.filterIsInstance<TaddhitaVikara>()
    is dev.panini.vyakaranam.ast.SankhyaPratipadika -> vikaras.filterIsInstance<TaddhitaVikara>()
}
