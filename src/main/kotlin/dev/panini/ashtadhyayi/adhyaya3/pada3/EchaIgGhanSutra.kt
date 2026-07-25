package dev.panini.ashtadhyayi.adhyaya3.pada3

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.3.56 इच इग्घञि.
 * Prescribes ik substitution for ec-ending roots before ghañ affix.
 */
object EchaIgGhanSutra : Sutra<String, String>(
    number = "3.3.56", text = "इच इग्घञि",
    hindiExplanation = "घञ् प्रत्यय परे रहते एजन्त (ए, ओ, ऐ, औ) धातुओं को इक् आदेश होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 3, optional = false, kramaValue = 330056,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DHATU,
    inputs = setOf(SutraInput.DHATU),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean =
        context.endsWith("ए") || context.endsWith("ओ") || context.endsWith("ऐ") || context.endsWith("औ") ||
        context.endsWith("े") || context.endsWith("ो") || context.endsWith("ै") || context.endsWith("ौ")

    override fun apply(context: String): String =
        if (context.endsWith("ै") || context.endsWith("े") || context.endsWith("ो") || context.endsWith("ौ")) {
            context.dropLast(1) + "ि"
        } else {
            context + "इ"
        }
}
