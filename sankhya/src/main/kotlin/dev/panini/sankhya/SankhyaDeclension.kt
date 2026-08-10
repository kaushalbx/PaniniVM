package dev.panini.sankhya

import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.derivation.SubantaDerivationRequest
import dev.panini.derivation.SubantaEngine

/** Declines cardinal numerals while preserving their numeric identity. */
object SankhyaDeclension {
    fun decline(
        value: Long,
        vibhakti: Vibhakti,
        vacana: Vacana,
        fallbackStem: String,
    ): String {
        if (vacana != naturalVacana(value)) return fallbackStem
        return when (value) {
            2L -> when (vibhakti) {
                Vibhakti.PRATHAMA, Vibhakti.DVITIYA -> "द्वे"
                Vibhakti.TRTIYA, Vibhakti.CHATURTHI, Vibhakti.PANCHAMI -> "द्वाभ्याम्"
                Vibhakti.SASTHI, Vibhakti.SAPTAMI -> "द्वयोः"
            }
            3L -> plural("त्रीणि", "त्रिभिः", "त्रिभ्यः", "त्रयाणाम्", "त्रिषु", vibhakti)
            4L -> plural("चत्वारि", "चतुर्भिः", "चतुर्भ्यः", "चतुर्णाम्", "चतुर्षु", vibhakti)
            5L -> plural("पञ्च", "पञ्चभिः", "पञ्चभ्यः", "पञ्चानाम्", "पञ्चसु", vibhakti)
            6L -> plural("षट्", "षड्भिः", "षड्भ्यः", "षण्णाम्", "षट्सु", vibhakti)
            7L -> plural("सप्त", "सप्तभिः", "सप्तभ्यः", "सप्तानाम्", "सप्तसु", vibhakti)
            8L -> plural("अष्ट", "अष्टाभिः", "अष्टाभ्यः", "अष्टानाम्", "अष्टासु", vibhakti)
            9L -> plural("नव", "नवभिः", "नवभ्यः", "नवानाम्", "नवसु", vibhakti)
            10L -> plural("दश", "दशभिः", "दशभ्यः", "दशानाम्", "दशसु", vibhakti)
            else -> runCatching {
                SubantaEngine().derive(
                    SubantaDerivationRequest(fallbackStem, vibhakti, vacana),
                ).final.surface
            }.getOrDefault(fallbackStem)
        }
    }

    fun naturalVacana(value: Long): Vacana = when (value) {
        1L -> Vacana.EKAVACANA
        2L -> Vacana.DVIVACANA
        else -> Vacana.BAHUVACANA
    }

    private fun plural(
        nominativeAccusative: String,
        instrumental: String,
        dativeAblative: String,
        genitive: String,
        locative: String,
        vibhakti: Vibhakti,
    ): String = when (vibhakti) {
        Vibhakti.PRATHAMA, Vibhakti.DVITIYA -> nominativeAccusative
        Vibhakti.TRTIYA -> instrumental
        Vibhakti.CHATURTHI, Vibhakti.PANCHAMI -> dativeAblative
        Vibhakti.SASTHI -> genitive
        Vibhakti.SAPTAMI -> locative
    }
}
