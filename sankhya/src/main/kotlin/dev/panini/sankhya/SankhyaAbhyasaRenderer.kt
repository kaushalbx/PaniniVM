package dev.panini.sankhya

/** Renders frequency and distribution numerals from their grammatical source marker. */
class SankhyaAbhyasaRenderer {
    fun numericStems(stems: List<String>): List<String> =
        stems.filterNot { markerFor(it) != null }

    fun render(marker: String, count: Long, cardinalSurface: String): String = when (markerFor(marker)) {
        Marker.KRTVASUC -> "${cardinalSurface}कृत्वः"
        Marker.SUC -> specialSucSurface(count) ?: "${cardinalSurface}कृत्वः"
        Marker.DHA -> "${cardinalSurface}धा"
        null -> "${cardinalSurface}कृत्वः"
    }

    private fun markerFor(surface: String): Marker? = when (surface) {
        "कृत्वः", "कृत्वा", "कृत्वसुच्" -> Marker.KRTVASUC
        "सुच्" -> Marker.SUC
        "धा" -> Marker.DHA
        else -> null
    }

    private fun specialSucSurface(count: Long): String? = when (count) {
        2L -> "द्विः"
        3L -> "त्रिः"
        4L -> "चतुः"
        else -> null
    }

    private enum class Marker { KRTVASUC, SUC, DHA }
}
