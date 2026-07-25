package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 1.1.70 तपरस्तत्कालस्य.
 * A vowel followed or preceded by 't' represents only vowels of its own exact mora count/duration.
 */
object TaparasTatKalasyaSutra : Sutra<String, Boolean>(
    number = "1.1.70", text = "तपरस्तत्कालस्य",
    hindiExplanation = "त् जिसके बाद में हो अथवा त् के जो बाद में हो, वह अपने समान काल वाले स्वर वर्णों का ग्राहक होता है।",
    type = SutraType.PARIBHASHA, chapter = 1, pada = 1, optional = false, kramaValue = 110070,
    role = SutraRole.Paribhasha(), action = SutraAction.PARIBHASHA, scope = SutraScope.VARNA,
    inputs = setOf(SutraInput.VARNA),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean = context.endsWith("त्") || context.endsWith("त")
    override fun apply(context: String): Boolean = true
}
