package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.core.Vibhakti
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.vyakaranam.analysis.KarakaEvidence
import dev.panini.vyakaranam.analysis.SemanticRelation
import dev.panini.vyakaranam.analysis.VibhaktiRuleContext
import dev.panini.vyakaranam.analysis.VibhaktiRuleResult

/**
 * 2.3.26: ktasya ca vartamāne.
 * Assigns genitive (Ṣaṣṭhī) to the agent when ktā-participle denotes present time.
 */
object KasyaCaVartamaneSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.26",
    text = "कस्य च वर्तमानी",
    hindiExplanation = "वर्तमान अर्थ में प्रयुक्त क्त-प्रत्ययान्त शब्द के योग में कर्ता में षष्ठी विभक्ति होती है।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 3,
    optional = false,
    kramaValue = 230026,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.KARAKA)
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        Vibhakti.SASTHI in context.morphologicalCandidates &&
            context.participant?.semanticRelations?.contains(SemanticRelation.PRESENT_PARTICIPLE_AGENT) == true

    override fun apply(context: VibhaktiRuleContext): VibhaktiRuleResult =
        VibhaktiRuleResult.Assigned(
            Vibhakti.SASTHI,
            KarakaEvidence(number, text, "Assigns Ṣaṣṭhī for agent with present-time kta participle.")
        )
}
