package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.1.30 कमेरणिङ्.
 * Prescribes ṇiṅ pratyaya for kam root.
 */
object KamerNingSutra : Sutra<String, String>(
    number = "3.1.30", text = "कमेरणिङ्",
    hindiExplanation = "कमूँ (कामी) धातु से 'णिङ्' प्रत्यय स्वार्थ में होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310030,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DHATU,
    inputs = setOf(SutraInput.DHATU),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean = context in setOf("कम्", "कमूँ", "कामय")
    override fun apply(context: String): String = "णिङ्"
}
