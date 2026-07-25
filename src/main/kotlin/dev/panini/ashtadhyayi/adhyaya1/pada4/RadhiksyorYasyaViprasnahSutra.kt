package dev.panini.ashtadhyayi.adhyaya1.pada4

import dev.panini.core.Karaka
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.vyakaranam.analysis.KarakaEvidence
import dev.panini.vyakaranam.analysis.KarakaRuleContext
import dev.panini.vyakaranam.analysis.KarakaRuleResult
import dev.panini.vyakaranam.analysis.SemanticRelation

/**
 * Sūtra 1.4.38 राधिक्योर्यस्य विप्रश्नः.
 * Assigns Sampradāna saṃjñā to the person whose destiny/fate is being inquired into via rādh or īkṣ.
 */
object RadhiksyorYasyaViprasnahSutra : Sutra<KarakaRuleContext, KarakaRuleResult>(
    number = "1.4.39", text = "राधिक्ष्योर्यस्य विप्रश्नः",
    hindiExplanation = "राध् तथा ईक्ष् धातु के योग में जिसके भावी शुभाशुभ विषय में प्रश्न किया जाए, उसकी सम्प्रदान संज्ञा होती है।",
    type = SutraType.NITYA, chapter = 1, pada = 4, optional = false, kramaValue = 140039,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.KARAKA_CANDIDATE, SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("1.4.1", "1.4.23"),
) {
    override fun matches(context: KarakaRuleContext): Boolean =
        (context.candidates.isEmpty() || Karaka.SAMPRADANA in context.candidates) &&
            (context.dhatu.surface.startsWith("राध्") || context.dhatu.surface.startsWith("ईक्ष्") || context.dhatu.surface == "राध" || context.dhatu.surface == "ईक्ष") &&
            SemanticRelation.INQUIRY_DESTINY_TARGET in context.participant.semanticRelations

    override fun apply(context: KarakaRuleContext) = KarakaRuleResult.Assigned(
        Karaka.SAMPRADANA,
        KarakaEvidence(number, text, "सम्प्रदान saṃjñā for target of destiny inquiry with rādh/īkṣ (1.4.38)."),
    )
}
