package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.1.26 हेतुमति च.
 * Prescribes ṇic causative pratyaya when prompting an agent to perform an action.
 */
object HetumatiCaSutra : Sutra<String, String>(
    number = "3.1.26", text = "हेतुमति च",
    hindiExplanation = "प्रयोजक व्यापार (हेतुमान् अर्थ) में धातु से 'णिच्' प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310026,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DHATU,
    inputs = setOf(SutraInput.DHATU),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean = context.isNotBlank()
    override fun apply(context: String): String = "णिच्"
}
