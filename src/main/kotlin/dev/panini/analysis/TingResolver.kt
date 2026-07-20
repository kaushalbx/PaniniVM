package dev.panini.analysis

class UnknownTingPratyayaException(
    val pratyaya: String,
) : IllegalArgumentException(
    "Unknown tiṅ pratyaya: $pratyaya",
)

interface TingResolver {

    fun resolve(
        pratyaya: String,
    ): TingAnalysis
}
