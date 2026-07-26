package dev.panini.sutra

data class SutraCatalogIssue(
    val sutra: String,
    val message: String,
)

/** Validates catalog facts independently of rule execution. */
object SutraCatalogValidator {
    fun validate(registry: SutraRegistry): List<SutraCatalogIssue> = buildList {
        registry.sutras.forEach { sutra ->
            val expectedKrama = sutra.chapter * 100_000 + sutra.pada * 10_000 + sutraNumber(sutra.number)
            if (sutra.kramaValue != expectedKrama) {
                add(SutraCatalogIssue(sutra.sutra, "krama ${sutra.kramaValue} should be $expectedKrama."))
            }
        }
    }

    private fun sutraNumber(number: String): Int =
        number.substringAfterLast('.').toIntOrNull() ?: 0
}
