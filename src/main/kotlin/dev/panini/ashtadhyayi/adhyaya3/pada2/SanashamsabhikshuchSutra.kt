package dev.panini.ashtadhyayi.adhyaya3.pada2

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.2.168 सनाशंसभिक्ष उच्.
 * Prescribes uc affix after desiderative stems, āśaṃs, and bhikṣ roots.
 */
object SanashamsabhikshuchSutra : Sutra<String, String>(
    number = "3.2.168", text = "सनाशंसभिक्ष उच्",
    hindiExplanation = "सन्नन्त (सन्), आशंस् तथा भिक्षै धातुओं से ताच्छील्य (स्वभाव) अर्थ में 'उच्' प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 2, optional = false, kramaValue = 320168,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DHATU,
    inputs = setOf(SutraInput.DHATU),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean =
        context.endsWith("सन्") || context.endsWith("स") || context in setOf("आशंस्", "भिक्षै", "भिक्ष्")
    override fun apply(context: String): String = "उच्"
}
