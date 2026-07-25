package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.sutra.ParibhashaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 1.1.66 तस्मादित्युत्तरस्य.
 * Paribhāṣā: A rule conditioned by an element in the ablative case applies to the element immediately following it.
 */
object TasmatItyUttarasyamSutra : Sutra<String, Boolean>(
    number = "1.1.66", text = "तस्मादित्युत्तरस्य",
    hindiExplanation = "पञ्चमी विभक्ति द्वारा निर्दिष्ट कार्य अव्यवहित उत्तर वर्ण के स्थान पर होता है।",
    type = SutraType.PARIBHASHA, chapter = 1, pada = 1, optional = false, kramaValue = 110066,
    role = SutraRole.Paribhasha(ParibhashaScope.ABLATIVE_TRIGGER), action = SutraAction.PARIBHASHA, scope = SutraScope.VARNA,
    inputs = setOf(SutraInput.VARNA),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean = context.endsWith("ात्") || context.endsWith("तः")
    override fun apply(context: String): Boolean = true
}
