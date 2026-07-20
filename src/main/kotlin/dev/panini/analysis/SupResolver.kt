package dev.panini.analysis

class UnknownSupPratyayaException(
    val pratyaya: String,
) : IllegalArgumentException(
    "Unknown sup pratyaya: $pratyaya",
)

interface SupResolver {

    fun resolve(
        pratyaya: String,
    ): SupAnalysis
}
