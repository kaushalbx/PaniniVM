package dev.panini.sankhya

/** Renders frequency and distribution numerals from their grammatical source marker. */
class SankhyaAbhyasaRenderer(
    private val generator: SankhyaGenerator = SankhyaGenerator(),
) {
    fun numericStems(stems: List<String>): List<String> =
        stems.filterNot { markerFor(it) != null }

    fun render(marker: String, count: Long): String = when (markerFor(marker)) {
        Marker.KRTVASUC -> generator.frequency(count).final.surface
        Marker.SUC -> generator.frequency(count, useSuc = true).final.surface
        Marker.DHA -> generator.distribution(count).final.surface
        null -> generator.frequency(count).final.surface
    }

    private fun markerFor(surface: String): Marker? = when (surface) {
        "कृत्वः", "कृत्वा", "कृत्वसुच्" -> Marker.KRTVASUC
        "सुच्" -> Marker.SUC
        "धा" -> Marker.DHA
        else -> null
    }

    private enum class Marker { KRTVASUC, SUC, DHA }
}
