package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 1.1.26 क्तक्तवतू निष्ठा.
 * Assigns Niṣṭhā saṃjñā to kta and ktavatu affixes.
 */
object KtaKtavatuNisthaSutra : Sutra<String, String>(
    number = "1.1.26", text = "क्तक्तवतू निष्ठा",
    hindiExplanation = "क्त तथा क्तवतु प्रत्ययों की 'निष्ठा' संज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 1, optional = false, kramaValue = 110026,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.PRATYAYA,
    inputs = setOf(SutraInput.PRATYAYA),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean = context == "क्त" || context == "क्तवतु"
    override fun apply(context: String): String = "निष्ठा"
}
