package dev.panini.execution

import dev.panini.core.Vibhakti
import dev.panini.execution.binding.CanonicalDhatuIdentity
import dev.panini.execution.binding.canonicalDhatuIdentity
import dev.panini.vyakaranam.ast.*

/** Checks written predicate agreement without inferring runtime payload cardinality. */
object CopularAgreement {
    fun validate(node: Invocation) {
        val sentence = node.vakya as? AkhyataVakya ?: return
        if (sentence.tinganta.canonicalDhatuIdentity() != CanonicalDhatuIdentity.AS) return
        val verb = sentence.tinganta.ting.affix ?: return
        val predicates = sentence.padas.filterIsInstance<SubantaPada>().filter { pada ->
            (pada.pratipadika as? MulaPratipadika)?.lexicalIdentity in setOf(
                MulaPratipadikaIdentity.SAMA, MulaPratipadikaIdentity.NYUNA, MulaPratipadikaIdentity.ADHIKA) &&
                pada.sup.supports(Vibhakti.PRATHAMA)
        }
        if (predicates.isEmpty()) return
        val subjects = sentence.padas.filterIsInstance<SubantaPada>().filter { pada ->
            pada !in predicates && pada.sup.supports(Vibhakti.PRATHAMA)
        }
        if (subjects.size == 1) {
            require(subjects.single().sup.supports(Vibhakti.PRATHAMA, verb.vacana)) {
                "Copular subject and finite verb must agree in grammatical number."
            }
            require(predicates.all {
                dev.panini.execution.binding.NominalGenderAgreement.compatible(it, subjects.single())
            }) { "Copular predicate and subject have incompatible established gender evidence." }
        }
        for (pada in predicates) {
            val nominal = pada.pratipadika as? MulaPratipadika ?: continue
            if (nominal.lexicalIdentity !in setOf(MulaPratipadikaIdentity.SAMA,
                    MulaPratipadikaIdentity.NYUNA, MulaPratipadikaIdentity.ADHIKA)) continue
            require(pada.sup.supports(Vibhakti.PRATHAMA, verb.vacana)) {
                "Copular predicate and finite verb must agree in grammatical number."
            }
        }
    }
}
