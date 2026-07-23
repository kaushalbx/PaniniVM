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

object DivahKarmaCaSutra : Sutra<KarakaRuleContext, KarakaRuleResult>(
    number = "1.4.43", text = "दिवः कर्म च",
    hindiExplanation = "दिव् धातु के साधकतम (करण) कारक की कर्म संज्ञा भी होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 4, optional = true, kramaValue = 140043,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.SEMANTIC_FEATURE, SutraInput.KARAKA_CANDIDATE),
    adhikara = setOf("1.4.23"),
) {
    override fun matches(context: KarakaRuleContext): Boolean {
        val normalized = context.dhatu.surface.trimEnd('्', 'ँ')
        val isDiv = normalized == "दिव" || normalized == "दीव्" || normalized.startsWith("दीव्य")
        return isDiv && SemanticRelation.INSTRUMENT in context.participant.semanticRelations && Karaka.KARMAN in context.candidates
    }

    override fun apply(context: KarakaRuleContext) = KarakaRuleResult.Assigned(
        Karaka.KARMAN,
        KarakaEvidence(
            number,
            text,
            "The instrument of root div is optionally designated as karma."
        ),
    )
}
