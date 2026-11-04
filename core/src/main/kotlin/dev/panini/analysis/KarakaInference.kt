package dev.panini.analysis

import dev.panini.core.Karaka
import dev.panini.core.Prayoga
import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti

/** Shared grammatical defaults for mapping case endings to semantic roles. */
object KarakaInference {
    /** All semantic roles licensed by an annotated ending before verbal context resolves syncretism. */
    fun candidates(upadesha: String, prayoga: Prayoga, sakarmaka: Boolean = true): Set<Karaka> =
        candidates(SupAffix.candidates(upadesha), prayoga, sakarmaka)

    /** Typed candidates retain syncretism until clause context establishes a role. */
    fun candidates(affixes: Collection<SupAffix>, prayoga: Prayoga, sakarmaka: Boolean = true): Set<Karaka> =
        candidatesForCases(affixes.mapTo(mutableSetOf()) { it.vibhakti }, prayoga, sakarmaka)

    fun candidatesForCases(cases: Set<Vibhakti>, prayoga: Prayoga, sakarmaka: Boolean = true): Set<Karaka> =
        cases.flatMapTo(mutableSetOf()) { case ->
            if (case == Vibhakti.TRTIYA && prayoga in setOf(Prayoga.KARMANI, Prayoga.BHAVE, Prayoga.CAUSATIVE))
                setOf(Karaka.KARTR, Karaka.KARANA)
            else setOfNotNull(infer(case, prayoga, sakarmaka))
        }

    /** Compatibility default; use candidatesForCases for ambiguity-preserving analysis. */
    fun infer(vibhakti: Vibhakti, prayoga: Prayoga, sakarmaka: Boolean = true): Karaka? =
        when (prayoga) {
            Prayoga.KARTARI -> when (vibhakti) {
                Vibhakti.PRATHAMA -> Karaka.KARTR
                Vibhakti.DVITIYA -> Karaka.KARMAN.takeIf { sakarmaka }
                Vibhakti.TRTIYA -> Karaka.KARANA
                Vibhakti.CHATURTHI -> Karaka.SAMPRADANA
                Vibhakti.PANCHAMI -> Karaka.APADANA
                Vibhakti.SASTHI -> Karaka.SAMBANDHA
                Vibhakti.SAPTAMI -> Karaka.ADHIKARANA
            }

            Prayoga.KARMANI -> when (vibhakti) {
                Vibhakti.PRATHAMA -> Karaka.KARMAN
                Vibhakti.TRTIYA -> Karaka.KARTR
                Vibhakti.CHATURTHI -> Karaka.SAMPRADANA
                Vibhakti.PANCHAMI -> Karaka.APADANA
                Vibhakti.SASTHI -> Karaka.SAMBANDHA
                Vibhakti.SAPTAMI -> Karaka.ADHIKARANA
                Vibhakti.DVITIYA -> null
            }

            Prayoga.CAUSATIVE -> when (vibhakti) {
                Vibhakti.PRATHAMA -> Karaka.KARTR
                Vibhakti.DVITIYA -> Karaka.KARMAN.takeIf { sakarmaka }
                Vibhakti.TRTIYA -> Karaka.KARTR
                Vibhakti.CHATURTHI -> Karaka.SAMPRADANA
                Vibhakti.PANCHAMI -> Karaka.APADANA
                Vibhakti.SASTHI -> Karaka.SAMBANDHA
                Vibhakti.SAPTAMI -> Karaka.ADHIKARANA
            }

            Prayoga.BHAVE -> when (vibhakti) {
                Vibhakti.TRTIYA -> Karaka.KARTR
                Vibhakti.CHATURTHI -> Karaka.SAMPRADANA
                Vibhakti.PANCHAMI -> Karaka.APADANA
                Vibhakti.SASTHI -> Karaka.SAMBANDHA
                Vibhakti.SAPTAMI -> Karaka.ADHIKARANA
                else -> null
            }

            Prayoga.ANIRDHARITA -> null
        }
}
