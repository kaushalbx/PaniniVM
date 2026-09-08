package dev.panini.ashtadhyayi

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraCatalogIssue
import dev.panini.vyakaranam.parser.PaniniParser

/**
 * Validates Aṣṭādhyāyī metadata that depends on the segmented Sanskrit parser.
 *
 * A missing segmented source is allowed while the catalog is being migrated.
 * Once supplied, the source must be a complete, parsable PaniniVM utterance.
 */
object AshtadhyayiSutraValidator {
    private val parser = PaniniParser()
    private val canonicalNumber = Regex("[1-8]\\.[1-4]\\.\\d+V?")
    private val canonicalPadaLimits = mapOf(
        (2 to 1) to 72,
        (2 to 2) to 38,
    )

    fun validate(sutras: Iterable<Sutra<*, *>>): List<SutraCatalogIssue> = buildList {
        sutras.forEach { sutra ->
            if (!canonicalNumber.matches(sutra.number)) {
                add(SutraCatalogIssue(sutra.number, "Noncanonical Aṣṭādhyāyī sūtra number"))
            } else {
                val components = sutra.number.removeSuffix("V").split('.').map(String::toInt)
                val limit = canonicalPadaLimits[components[0] to components[1]]
                if (limit != null && components[2] > limit) {
                    add(SutraCatalogIssue(sutra.number, "Sūtra number exceeds the canonical limit for ${components[0]}.${components[1]} ($limit)"))
                }
            }
            val source = sutra.segmentedSource ?: return@forEach
            parser.validate(source).forEach { error ->
                add(
                    SutraCatalogIssue(
                        sutra = sutra.number,
                        message = "Invalid segmented source at ${error.line}:${error.column}: ${error.message}",
                    ),
                )
            }
        }
    }
}
