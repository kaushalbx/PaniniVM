package dev.panini.analysis

import dev.panini.derivation.Purusha
import dev.panini.derivation.Vacana

class DefaultTingResolver : TingResolver {

    private val analyses: Map<String, TingAnalysis> =
        buildMap {
            parasmaipada(
                pratyayas = listOf(
                    "तिप्", "तस्", "झि",
                    "सिप्", "थस्", "थ",
                    "मिप्", "वस्", "मस्",
                ),
            )

            atmanepada(
                pratyayas = listOf(
                    "त", "आताम्", "झ",
                    "थास्", "आथाम्", "ध्वम्",
                    "इट्", "वहि", "महिङ्",
                ),
            )
        }

    override fun resolve(
        pratyaya: String,
    ): TingAnalysis {
        val normalized = pratyaya.trim()

        return analyses[normalized]
            ?: throw UnknownTingPratyayaException(normalized)
    }

    private fun MutableMap<String, TingAnalysis>.parasmaipada(
        pratyayas: List<String>,
    ) {
        addPada(
            pratyayas = pratyayas,
            pada = PadaType.PARASMAIPADA,
        )
    }

    private fun MutableMap<String, TingAnalysis>.atmanepada(
        pratyayas: List<String>,
    ) {
        addPada(
            pratyayas = pratyayas,
            pada = PadaType.ATMANEPADA,
        )
    }

    private fun MutableMap<String, TingAnalysis>.addPada(
        pratyayas: List<String>,
        pada: PadaType,
    ) {
        require(pratyayas.size == 9) {
            "A tiṅ pada must contain exactly nine suffixes."
        }

        val positions = listOf(
            Purusha.PRATHAMA to Vacana.EKAVACANA,
            Purusha.PRATHAMA to Vacana.DVIVACANA,
            Purusha.PRATHAMA to Vacana.BAHUVACANA,

            Purusha.MADHYAMA to Vacana.EKAVACANA,
            Purusha.MADHYAMA to Vacana.DVIVACANA,
            Purusha.MADHYAMA to Vacana.BAHUVACANA,

            Purusha.UTTAMA to Vacana.EKAVACANA,
            Purusha.UTTAMA to Vacana.DVIVACANA,
            Purusha.UTTAMA to Vacana.BAHUVACANA,
        )

        pratyayas
            .zip(positions)
            .forEach { (pratyaya, position) ->
                val (purusha, vacana) = position

                put(
                    pratyaya,
                    TingAnalysis(
                        pratyaya = pratyaya,
                        purusha = purusha,
                        vacana = vacana,
                        pada = pada,
                    ),
                )
            }
    }
}
