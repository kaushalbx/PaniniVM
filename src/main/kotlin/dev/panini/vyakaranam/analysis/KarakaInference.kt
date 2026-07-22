package dev.panini.vyakaranam.analysis

import dev.panini.core.Karaka
import dev.panini.core.Prayoga
import dev.panini.core.Vibhakti

/** Shared grammatical defaults for mapping case endings to semantic roles. */
object KarakaInference {
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
                else -> null
            }

            Prayoga.BHAVE,
            Prayoga.ANIRDHARITA,
                -> null
        }
}
