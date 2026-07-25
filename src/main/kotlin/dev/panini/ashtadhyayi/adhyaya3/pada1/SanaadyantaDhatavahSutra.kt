package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.1.32 सनाद्यन्ता धातवः.
 * Assigns dhātu saṃjñā to secondary stems ending in san and other sanādyanta affixes.
 */
object SanaadyantaDhatavahSutra : Sutra<String, String>(
    number = "3.1.32", text = "सनाद्यन्ता धातवः",
    hindiExplanation = "सन् आदि प्रत्यय (सन्, यङ्, णिच् आदि) जिनके अन्त में हों, उनकी 'धातु' संज्ञा होती है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310032,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.DHATU,
    inputs = setOf(SutraInput.DHATU),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean =
        context.endsWith("सन्") || context.endsWith("यङ्") || context.endsWith("णिच्") ||
        context.endsWith("स") || context.endsWith("य") || context.endsWith("ष") || context.endsWith("षा") ||
        context.contains("सन्") || context.contains("णिच्") || context.contains("यङ्")
    override fun apply(context: String): String = context
}
