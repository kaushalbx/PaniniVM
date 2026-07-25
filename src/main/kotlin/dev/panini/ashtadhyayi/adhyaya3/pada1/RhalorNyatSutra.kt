package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.1.124 ऋहलोर्ण्यत्.
 * Prescribes ṇyat kṛtya affix after ṛ-ending or consonant-ending roots.
 */
object RhalorNyatSutra : Sutra<String, String>(
    number = "3.1.124", text = "ऋहलोर्ण्यत्",
    hindiExplanation = "ऋकारान्त तथा हलन्त धातुओं से 'ण्यत्' कृत्य प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310124,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DHATU,
    inputs = setOf(SutraInput.DHATU),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean = context in setOf("कृ", "धृ", "पच्", "पठ्", "लिख्")
    override fun apply(context: String): String = "ण्यत्"
}
