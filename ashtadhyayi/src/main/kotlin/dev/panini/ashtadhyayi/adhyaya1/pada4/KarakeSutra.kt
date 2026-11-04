package dev.panini.ashtadhyayi.adhyaya1.pada4

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.analysis.KarakaRuleContext
import dev.panini.analysis.KarakaRuleResult

/** 1.4.23 कारके governs the action-related domain of the following designations. */
object KarakeSutra : Sutra<KarakaRuleContext, KarakaRuleResult>(
    number = "1.4.23", text = "कारके",
    hindiExplanation = "अधिकारोऽयम्। क्रियासम्बन्धिनि कारके अपादानादिसंज्ञाः विधीयन्ते।",
    type = SutraType.ADHIKARA, chapter = 1, pada = 4, optional = false, kramaValue = 140023,
    role = SutraRole.Adhikara(endKrama = 140055), action = SutraAction.ADHIKARA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.KARAKA_CANDIDATE),
    adhikara = emptySet(),
) {
    override fun matches(context: KarakaRuleContext): Boolean = false

    override fun apply(context: KarakaRuleContext): KarakaRuleResult =
        throw UnsupportedOperationException("1.4.23 is an adhikāra; it does not assign a participant's kāraka.")
}
