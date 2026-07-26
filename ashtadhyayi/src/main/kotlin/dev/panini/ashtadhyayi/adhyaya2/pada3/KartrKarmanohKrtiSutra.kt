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

/** Rule 2.3.65 कर्तृकर्मणोः कृति. Assigns Ṣaṣṭhī to unexpressed Kartā or Karman in connection with a Kṛt affix. */
object KartrKarmanohKrtiSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.65", text = "कर्तृकर्मणोः कृति",
    hindiExplanation = "कृद्योगे अनुक्ते कर्तरि कर्मणि च षष्ठी स्यात्।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230065,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.KARAKA, SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            (context.karaka == Karaka.KARTR || context.karaka == Karaka.KARMAN) &&
            Vibhakti.SASTHI in context.morphologicalCandidates

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.SASTHI,
        KarakaEvidence(number, text, "षष्ठी realizes unexpressed ${context.karaka} under primary verbal noun (kṛt) governance (2.3.65)."),
    )
}
