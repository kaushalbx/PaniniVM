package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.1.67 सार्वधातुके यक्.
 * Prescribes yak vikaraṇa pratyaya in passive (karmaṇi) and bhāve before Sārvadhātuka affixes.
 */
object SarvadhatukeYakSutra : Sutra<String, String>(
    number = "3.1.67", text = "सार्वधातुके यक्",
    hindiExplanation = "भाव और कर्म में सार्वधातुक प्रत्यय परे रहते धातु से 'यक्' विकरण होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310067,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DHATU,
    inputs = setOf(SutraInput.DHATU),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean = context in setOf("कर्मणि", "भाववाच्य", "कर्मवाच्य", "सार्वधातुक")
    override fun apply(context: String): String = "यक्"
}
