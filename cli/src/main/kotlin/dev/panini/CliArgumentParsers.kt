package dev.panini

import dev.panini.core.Karaka
import dev.panini.core.Lakara
import dev.panini.core.Prayoga
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti

internal object CliArgumentParsers {
    fun vibhakti(value: String): Vibhakti = when (value.uppercase()) {
        "PRATHAMA", "प्रथमा" -> Vibhakti.PRATHAMA
        "DVITIYA", "द्वितीया" -> Vibhakti.DVITIYA
        "TRTIYA", "तृतीया" -> Vibhakti.TRTIYA
        "CHATURTHI", "चतुर्थी" -> Vibhakti.CHATURTHI
        "PANCHAMI", "पञ्चमी", "पंचमी" -> Vibhakti.PANCHAMI
        "SASTHI", "षष्ठी" -> Vibhakti.SASTHI
        "SAPTAMI", "सप्तमी" -> Vibhakti.SAPTAMI
        else -> error("Unknown vibhakti: $value")
    }

    fun vacana(value: String): Vacana = when (value.uppercase()) {
        "EKAVACANA", "एकवचन" -> Vacana.EKAVACANA
        "DVIVACANA", "द्विवचन" -> Vacana.DVIVACANA
        "BAHUVACANA", "बहुवचन" -> Vacana.BAHUVACANA
        else -> error("Unknown vacana: $value")
    }

    fun lakaraOrNull(value: String): Lakara? = Lakara.entries.firstOrNull {
        it.name == value.uppercase() || it.upadesha == value
    }

    fun karaka(value: String): Karaka = when (value.uppercase()) {
        "KARTR", "कर्ता" -> Karaka.KARTR
        "KARMAN", "कर्म" -> Karaka.KARMAN
        "KARANA", "करण" -> Karaka.KARANA
        "SAMPRADANA", "सम्प्रदान", "संप्रदान" -> Karaka.SAMPRADANA
        "APADANA", "अपादान" -> Karaka.APADANA
        "ADHIKARANA", "अधिकरण" -> Karaka.ADHIKARANA
        "SAMBANDHA", "सम्बन्ध", "संबंध" -> Karaka.SAMBANDHA
        "SAMBODHANA", "सम्बोधन", "संबोधन" -> Karaka.SAMBODHANA
        "ANIRDHARITA" -> Karaka.ANIRDHARITA
        else -> error("Unknown karaka: $value")
    }

    fun prayoga(value: String): Prayoga = when (value.uppercase()) {
        "KARTARI", "कर्तरि" -> Prayoga.KARTARI
        "KARMANI", "कर्मणि" -> Prayoga.KARMANI
        "BHAVE", "भावे" -> Prayoga.BHAVE
        "CAUSATIVE" -> Prayoga.CAUSATIVE
        "ANIRDHARITA" -> Prayoga.ANIRDHARITA
        else -> error("Unknown prayoga: $value")
    }
}
