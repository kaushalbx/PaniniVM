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

    fun validate(sutras: Iterable<Sutra<*, *>>): List<SutraCatalogIssue> = buildList {
        sutras.forEach { sutra ->
            if (!canonicalNumber.matches(sutra.number)) {
                add(SutraCatalogIssue(sutra.number, "Noncanonical Aṣṭādhyāyī sūtra number"))
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
