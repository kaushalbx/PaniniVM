package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.KridantaPratipadika
import dev.panini.vyakaranam.ast.KrtPratyayaIdentity
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.Pratipadika
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.TaddhitaVikara
import dev.panini.vyakaranam.parser.PaniniParser

data class PrakriyaHeaderIdentity(
    val operationStem: String,
    val domainStem: String?,
)

/** Extracts saṃjñā operation and domain identities from nominal case structure. */
object PrakriyaHeaderIdentityParser {
    private val parser = PaniniParser()

    fun parse(source: String): PrakriyaHeaderIdentity? {
        val subantas = parseSubantas(source) ?: return null
        val operation = subantas.lastOrNull()
            ?.takeIf { it.vibhakti() == Vibhakti.PRATHAMA }
            ?: return null
        val domain = subantas.dropLast(1).lastOrNull { it.vibhakti() == Vibhakti.SASTHI }
        return PrakriyaHeaderIdentity(
            operationStem = operation.pratipadika.prakriyaIdentity(),
            domainStem = domain?.pratipadika?.prakriyaDomainIdentity(),
        )
    }

    fun hasOperationKrtPratyayaIdentity(source: String, identity: KrtPratyayaIdentity): Boolean =
        (parseSubantas(source)?.lastOrNull()?.pratipadika as? KridantaPratipadika)
            ?.krtPratyayaIdentity == identity

    private fun parseSubantas(source: String): List<SubantaPada>? =
        parser.parseOrNull(source.trim().trimEnd('।', '॥', ' '))
            ?.grammaticalVakyas()
            ?.flatMap { it.padas }
            ?.filterIsInstance<SubantaPada>()

    private fun SubantaPada.vibhakti(): Vibhakti? =
        SupAffix.fromUpadesha(sup.text)?.vibhakti
}

internal fun Pratipadika.prakriyaIdentity(): String = PrakriyaInvocationMatcher.normalizeIdentity(
    when (this) {
        is MulaPratipadika -> text
        is KridantaPratipadika -> sourceText
        else -> sourceText
    },
)

internal fun Pratipadika.prakriyaDomainIdentity(): String {
    if (this is MulaPratipadika) return PrakriyaInvocationMatcher.normalizeIdentity(text)
    return taddhitaVikaras().asReversed().fold(prakriyaIdentity()) { identity, vikara ->
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
