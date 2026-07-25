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
 * Sūtra 2.3.71 कृत्यानां कर्तरि वा.
 * Option for Tṛtīyā or Ṣaṣṭhī for kartā under kṛtya affixes (gerundives).
 */
object KrtyanamKartariVaSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.71", text = "कृत्यानां कर्तरि वा",
    hindiExplanation = "कृत्य-प्रत्ययान्त शब्द के योग में कर्ता में तृतीया अथवा षष्ठी विभक्ति होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = true, kramaValue = 230071,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.KARAKA, SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            context.karaka == Karaka.KARTR &&
            (Vibhakti.TRTIYA in context.morphologicalCandidates || Vibhakti.SASTHI in context.morphologicalCandidates)

    override fun apply(context: VibhaktiRuleContext): VibhaktiRuleResult {
        val v = if (Vibhakti.TRTIYA in context.morphologicalCandidates) Vibhakti.TRTIYA else Vibhakti.SASTHI
        return VibhaktiRuleResult.Assigned(
            v,
            KarakaEvidence(number, text, "$v realizes agent under kṛtya (gerundive) affix governance (2.3.71)."),
        )
    }
}
