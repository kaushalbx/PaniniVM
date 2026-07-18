package dev.panini.derivation

import dev.panini.dhatupatha.PadaType
import dev.panini.shiksha.Linga

/** The requested taddhita/kṛt meaning; distinct from grammatical facts established by rules. */
enum class DerivationalMeaning {
    APATYA, GOTRA, ANANTARA_APATYA, TADRAJA, SAMUHA, VISHAYA_DESE, NIVASA,
    TATRA_BHAVA, JATA, VYAKHYANA, TATAH_AGATA, ABHIJANA, ADHYAYANA_VEDANA,
    KARTR_VEDANA, APADANA, BHAVA, BHAVISYAT, CARATI, JIVATI,
}

/** Non-semantic derivational circumstances that govern rule applicability. */
enum class DerivationalEnvironment {
    ARDHADHATUKA, PRAGDIVYATIYA, UDICYA, ASATTVA, KRIYAYOGA, SVANGA,
    KALAVRTTI, CHATURARTHIKA, UNADI_LICENSED, DHATU_LOPA,
}

enum class Prayoga { KARTARI, KARMANI, BHAVE }
enum class Purusha { PRATHAMA, MADHYAMA, UTTAMA }
/** The ten lakāras, retaining their grammatical upadeśa forms. */
enum class Lakara(val upadesha: String) {
    LAT("लट्"),
    LIT("लिट्"),
    LUT("लुट्"),
    LRT("लृट्"),
    LET("लेट्"),
    LOT("लोट्"),
    LANG("लङ्"),
    LING("लिङ्"),
    LUNG("लुङ्"),
    LRNG("लृङ्"),
}
enum class Kala { VARTAMANA, BHUTA, BHAVISYAT }
enum class PhonologicalRequest { GUNA, VRDDHI }
enum class LetAugment { AT, AAT }
enum class LetFormation { PRESENT_STEM, SIP_AORIST }
enum class LetEOption { E, AI }

/** Typed morphosyntactic axes replace independent boolean-like feature flags. */
data class Rupa(
    val linga: Linga? = null,
    val vibhakti: Vibhakti? = null,
    val vacana: Vacana? = null,
    val purusha: Purusha? = null,
    val prayoga: Prayoga? = null,
    val lakara: Lakara? = null,
    val pada: PadaType? = null,
)

data class DerivationalContext(
    /** User-requested derivational meaning; rules establish grammar separately in samjnas. */
    val requestedMeaning: DerivationalMeaning? = null,
    /** Meanings established during the derivation by an executable sūtra. */
    val derivedMeanings: Set<DerivationalMeaning> = emptySet(),
    val environments: Set<DerivationalEnvironment> = emptySet(),
    val kala: Kala? = null,
    val phonologicalRequest: PhonologicalRequest? = null,
    val letAugment: LetAugment = LetAugment.AAT,
    val letFormation: LetFormation = LetFormation.PRESENT_STEM,
    val letEOption: LetEOption = LetEOption.E,
    val rupa: Rupa = Rupa(),
) {
    fun has(environment: DerivationalEnvironment): Boolean = environment in environments
}
