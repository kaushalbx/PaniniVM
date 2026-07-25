package dev.panini.ashtadhyayi.adhyaya3.pada2

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.2.178 अन्येभ्योऽपि दृश्यते.
 * Prescribes kvin/kvip affixes for other roots seen in usage.
 */
object AnyaebhyopiDrshyateSutra : Sutra<String, String>(
    number = "3.2.178", text = "अन्येभ्योऽपि दृश्यते",
    hindiExplanation = "अन्य धातुओं से भी ताच्छील्य (स्वभाव) अर्थ में 'क्विन्' या 'क्विप्' (सर्वलोपी) प्रत्यय देखा जाता है।",
    type = SutraType.NITYA, chapter = 3, pada = 2, optional = false, kramaValue = 320178,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DHATU,
    inputs = setOf(SutraInput.DHATU),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean = context.isNotBlank()
    override fun apply(context: String): String = "क्विप्"
}
