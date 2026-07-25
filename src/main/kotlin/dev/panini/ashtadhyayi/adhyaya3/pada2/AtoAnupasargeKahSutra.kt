package dev.panini.ashtadhyayi.adhyaya3.pada2

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.2.3 आतोऽनुपसर्गे कः.
 * Prescribes ka affix after ā-ending roots without prefix when upapada is present.
 */
object AtoAnupasargeKahSutra : Sutra<String, String>(
    number = "3.2.3", text = "आतोऽनुपसर्गे कः",
    hindiExplanation = "अनुपसर्ग आकारान्त धातुओं से कर्म उपपद रहते 'क' प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 2, optional = false, kramaValue = 320003,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DHATU,
    inputs = setOf(SutraInput.DHATU),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean = context.endsWith("ा") || context in setOf("दा", "धा", "ज्ञा", "पा")
    override fun apply(context: String): String = "क"
}
