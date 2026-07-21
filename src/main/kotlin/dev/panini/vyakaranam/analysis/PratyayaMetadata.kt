package dev.panini.vyakaranam.analysis

import dev.panini.vyakaranam.ast.*

data class SupMetadata(
    val vibhakti: Vibhakti,
    val vacana: Vacana,
)

data class TingMetadata(
    val purusha: Purusha,
    val vacana: Vacana,
    val pada: PadaPrakara,
)

object PratyayaMetadata {

    private val supMetadata: Map<String, SupMetadata> = mapOf(
        "सुँ" to SupMetadata(Vibhakti.PRATHAMA, Vacana.EKAVACANA),
        "औ" to SupMetadata(Vibhakti.PRATHAMA, Vacana.DVIVACANA),
        "जस्" to SupMetadata(Vibhakti.PRATHAMA, Vacana.BAHUVACANA),

        "अम्" to SupMetadata(Vibhakti.DVITIYA, Vacana.EKAVACANA),
        "औट्" to SupMetadata(Vibhakti.DVITIYA, Vacana.DVIVACANA),
        "शस्" to SupMetadata(Vibhakti.DVITIYA, Vacana.BAHUVACANA),

        "टा" to SupMetadata(Vibhakti.TRITIYA, Vacana.EKAVACANA),
        "भ्याम्" to SupMetadata(Vibhakti.TRITIYA, Vacana.DVIVACANA),
        "भिस्" to SupMetadata(Vibhakti.TRITIYA, Vacana.BAHUVACANA),

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
        "ङस्" to SupMetadata(Vibhakti.SHASTHI, Vacana.EKAVACANA),
        "ओस्" to SupMetadata(Vibhakti.SHASTHI, Vacana.DVIVACANA),
        "आम्" to SupMetadata(Vibhakti.SHASTHI, Vacana.BAHUVACANA),
        "ङि" to SupMetadata(Vibhakti.SAPTAMI, Vacana.EKAVACANA),
        "सुप्" to SupMetadata(Vibhakti.SAPTAMI, Vacana.BAHUVACANA),
    )

    private val tingMetadata: Map<String, TingMetadata> = mapOf(
        "तिप्" to TingMetadata(
            Purusha.PRATHAMA,
            Vacana.EKAVACANA,
            PadaPrakara.PARASMAIPADA,
        ),
        "तस्" to TingMetadata(
            Purusha.PRATHAMA,
            Vacana.DVIVACANA,
            PadaPrakara.PARASMAIPADA,
        ),
        "झि" to TingMetadata(
            Purusha.PRATHAMA,
            Vacana.BAHUVACANA,
            PadaPrakara.PARASMAIPADA,
        ),
        "सिप्" to TingMetadata(
            Purusha.MADHYAMA,
            Vacana.EKAVACANA,
            PadaPrakara.PARASMAIPADA,
        ),
        "थस्" to TingMetadata(
            Purusha.MADHYAMA,
            Vacana.DVIVACANA,
            PadaPrakara.PARASMAIPADA,
        ),
        "थ" to TingMetadata(
            Purusha.MADHYAMA,
            Vacana.BAHUVACANA,
            PadaPrakara.PARASMAIPADA,
        ),
        "मिप्" to TingMetadata(
            Purusha.UTTAMA,
            Vacana.EKAVACANA,
            PadaPrakara.PARASMAIPADA,
        ),
        "वस्" to TingMetadata(
            Purusha.UTTAMA,
            Vacana.DVIVACANA,
            PadaPrakara.PARASMAIPADA,
        ),
        "मस्" to TingMetadata(
            Purusha.UTTAMA,
            Vacana.BAHUVACANA,
            PadaPrakara.PARASMAIPADA,
        ),

        "त" to TingMetadata(
            Purusha.PRATHAMA,
            Vacana.EKAVACANA,
            PadaPrakara.ATMANEPADA,
        ),
        "आताम्" to TingMetadata(
            Purusha.PRATHAMA,
            Vacana.DVIVACANA,
            PadaPrakara.ATMANEPADA,
        ),
        "झ" to TingMetadata(
            Purusha.PRATHAMA,
            Vacana.BAHUVACANA,
            PadaPrakara.ATMANEPADA,
        ),
        "थास्" to TingMetadata(
            Purusha.MADHYAMA,
            Vacana.EKAVACANA,
            PadaPrakara.ATMANEPADA,
        ),
        "आथाम्" to TingMetadata(
            Purusha.MADHYAMA,
            Vacana.DVIVACANA,
            PadaPrakara.ATMANEPADA,
        ),
        "ध्वम्" to TingMetadata(
            Purusha.MADHYAMA,
            Vacana.BAHUVACANA,
            PadaPrakara.ATMANEPADA,
        ),
        "इट्" to TingMetadata(
            Purusha.UTTAMA,
            Vacana.EKAVACANA,
            PadaPrakara.ATMANEPADA,
        ),
        "वहि" to TingMetadata(
            Purusha.UTTAMA,
            Vacana.DVIVACANA,
            PadaPrakara.ATMANEPADA,
        ),
        "महिङ्" to TingMetadata(
            Purusha.UTTAMA,
            Vacana.BAHUVACANA,
            PadaPrakara.ATMANEPADA,
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
