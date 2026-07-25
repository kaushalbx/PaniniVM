package dev.panini.ashtadhyayi.adhyaya3.pada3

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.3.94 स्त्रियां क्तिन्.
 * Prescribes ktin feminine action affix.
 */
object StriyamKtinSutra : Sutra<String, String>(
    number = "3.3.94", text = "स्त्रियां क्तिन्",
    hindiExplanation = "स्त्रीलिङ्ग भाव अर्थ में धातु से 'क्तिन्' प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 3, optional = false, kramaValue = 330094,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DHATU,
    inputs = setOf(SutraInput.DHATU),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean = context.isNotBlank()
    override fun apply(context: String): String = "क्तिन्"
}
