package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 1.1.71 आदिरन्त्येन सहेता.
 * Saṃjñā: An initial sound combined with a final it-marker forms a pratyāhāra representing all intermediate sounds.
 */
object AdirAntyenaSahetaSutra : Sutra<String, String>(
    number = "1.1.71", text = "आदिरन्त्येन सहेता",
    hindiExplanation = "अन्तिम इत्-संज्ञक वर्ण के साथ आदि वर्ण मिलकर प्रत्याहार बनाता है, जो मध्यवर्ती वर्णों का बोधक होता है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 1, optional = false, kramaValue = 110071,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.VARNA,
    inputs = setOf(SutraInput.PRATYAHARA, SutraInput.IT_MARKER),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean = context.endsWith("्") || context.endsWith("च्") || context.endsWith("ल्")
    override fun apply(context: String): String = "प्रत्याहार"
}
