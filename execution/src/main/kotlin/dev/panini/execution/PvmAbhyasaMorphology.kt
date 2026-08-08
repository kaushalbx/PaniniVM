package dev.panini.execution

/** Compatibility morphology for readable अभ्यास and distribution surfaces. */
internal object PvmAbhyasaMorphology {
    private val NON_NUMERIC_STEMS = setOf("कृत्वः", "कृत्वा", "कृत्वसुच्", "सुच्", "धा")
    private val KRTVAS_STEMS = setOf("कृत्वः", "कृत्वा", "कृत्वसुच्")
    private val SUC_SURFACES = mapOf(
        2L to "द्विः",
        3L to "त्रिः",
        4L to "चतुः",
    )

    fun numericStems(stems: List<String>): List<String> = stems.filterNot { it in NON_NUMERIC_STEMS }

    fun surface(lastStem: String, count: Long, cardinalSurface: String): String = when {
        lastStem in KRTVAS_STEMS -> "${cardinalSurface}कृत्वः"
        lastStem == "सुच्" -> SUC_SURFACES[count] ?: "${cardinalSurface}कृत्वः"
        lastStem == "धा" -> "${cardinalSurface}धा"
        else -> "${cardinalSurface}कृत्वः"
    }
}
