package dev.panini.ashtadhyayi.adhyaya3.pada2

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.2.124 लटः शतृशानचावप्रथमासमानाधिकरणे.
 * Prescribes śatṛ and śānac active/middle participle affixes in Laṭ.
 */
object LatahSatriShanacauSutra : Sutra<String, String>(
    number = "3.2.124", text = "लटः शतृशानचावप्रथमासमानाधिकरणे",
    hindiExplanation = "अप्रथमा समानाधिकरण में लट् लकार के स्थान पर 'शतृ' तथा 'शानच्' प्रत्यय होते हैं।",
    type = SutraType.NITYA, chapter = 3, pada = 2, optional = false, kramaValue = 320124,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DHATU,
    inputs = setOf(SutraInput.DHATU),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean = context in setOf("लट्", "वर्तमान", "सत्")
    override fun apply(context: String): String = "शतृ"
}
