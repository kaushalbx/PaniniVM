package dev.panini.sutra

data class SutraCatalogIssue(
    val sutra: String,
    val message: String,
)

/** Validates catalog facts independently of rule execution. */
object SutraCatalogValidator {
    fun validate(registry: SutraRegistry): List<SutraCatalogIssue> = buildList {
        registry.sutras.forEach { sutra ->
            val yogavibhaga = (sutra as? dev.panini.derivation.DerivationSutra)?.source as?
                dev.panini.sutra.runtime.SutraSource.Yogavibhaga
            if (yogavibhaga != null) {
                val parent = registry.get(yogavibhaga.parentSutra)
                if (parent == null) add(SutraCatalogIssue(sutra.sutra, "Yogavibhāga parent ${yogavibhaga.parentSutra} is not registered."))
                else if (sutra.kramaValue != parent.kramaValue || sutra.chapter != parent.chapter || sutra.pada != parent.pada)
                    add(SutraCatalogIssue(sutra.sutra, "Yogavibhāga ordering must follow its parent ${yogavibhaga.parentSutra}."))
                if (sutra.text != yogavibhaga.text || !sutra.number.startsWith("yogavibhaga:${yogavibhaga.parentSutra}:"))
                    add(SutraCatalogIssue(sutra.sutra, "Yogavibhāga identity/text does not match its declared source."))
                return@forEach
            }
            val vartika = (sutra as? dev.panini.derivation.DerivationSutra)?.source as?
                dev.panini.sutra.runtime.SutraSource.Vartika
            if (vartika != null) {
                val parent = registry.get(vartika.parentSutra)
                if (parent == null) add(SutraCatalogIssue(sutra.sutra, "Vārttika parent ${vartika.parentSutra} is not registered."))
                else if (sutra.kramaValue != parent.kramaValue || sutra.chapter != parent.chapter || sutra.pada != parent.pada)
                    add(SutraCatalogIssue(sutra.sutra, "Vārttika ordering must follow its parent ${vartika.parentSutra}."))
                if (sutra.text != vartika.text || !sutra.number.startsWith("vartika:${vartika.parentSutra}:"))
                    add(SutraCatalogIssue(sutra.sutra, "Vārttika identity/text does not match its declared source."))
                return@forEach
            }
            val expectedKrama = sutra.chapter * 100_000 + sutra.pada * 10_000 + sutraNumber(sutra.number)
            if (sutra.kramaValue != expectedKrama) {
                add(SutraCatalogIssue(sutra.sutra, "krama ${sutra.kramaValue} should be $expectedKrama."))
            }
        }
    }

    private fun sutraNumber(number: String): Int =
        number.substringAfterLast('.').toIntOrNull() ?: 0
}
