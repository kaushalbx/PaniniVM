package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 1.1.24 ष्णान्ता षट्.
 * Assigns ṣaṭ saṃjñā to cardinal numeral stems ending in ṣ or n.
 */
object SnatSatSutra : Sutra<String, String>(
    number = "1.1.24", text = "ष्णान्ता षट्",
    hindiExplanation = "ष् अथवा न् वर्णों से अन्त होने वाली सङ्ख्यावाचक शब्दों की 'षट्' संज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 1, optional = false, kramaValue = 110024,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.DERIVATION,
    inputs = setOf(SutraInput.PRATIPADIKA),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean =
        context.endsWith("ष्") || context.endsWith("न्") || context.endsWith("ष") || context.endsWith("न")

    override fun apply(context: String): String = "षट्"
}
