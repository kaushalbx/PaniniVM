package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.4.111 लङ्ः शाकटायनस्यैव.
 * Prescribes jus substitution in Laṅ for 3rd person plural according to Śākaṭāyana.
 */
object LanSakatayanasyaIvaSutra : Sutra<String, String>(
    number = "3.4.111", text = "लङ्ः शाकटायनस्यैव",
    hindiExplanation = "शाकटायन आचार्य के मत में लङ् लकार के 'झि' स्थान में 'जुस्' (उः) होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 4, optional = true, kramaValue = 340111,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.PRATYAYA,
    inputs = setOf(SutraInput.PRATYAYA),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean = context in setOf("झि", "अन्", "लङ्")
    override fun apply(context: String): String = "जुस्"
}
