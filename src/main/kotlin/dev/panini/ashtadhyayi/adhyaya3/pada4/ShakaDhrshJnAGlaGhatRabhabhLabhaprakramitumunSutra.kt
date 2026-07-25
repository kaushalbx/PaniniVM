package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.4.65 शकधृषज्ञाग्लाघटरभलभक्रमसहाद्यर्थेषु तुमुन्.
 * Prescribes tumun infinitive after śak, dhṛṣ, jñā, glā, etc.
 */
object ShakaDhrshJnAGlaGhatRabhabhLabhaprakramitumunSutra : Sutra<String, String>(
    number = "3.4.65", text = "शकधृषज्ञाग्लाघटरभलभक्रमसहाद्यर्थेषु तुमुन्",
    hindiExplanation = "शक, धृष, ज्ञा, ग्ला, घट, रभ, लभ, क्रम, सह आदि धातु उपपद रहते 'तुमुन्' प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 4, optional = false, kramaValue = 340065,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DHATU,
    inputs = setOf(SutraInput.DHATU),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean =
        context in setOf("शक्", "धृष्", "ज्ञा", "ग्ला", "घट्", "रभ्", "लभ्", "क्रम्", "सह्")
    override fun apply(context: String): String = "तुमुन्"
}
