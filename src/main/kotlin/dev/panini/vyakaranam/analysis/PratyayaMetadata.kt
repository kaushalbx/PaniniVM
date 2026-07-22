package dev.panini.vyakaranam.analysis

import dev.panini.core.Purusha
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.core.PadaType

data class SupMetadata(
    val vibhakti: Vibhakti,
    val vacana: Vacana,
)

data class TingMetadata(
    val purusha: Purusha,
    val vacana: Vacana,
    val pada: PadaType,
)

object PratyayaMetadata {

    private val supMetadata: Map<String, SupMetadata> = mapOf(
        "सुँ" to SupMetadata(Vibhakti.PRATHAMA, Vacana.EKAVACANA),
        "औ" to SupMetadata(Vibhakti.PRATHAMA, Vacana.DVIVACANA),
        "जस्" to SupMetadata(Vibhakti.PRATHAMA, Vacana.BAHUVACANA),

        "अम्" to SupMetadata(Vibhakti.DVITIYA, Vacana.EKAVACANA),
        "औट्" to SupMetadata(Vibhakti.DVITIYA, Vacana.DVIVACANA),
        "शस्" to SupMetadata(Vibhakti.DVITIYA, Vacana.BAHUVACANA),

        "टा" to SupMetadata(Vibhakti.TRTIYA, Vacana.EKAVACANA),
        "भ्याम्" to SupMetadata(Vibhakti.TRTIYA, Vacana.DVIVACANA),
        "भिस्" to SupMetadata(Vibhakti.TRTIYA, Vacana.BAHUVACANA),

        "ङे" to SupMetadata(Vibhakti.CHATURTHI, Vacana.EKAVACANA),

        /*
         * भ्याम् is shared by:
         * - तृतीया-द्विवचन
         * - चतुर्थी-द्विवचन
         * - पञ्चमी-द्विवचन
         *
         * भ्यस् is shared by:
         * - चतुर्थी-बहुवचन
         * - पञ्चमी-बहुवचन
         *
         * Therefore a raw surface map cannot uniquely determine all cases.
         */
        "ङसिँ" to SupMetadata(Vibhakti.PANCHAMI, Vacana.EKAVACANA),
        "ङस्" to SupMetadata(Vibhakti.SASTHI, Vacana.EKAVACANA),
        "ओस्" to SupMetadata(Vibhakti.SASTHI, Vacana.DVIVACANA),
        "आम्" to SupMetadata(Vibhakti.SASTHI, Vacana.BAHUVACANA),
        "ङि" to SupMetadata(Vibhakti.SAPTAMI, Vacana.EKAVACANA),
        "सुप्" to SupMetadata(Vibhakti.SAPTAMI, Vacana.BAHUVACANA),
    )

    private val tingMetadata: Map<String, TingMetadata> = mapOf(
        "तिप्" to TingMetadata(
            Purusha.PRATHAMA,
            Vacana.EKAVACANA,
            PadaType.PARASMAIPADA,
        ),
        "तस्" to TingMetadata(
            Purusha.PRATHAMA,
            Vacana.DVIVACANA,
            PadaType.PARASMAIPADA,
        ),
        "झि" to TingMetadata(
            Purusha.PRATHAMA,
            Vacana.BAHUVACANA,
            PadaType.PARASMAIPADA,
        ),
        "सिप्" to TingMetadata(
            Purusha.MADHYAMA,
            Vacana.EKAVACANA,
            PadaType.PARASMAIPADA,
        ),
        "थस्" to TingMetadata(
            Purusha.MADHYAMA,
            Vacana.DVIVACANA,
            PadaType.PARASMAIPADA,
        ),
        "थ" to TingMetadata(
            Purusha.MADHYAMA,
            Vacana.BAHUVACANA,
            PadaType.PARASMAIPADA,
        ),
        "मिप्" to TingMetadata(
            Purusha.UTTAMA,
            Vacana.EKAVACANA,
            PadaType.PARASMAIPADA,
        ),
        "वस्" to TingMetadata(
            Purusha.UTTAMA,
            Vacana.DVIVACANA,
            PadaType.PARASMAIPADA,
        ),
        "मस्" to TingMetadata(
            Purusha.UTTAMA,
            Vacana.BAHUVACANA,
            PadaType.PARASMAIPADA,
        ),

        "त" to TingMetadata(
            Purusha.PRATHAMA,
            Vacana.EKAVACANA,
            PadaType.ATMANEPADA,
        ),
        "आताम्" to TingMetadata(
            Purusha.PRATHAMA,
            Vacana.DVIVACANA,
            PadaType.ATMANEPADA,
        ),
        "झ" to TingMetadata(
            Purusha.PRATHAMA,
            Vacana.BAHUVACANA,
            PadaType.ATMANEPADA,
        ),
        "थास्" to TingMetadata(
            Purusha.MADHYAMA,
            Vacana.EKAVACANA,
            PadaType.ATMANEPADA,
        ),
        "आथाम्" to TingMetadata(
            Purusha.MADHYAMA,
            Vacana.DVIVACANA,
            PadaType.ATMANEPADA,
        ),
        "ध्वम्" to TingMetadata(
            Purusha.MADHYAMA,
            Vacana.BAHUVACANA,
            PadaType.ATMANEPADA,
        ),
        "इट्" to TingMetadata(
            Purusha.UTTAMA,
            Vacana.EKAVACANA,
            PadaType.ATMANEPADA,
        ),
        "वहि" to TingMetadata(
            Purusha.UTTAMA,
            Vacana.DVIVACANA,
            PadaType.ATMANEPADA,
        ),
        "महिङ्" to TingMetadata(
            Purusha.UTTAMA,
            Vacana.BAHUVACANA,
            PadaType.ATMANEPADA,
        ),
    )

    fun sup(text: String): SupMetadata? =
        supMetadata[text]

    fun requireSup(text: String): SupMetadata =
        sup(text)
            ?: throw IllegalArgumentException(
                "सुप्प्रत्ययस्य विवरणं न प्राप्तम्: $text",
            )

    fun ting(text: String): TingMetadata? =
        tingMetadata[text]

    fun requireTing(text: String): TingMetadata =
        ting(text)
            ?: throw IllegalArgumentException(
                "तिङ्प्रत्ययस्य विवरणं न प्राप्तम्: $text",
            )
}
