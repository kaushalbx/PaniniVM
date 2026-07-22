package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.core.Karaka
import dev.panini.core.Vibhakti
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.vyakaranam.analysis.VibhaktiRuleContext
import dev.panini.vyakaranam.analysis.VibhaktiRuleResult
import dev.panini.vyakaranam.analysis.KarakaEvidence

object SaptamyAdhikaraneCaSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.36", text = "सप्तम्यधिकरणे च", hindiExplanation = "अनभिहित अधिकरण में सप्तमी होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230036,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.KARAKA, SutraInput.SEMANTIC_FEATURE), adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext) = context.accepts(Karaka.ADHIKARANA, Vibhakti.SAPTAMI)
    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.SAPTAMI,
        KarakaEvidence(number, text, "सप्तमी realizes ${context.karaka} in this construction."),
    )
}
