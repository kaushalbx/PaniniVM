package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.4.67 कर्तरि कृत्.
 * Prescribes kṛt affixes in Kartari agent sense by default.
 */
object KartariKrtSutra : Sutra<String, String>(
    number = "3.4.67", text = "कर्तरि कृत्",
    hindiExplanation = "अविशेष रूप से विहित कृत् प्रत्यय कर्ता (कर्तृकारक) अर्थ में होते हैं।",
    type = SutraType.NITYA, chapter = 3, pada = 4, optional = false, kramaValue = 340067,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DHATU,
    inputs = setOf(SutraInput.DHATU),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean = context.isNotBlank()
    override fun apply(context: String): String = context
}
