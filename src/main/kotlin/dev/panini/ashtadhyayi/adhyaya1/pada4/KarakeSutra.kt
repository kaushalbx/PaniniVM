package dev.panini.ashtadhyayi.adhyaya1.pada4

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.vyakaranam.analysis.KarakaEvidence
import dev.panini.vyakaranam.analysis.KarakaRuleContext
import dev.panini.vyakaranam.analysis.KarakaRuleResult

/** Governing sūtra 1.4.23 कारके (A Kaḍārād Ekā Saṃjñā 1.4.1). Ensures mutual exclusivity of kāraka designations. */
object KarakeSutra : Sutra<KarakaRuleContext, KarakaRuleResult>(
    number = "1.4.23", text = "कारके",
    hindiExplanation = "अधिकारोऽयम्। इत ऊर्ध्वं या इतः प्राक् कडारादुक्ताः ताः कारके इत्यधिकारस्थत्वात् कारकाख्यत्वं लभन्ते। एका च संज्ञेति नियम्यते।",
    type = SutraType.ADHIKARA, chapter = 1, pada = 4, optional = false, kramaValue = 140023,
    role = SutraRole.Adhikara, action = SutraAction.ADHIKARA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.KARAKA_CANDIDATE),
    adhikara = emptySet(),
) {
    override fun matches(context: KarakaRuleContext): Boolean = false

    override fun apply(context: KarakaRuleContext) = KarakaRuleResult.Assigned(
        context.candidates.first(),
        KarakaEvidence(number, text, "Kāraka governing domain (1.4.23): mutual exclusivity governs kāraka designations."),
    )
}
