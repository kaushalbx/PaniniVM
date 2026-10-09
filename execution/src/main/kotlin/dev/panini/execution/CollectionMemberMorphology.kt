package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.execution.binding.CanonicalDhatuIdentity
import dev.panini.execution.binding.canonicalDhatuIdentity
import dev.panini.vyakaranam.ast.*

/** Validates the supported substantival final-member object before binding loses morphology. */
object CollectionMemberMorphology {
    /** Preserve member-role morphology before procedure parameters rewrite nouns. */
    fun protectedPadas(padas: List<Pada>): List<SubantaPada> {
        val verb = padas.filterIsInstance<TingantaPada>().singleOrNull() ?: return emptyList()
        val projected = PriorActionLowering.expand(Invocation(AkhyataVakya(
            padas.joinToString(" ") { it.sourceText }, padas, verb)))
        if (projected != null) return projected.statements.filterIsInstance<Invocation>()
            .flatMap { protectedPadas(it.vakya.padas) }
        val nominals = padas.filterIsInstance<SubantaPada>()
        if (nominals.none { it.sup.text == SupAffix.NGAS.upadesha }) return emptyList()
        val identity = when (verb.canonicalDhatuIdentity()) {
            CanonicalDhatuIdentity.HR -> if ("उद्" in verb.upasargas) MulaPratipadikaIdentity.ANTIMA else null
            CanonicalDhatuIdentity.YUJ -> MulaPratipadikaIdentity.SANKHYA
            else -> null
        } ?: return emptyList()
        return nominals.filter {
            (it.pratipadika as? MulaPratipadika)?.lexicalIdentity == identity &&
                it.sup.text != SupAffix.NGAS.upadesha &&
                (identity != MulaPratipadikaIdentity.SANKHYA || it.sup.text == SupAffix.SAS.upadesha)
        }
    }

    fun validate(node: Invocation) {
        val sentence = node.vakya as? AkhyataVakya ?: return
        if (sentence.tinganta.canonicalDhatuIdentity() != CanonicalDhatuIdentity.HR ||
            "उद्" !in sentence.tinganta.upasargas) return
        val nominals = sentence.padas.filterIsInstance<SubantaPada>()
        if (nominals.none { it.sup.text == SupAffix.NGAS.upadesha }) return
        val selectors = nominals.filter {
            (it.pratipadika as? MulaPratipadika)?.lexicalIdentity == MulaPratipadikaIdentity.ANTIMA
        }
        if (selectors.isEmpty()) return
        require(selectors.size == 1) { "Final-member extraction requires one unambiguous selector." }
        val selector = selectors.single()
        require(selector.sup.text == SupAffix.AM.upadesha) {
            "Final-member extraction requires a singular accusative object."
        }
        require((selector.pratipadika as MulaPratipadika).vikaras.isEmpty()) {
            "Derived final-member nouns require their own semantics, not plain अन्तिम selection."
        }
    }
}
