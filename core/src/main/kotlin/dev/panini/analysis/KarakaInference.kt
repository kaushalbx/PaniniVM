package dev.panini.analysis

import dev.panini.core.Karaka
import dev.panini.core.Prayoga
import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti

/** Shared grammatical defaults for mapping case endings to semantic roles. */
object KarakaInference {
    /** All semantic roles licensed by an annotated ending before verbal context resolves syncretism. */
    fun candidates(upadesha: String, prayoga: Prayoga, sakarmaka: Boolean = true): Set<Karaka> =
        SupAffix.candidates(upadesha)
            .mapNotNull { infer(it.vibhakti, prayoga, sakarmaka) }
            .toSet()

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

            Prayoga.BHAVE,
            Prayoga.ANIRDHARITA,
                -> null
        }
}
