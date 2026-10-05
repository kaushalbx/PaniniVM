package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.KridantaPratipadika
import dev.panini.vyakaranam.ast.KrtPratyayaIdentity
import dev.panini.vyakaranam.ast.Pratipadika
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.morphologicalKey
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
        return parseSubantas(subantas)
    }

    /** Extracts a header identity without serializing and reparsing an existing AST. */
    fun parse(padas: List<Pada>): PrakriyaHeaderIdentity? =
        parseSubantas(padas.filterIsInstance<SubantaPada>())

    private fun parseSubantas(subantas: List<SubantaPada>): PrakriyaHeaderIdentity? {
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

internal fun Pratipadika.prakriyaIdentity(): String = morphologicalKey()

internal fun Pratipadika.prakriyaDomainIdentity(): String = morphologicalKey(includeTaddhita = false)
