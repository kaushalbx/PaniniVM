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

object ApadanePancamiSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.28", text = "अपादाने पञ्चमी", hindiExplanation = "अनभिहित अपादान में पञ्चमी होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230028,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.KARAKA, SutraInput.SEMANTIC_FEATURE), adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext) = context.accepts(Karaka.APADANA, Vibhakti.PANCHAMI)
    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.PANCHAMI,
        KarakaEvidence(number, text, "पञ्चमी realizes ${context.karaka} in this construction."),
    )
}
