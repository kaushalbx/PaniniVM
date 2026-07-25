package dev.panini.ashtadhyayi.adhyaya1.pada4

import dev.panini.sutra.ParibhashaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 1.4.2 विप्रतिषेधे परं कार्यम्.
 * Conflict resolution paribhāṣā: when two rules of equal strength conflict, the rule that is later in Aṣṭādhyāyī order is applied.
 */
object VipratisedheParamKaryamSutra : Sutra<Pair<Int, Int>, Int>(
    number = "1.4.2", text = "विप्रतिषेधे परं कार्यम्",
    hindiExplanation = "समान बल वाले दो सूत्रों के परस्पर विरोध होने पर अष्टाध्यायी क्रम में पर (बाद वाला) सूत्र प्रवृत्त होता है।",
    type = SutraType.PARIBHASHA, chapter = 1, pada = 4, optional = false, kramaValue = 140002,
    role = SutraRole.Paribhasha(ParibhashaScope.GENERAL), action = SutraAction.PARIBHASHA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DERIVATION_STAGE),
    adhikara = emptySet(),
) {
    override fun matches(context: Pair<Int, Int>): Boolean = context.first != context.second
    override fun apply(context: Pair<Int, Int>): Int = maxOf(context.first, context.second)
}
