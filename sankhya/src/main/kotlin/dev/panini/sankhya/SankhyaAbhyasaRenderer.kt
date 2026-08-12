package dev.panini.sankhya

object SankhyaAbhyasaMarkers {
    fun isMarker(surface: String): Boolean = markerFor(surface) != null

    fun isFrequency(surface: String): Boolean =
        markerFor(surface) == Marker.KRTVASUC || markerFor(surface) == Marker.SUC

    fun numericStems(stems: List<String>): List<String> = stems.filterNot(::isMarker)

    fun numericFrequencyStems(stems: List<String>): List<String> = stems.filterNot(::isFrequency)

    internal fun markerFor(surface: String): Marker? = when (surface) {
        "कृत्वः", "कृत्वस", "कृत्वा", "कृत्वसुच्" -> Marker.KRTVASUC
        "सुच्" -> Marker.SUC
        "धा" -> Marker.DHA
        else -> null
    }

    internal enum class Marker { KRTVASUC, SUC, DHA }
}

/** Renders frequency and distribution numerals from their grammatical source marker. */
class SankhyaAbhyasaRenderer(
    private val generator: SankhyaGenerator = SankhyaGenerator(),
) {
    fun numericStems(stems: List<String>): List<String> =
        SankhyaAbhyasaMarkers.numericStems(stems)

    fun render(marker: String, count: Long): String = when (SankhyaAbhyasaMarkers.markerFor(marker)) {
        SankhyaAbhyasaMarkers.Marker.KRTVASUC -> generator.frequency(count).final.surface
        SankhyaAbhyasaMarkers.Marker.SUC -> generator.frequency(count, useSuc = true).final.surface
        SankhyaAbhyasaMarkers.Marker.DHA -> generator.distribution(count).final.surface
        null -> generator.frequency(count).final.surface
    }
}
