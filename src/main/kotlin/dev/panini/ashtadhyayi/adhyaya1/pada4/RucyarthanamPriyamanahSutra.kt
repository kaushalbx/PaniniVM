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

object RucyarthanamPriyamanahSutra : Sutra<KarakaRuleContext, KarakaRuleResult>(
    number = "1.4.33", text = "रुच्यर्थानां प्रीयमाणः",
    hindiExplanation = "रुच् अर्थ वाली धातुओं के प्रयोग में जो प्रसन्न होने वाला है, उसकी सम्प्रदानसंज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 4, optional = false, kramaValue = 140033,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.SEMANTIC_FEATURE, SutraInput.KARAKA_CANDIDATE),
    adhikara = setOf("1.4.23"),
) {
    override fun matches(context: KarakaRuleContext): Boolean {
        val normalized = context.dhatu.surface.trimEnd('्', 'ँ')
        val isRuc = normalized == "रुच" || normalized == "रोच" || normalized.startsWith("रोच")
        return isRuc && SemanticRelation.RECIPIENT in context.participant.semanticRelations && Karaka.SAMPRADANA in context.candidates
    }

    override fun apply(context: KarakaRuleContext) = KarakaRuleResult.Assigned(
        Karaka.SAMPRADANA,
        KarakaEvidence(
            number,
            text,
            "The participant is the one who is pleased/delighted (preeyamana)."
        ),
    )
}
