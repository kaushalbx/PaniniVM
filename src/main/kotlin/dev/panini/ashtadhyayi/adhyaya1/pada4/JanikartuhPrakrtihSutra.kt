package dev.panini.ashtadhyayi.adhyaya1.pada4

import dev.panini.core.Karaka
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.analysis.KarakaEvidence
import dev.panini.analysis.KarakaRuleContext
import dev.panini.analysis.KarakaRuleResult
import dev.panini.analysis.SemanticRelation

object JanikartuhPrakrtihSutra : Sutra<KarakaRuleContext, KarakaRuleResult>(
    number = "1.4.30", text = "जनिकर्तुः प्रकृतिः",
    hindiExplanation = "उत्पन्न होने वाले के आदि कारण की अपादानसंज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 4, optional = false, kramaValue = 140030,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.SEMANTIC_FEATURE, SutraInput.KARAKA_CANDIDATE),
    adhikara = setOf("1.4.23"),
) {
    override fun matches(context: KarakaRuleContext): Boolean {
        val normalized = context.dhatu.surface.trimEnd('्', 'ँ')
        val isJan = normalized == "जन्" || normalized == "जाय्" || normalized == "जायते" || normalized.startsWith("जन")
        return isJan && SemanticRelation.SOURCE in context.participant.semanticRelations && Karaka.APADANA in context.candidates
    }

    override fun apply(context: KarakaRuleContext) = KarakaRuleResult.Assigned(
        Karaka.APADANA,
        KarakaEvidence(
            number,
            text,
            "The participant is the maternal cause/origin of a generated agent."
        ),
    )
}
