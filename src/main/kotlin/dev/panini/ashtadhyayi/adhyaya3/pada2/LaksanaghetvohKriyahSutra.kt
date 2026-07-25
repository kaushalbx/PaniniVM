package dev.panini.ashtadhyayi.adhyaya3.pada2

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.2.126 लक्षणहेत्वोः क्रियायाः.
 * Prescribes śatṛ and śānac in continuous/causal conditions.
 */
object LaksanaghetvohKriyahSutra : Sutra<String, String>(
    number = "3.2.126", text = "लक्षणहेत्वोः क्रियायाः",
    hindiExplanation = "लक्षण तथा हेतु अर्थ प्रकट करने वाली क्रिया से लट् के स्थान पर 'शतृ' व 'शानच्' प्रत्यय होते हैं।",
    type = SutraType.NITYA, chapter = 3, pada = 2, optional = false, kramaValue = 320126,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DHATU,
    inputs = setOf(SutraInput.DHATU),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean = context in setOf("लक्षण", "हेतु", "कारण")
    override fun apply(context: String): String = "शतृ"
}
