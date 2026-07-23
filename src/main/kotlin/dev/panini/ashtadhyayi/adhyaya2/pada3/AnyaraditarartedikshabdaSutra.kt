package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.core.Karaka
import dev.panini.core.Vibhakti
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.vyakaranam.analysis.KarakaEvidence
import dev.panini.vyakaranam.analysis.VibhaktiRuleContext
import dev.panini.vyakaranam.analysis.VibhaktiRuleResult

/**
 * Sūtra 2.3.29 अन्यारादितरर्तेदिक्छब्दाञ्चूत्तरपदाजाहियुक्ते.
 * Assigns Pañcamī in connection with words for other (anya, itara), remote/near (ārāt),
 * exclusion (ṛte), cardinal directions (dikśabda), and añcu-suffixed words.
 */
object AnyaraditarartedikshabdaSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.29", text = "अन्यारादितरर्तेदिक्छब्दाञ्चूत्तरपदाजाहियुक्ते",
    hindiExplanation = "अन्य-आरात्-इतर-ऋते-दिक्शब्द-अञ्चूत्तरपद-आच्-आहि इत्येतैर्योगे पञ्चमी स्यात्।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230029,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.KARAKA, SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            (context.karaka == Karaka.APADANA || context.karaka == Karaka.ANIRDHARITA) &&
            Vibhakti.PANCHAMI in context.morphologicalCandidates

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.PANCHAMI,
        KarakaEvidence(number, text, "पञ्चमी realizes separation/exclusion/direction relation (2.3.29)."),
    )
}
