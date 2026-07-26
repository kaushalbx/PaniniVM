package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.core.Karaka
import dev.panini.core.Vibhakti
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.analysis.KarakaEvidence
import dev.panini.analysis.VibhaktiRuleContext
import dev.panini.analysis.VibhaktiRuleResult

object KartrkaranayostrtiyaSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.18", text = "कर्तृकरणयोस्तृतीया", hindiExplanation = "अनभिहित कर्ता अथवा करण में तृतीया होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230018,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.KARAKA, SutraInput.SEMANTIC_FEATURE), adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext) =
        !context.abhihita && context.karaka in setOf(Karaka.KARTR, Karaka.KARANA) && Vibhakti.TRTIYA in context.morphologicalCandidates
    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.TRTIYA,
        KarakaEvidence(number, text, "तृतीया realizes ${context.karaka} in this construction."),
    )
}
