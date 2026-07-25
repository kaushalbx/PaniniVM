package dev.panini.ashtadhyayi.adhyaya3.pada3

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.3.57 ऋदोरप्.
 * Prescribes ap action affix after ṛ-ending and u-ending roots.
 */
object RadorApsutra : Sutra<String, String>(
    number = "3.3.57", text = "ऋदोरप्",
    hindiExplanation = "ऋकारान्त तथा उकारान्त धातुओं से भाव एवं अकर्तृकारक अर्थ में 'अप' प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 3, optional = false, kramaValue = 330057,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DHATU,
    inputs = setOf(SutraInput.DHATU),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean = context.endsWith("ृ") || context.endsWith("ु") || context in setOf("कृ", "सृ", "प्लु", "द्रु")
    override fun apply(context: String): String = "अप्"
}
