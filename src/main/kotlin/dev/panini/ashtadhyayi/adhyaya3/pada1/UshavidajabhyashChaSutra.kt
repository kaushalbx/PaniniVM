package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.1.38 उषविदजाकृभ्योऽन्तरस्याम्.
 * Prescribes optional ām pratyaya for uṣ, vid, jāgṛ roots in Lit.
 */
object UshavidajabhyashChaSutra : Sutra<String, String>(
    number = "3.1.38", text = "उषविदजाकृभ्योऽन्तरस्याम्",
    hindiExplanation = "उष्, विद् तथा जागृ धातुओं से लिट् लकार में विकल्प से 'आम' प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = true, kramaValue = 310038,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DHATU,
    inputs = setOf(SutraInput.DHATU),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean = context in setOf("उष्", "विद्", "जागृ")
    override fun apply(context: String): String = "आम"
}
