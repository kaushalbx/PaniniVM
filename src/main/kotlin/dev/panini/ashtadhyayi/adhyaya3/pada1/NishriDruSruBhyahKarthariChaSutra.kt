package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.1.48 णिश्श्रिद्रुस्रुभ्यः कर्तरि चङ्.
 * Prescribes caṅ aorist vikaraṇa for ṇi, śri, dru, sru roots in Kartari Luṅ.
 */
object NishriDruSruBhyahKarthariChaSutra : Sutra<String, String>(
    number = "3.1.48", text = "णिश्श्रिद्रुस्रुभ्यः कर्तरि चङ्",
    hindiExplanation = "णिच्यन्त (णि), श्रि, द्रु तथा स्रु धातुओं से कर्तृवाच्य लुङ् लकार में 'चङ्' प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310048,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DHATU,
    inputs = setOf(SutraInput.DHATU),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean =
        context.endsWith("णिच्") || context.endsWith("णि") || context in setOf("श्रि", "द्रु", "स्रु")
    override fun apply(context: String): String = "चङ्"
}
