package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.sutra.ParibhashaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 1.1.64 तस्मिन्निति निर्दिष्टे पूर्वस्य.
 * Paribhāṣā: A rule conditioned by an element in the locative case applies to the element immediately preceding it.
 */
object TasminNirdistePurvasyaSutra : Sutra<String, Boolean>(
    number = "1.1.64", text = "तस्मिन्निति निर्दिष्टे पूर्वस्य",
    hindiExplanation = "सप्तमी विभक्ति द्वारा निर्दिष्ट कार्य अव्यवहित पूर्व वर्ण के स्थान पर होता है।",
    type = SutraType.PARIBHASHA, chapter = 1, pada = 1, optional = false, kramaValue = 110064,
    role = SutraRole.Paribhasha(ParibhashaScope.LOCATIVE_TRIGGER), action = SutraAction.PARIBHASHA, scope = SutraScope.VARNA,
    inputs = setOf(SutraInput.VARNA),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean =
        context.endsWith("ि") || context.endsWith("े") || context.endsWith("इ") || context.endsWith("इति")
    override fun apply(context: String): Boolean = true
}
