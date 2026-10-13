package dev.panini.execution

import dev.panini.core.*
import dev.panini.execution.binding.PhalaReference
import dev.panini.execution.binding.CanonicalDhatuIdentity
import dev.panini.execution.binding.canonicalDhatuIdentity
import dev.panini.vyakaranam.ast.*

/** A procedure-owned nominative result predication, not a lexical return command. */
object ProcedureResultStatement {
    data class Relation(val role: SubantaPada, val value: Pada)

    fun resolve(node: Invocation, roleShadowed: Boolean = false): Relation? {
        if (roleShadowed) return null
        val sentence = node.vakya as? AkhyataVakya ?: return null
        val verb = sentence.tinganta
        if (verb.canonicalDhatuIdentity() != CanonicalDhatuIdentity.AS || verb.lakara != Lakara.LAT ||
            verb.upasargas.isNotEmpty() || verb.dhatu.sanadiPratyayas.isNotEmpty()) return null
        val roles = sentence.padas.filterIsInstance<SubantaPada>().filter {
            PhalaReference.isReference(it) && it.sup.supports(Vibhakti.PRATHAMA, Vacana.EKAVACANA)
        }
        if (roles.isEmpty()) return null
        val values = sentence.padas.filter { it !== verb && roles.none { role -> role === it } }
        // Instrumental/ablative comparisons and modified history references are other constructions.
        if (values.any { it !is SupBearingPada || !it.sup.supports(Vibhakti.PRATHAMA) }) return null
        require(roles.size == 1 && values.size == 1) { "A procedure-result statement requires one result role and one value." }
        require(verb.ting.affix == TingAffix.TIP) { "The singular result role requires a third-person singular copula." }
        return Relation(roles.single(), values.single())
    }
}
