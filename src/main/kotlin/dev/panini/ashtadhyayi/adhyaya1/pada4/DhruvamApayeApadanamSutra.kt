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

object DhruvamApayeApadanamSutra : Sutra<KarakaRuleContext, KarakaRuleResult>(
    number = "1.4.24", text = "ध्रुवमपायेऽपादानम्",
    hindiExplanation = "अपाय में स्थिर अवधिभूत कारक की अपादानसंज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 4, optional = false, kramaValue = 140024,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.SEMANTIC_FEATURE, SutraInput.KARAKA_CANDIDATE),
    adhikara = setOf("1.4.23"),
) {
    override fun matches(context: KarakaRuleContext): Boolean {
        val normalized = context.dhatu.surface.trimEnd('्', 'ँ')
        val isBhuOrJan = normalized == "भू" || normalized == "भव्" || normalized == "प्रभू" || normalized == "प्रभव्" ||
                         normalized.startsWith("भव") || normalized.startsWith("प्रभव") ||
                         normalized == "जन्" || normalized == "जाय्" || normalized == "जायते" || normalized.startsWith("जन")
        return !isBhuOrJan && SemanticRelation.SOURCE in context.participant.semanticRelations && Karaka.APADANA in context.candidates
    }

    override fun apply(context: KarakaRuleContext) = KarakaRuleResult.Assigned(
        Karaka.APADANA,
        KarakaEvidence(
            number,
            text,
            "The participant is the fixed point from which separation occurs."
        ),
    )
}
