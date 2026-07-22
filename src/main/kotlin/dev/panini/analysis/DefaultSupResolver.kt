package dev.panini.analysis

import dev.panini.core.Vacana
import dev.panini.core.Vibhakti

class DefaultSupResolver : SupResolver {

    private val positionsByPratyaya: Map<String, Set<SupPosition>> =
        mapOf(
            "सुँ" to positions(
                Vibhakti.PRATHAMA,
                Vacana.EKAVACANA,
            ),
            "औ" to positions(
                Vibhakti.PRATHAMA,
                Vacana.DVIVACANA,
            ),
            "जस्" to positions(
                Vibhakti.PRATHAMA,
                Vacana.BAHUVACANA,
            ),

            "अम्" to positions(
                Vibhakti.DVITIYA,
                Vacana.EKAVACANA,
            ),
            "औट्" to positions(
                Vibhakti.DVITIYA,
                Vacana.DVIVACANA,
            ),
            "शस्" to positions(
                Vibhakti.DVITIYA,
                Vacana.BAHUVACANA,
            ),

            "टा" to positions(
                Vibhakti.TRTIYA,
                Vacana.EKAVACANA,
            ),
            "भ्याम्" to setOf(
                SupPosition(
                    Vibhakti.TRTIYA,
                    Vacana.DVIVACANA,
                ),
                SupPosition(
                    Vibhakti.CHATURTHI,
                    Vacana.DVIVACANA,
                ),
                SupPosition(
                    Vibhakti.PANCHAMI,
                    Vacana.DVIVACANA,
                ),
            ),
            "भिस्" to positions(
                Vibhakti.TRTIYA,
                Vacana.BAHUVACANA,
            ),

            "ङे" to positions(
                Vibhakti.CHATURTHI,
                Vacana.EKAVACANA,
            ),
            "भ्यस्" to setOf(
                SupPosition(
                    Vibhakti.CHATURTHI,
                    Vacana.BAHUVACANA,
                ),
                SupPosition(
                    Vibhakti.PANCHAMI,
                    Vacana.BAHUVACANA,
                ),
            ),

            "ङसिँ" to positions(
                Vibhakti.PANCHAMI,
                Vacana.EKAVACANA,
            ),

            "ङस्" to positions(
                Vibhakti.SASTHI,
                Vacana.EKAVACANA,
            ),
            "ओस्" to setOf(
                SupPosition(
                    Vibhakti.SASTHI,
                    Vacana.DVIVACANA,
                ),
                SupPosition(
                    Vibhakti.SAPTAMI,
                    Vacana.DVIVACANA,
                ),
            ),
            "आम्" to positions(
                Vibhakti.SASTHI,
                Vacana.BAHUVACANA,
            ),

            "ङि" to positions(
                Vibhakti.SAPTAMI,
                Vacana.EKAVACANA,
            ),
            "सुप्" to positions(
                Vibhakti.SAPTAMI,
                Vacana.BAHUVACANA,
            ),
        )

    override fun resolve(
        pratyaya: String,
    ): SupAnalysis {
        val normalized = pratyaya.trim()

        val candidates = positionsByPratyaya[normalized]
            ?: throw UnknownSupPratyayaException(normalized)

        return SupAnalysis(
            pratyaya = normalized,
            candidates = candidates,
        )
    }

    private fun positions(
        vibhakti: Vibhakti,
        vacana: Vacana,
    ): Set<SupPosition> =
        setOf(
            SupPosition(
                vibhakti = vibhakti,
                vacana = vacana,
            ),
        )
}
