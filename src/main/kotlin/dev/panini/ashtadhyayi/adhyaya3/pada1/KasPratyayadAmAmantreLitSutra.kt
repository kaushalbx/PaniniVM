package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.1.35 कास्प्रत्ययादाममन्त्रे लिटि.
 * Prescribes ām pratyaya for kās and pratyayānta roots in Lit.
 */
object KasPratyayadAmAmantreLitSutra : Sutra<String, String>(
    number = "3.1.35", text = "कास्प्रत्ययादाममन्त्रे लिटि",
    hindiExplanation = "कास् धातु से तथा प्रत्ययान्त (सनाद्यन्त) धातुओं से लिट् लकार में 'आम' प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310035,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DHATU,
    inputs = setOf(SutraInput.DHATU),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean =
        context == "कास्" || context.endsWith("णिच्") || context.endsWith("सन्") || context.endsWith("यङ्") || context.endsWith("आय")
    override fun apply(context: String): String = "आम"
}
