package dev.panini.ashtadhyayi.adhyaya3.pada3

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.3.156 हेतुहेतुमतोर्लिङ्.
 * Prescribes liṅ mood in cause-and-effect condition.
 */
object HetuhetumatorLingSutra : Sutra<String, String>(
    number = "3.3.156", text = "हेतुहेतुमतोर्लिङ्",
    hindiExplanation = "हेतु (कारण) तथा हेतुमान् (कार्य) सम्बन्धी क्रियाओं में धातु से 'लिङ्' लकार होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 3, optional = false, kramaValue = 330156,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DHATU,
    inputs = setOf(SutraInput.DHATU),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean = context in setOf("हेतु", "कार्य", "कारण")
    override fun apply(context: String): String = "लिङ्"
}
