package dev.panini.execution

import dev.panini.core.SupAffix
import dev.panini.execution.binding.CanonicalDhatuIdentity
import dev.panini.execution.binding.canonicalDhatuIdentity
import dev.panini.vyakaranam.ast.*

/** Preserves and validates substantival member selectors before binding loses morphology. */
object CollectionMemberMorphology {
    private fun supText(pada: Pada): String? = when (pada) {
        is SubantaPada -> pada.sup.text
        is SankhyaPuranaPada -> pada.sup.text
        is SankhyaPada -> pada.sup.text
        else -> null
    }

    fun ordinalExpression(pada: Pada): ExecutionExpression.Pada? {
        val ordinal = PuranaPratyayaResolver.ordinalValue(pada) ?: return null
        val sup = supText(pada) ?: return null
        require(sup == SupAffix.AM.upadesha && ordinal > 0) {
            "Ordinal-member extraction requires a positive singular accusative selector."
        }
        return ExecutionExpression.Pada(pada.sourceText,
            value = SanskritValue.Sankhya(ordinal, pada.sourceText),
            memberSelection = CollectionMemberSelection.ORDINAL)
    }

    /** Preserve member-role morphology before procedure parameters rewrite nouns. */
    fun protectedPadas(padas: List<Pada>): List<Pada> {
        val verb = padas.filterIsInstance<TingantaPada>().singleOrNull() ?: return emptyList()
        val projected = PriorActionLowering.expand(Invocation(AkhyataVakya(
            padas.joinToString(" ") { it.sourceText }, padas, verb)))
        if (projected != null) return projected.statements.filterIsInstance<Invocation>()
            .flatMap { protectedPadas(it.vakya.padas) }
        val nominals = padas.filterIsInstance<SubantaPada>()
        if (nominals.none { it.sup.text == SupAffix.NGAS.upadesha }) return emptyList()
        if (verb.canonicalDhatuIdentity() == CanonicalDhatuIdentity.HR && "उद्" in verb.upasargas) {
            return padas.filter { pada ->
                (supText(pada) != SupAffix.NGAS.upadesha && PuranaPratyayaResolver.ordinalValue(pada) != null) ||
                    ((pada as? SubantaPada)?.pratipadika as? MulaPratipadika)?.lexicalIdentity == MulaPratipadikaIdentity.ANTIMA
            }
        }
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
        val ordinals = sentence.padas.filter {
            supText(it) != SupAffix.NGAS.upadesha && PuranaPratyayaResolver.ordinalValue(it) != null
        }
        if (ordinals.isNotEmpty()) {
            require(ordinals.size == 1 && nominals.none {
                (it.pratipadika as? MulaPratipadika)?.lexicalIdentity == MulaPratipadikaIdentity.ANTIMA
            }) { "Collection extraction requires one unambiguous member selector." }
            ordinalExpression(ordinals.single())
        }
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
