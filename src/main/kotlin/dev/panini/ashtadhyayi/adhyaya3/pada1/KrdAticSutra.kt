package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.1.93 कृदतिङ्.
 * Assigns kṛt saṃjñā to non-tiṅ affixes prescribed after a root.
 */
object KrdAticSutra : Sutra<String, String>(
    number = "3.1.93", text = "कृदतिङ्",
    hindiExplanation = "धातु अधिकार में विहित तिङ् से भिन्न प्रत्ययों की 'कृत्' संज्ञा होती है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310093,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.PRATYAYA,
    inputs = setOf(SutraInput.PRATYAYA),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean = context !in setOf("तिप्", "तस्", "झि", "सिप्", "थस्", "थ", "मिप्", "वस्", "मस्", "त", "आताम्", "झ", "थास्", "आथाम्", "ध्वम्", "इट्", "वहि", "महिङ्")
    override fun apply(context: String): String = context
}
