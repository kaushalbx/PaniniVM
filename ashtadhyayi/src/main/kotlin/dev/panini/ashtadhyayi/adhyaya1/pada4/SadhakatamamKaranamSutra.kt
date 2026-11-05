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
import dev.panini.analysis.isSelectedInstrument
import dev.panini.analysis.isStrongestOrdinaryReading

object SadhakatamamKaranamSutra : Sutra<KarakaRuleContext, KarakaRuleResult>(
    number = "1.4.42", text = "साधकतमं करणम्",
    hindiExplanation = "क्रियासिद्धि में प्रकृष्ट साधन की करणसंज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 4, optional = false, kramaValue = 140042,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.SEMANTIC_FEATURE, SutraInput.KARAKA_CANDIDATE),
    adhikara = setOf("1.4.23"),
) {
    override fun matches(context: KarakaRuleContext): Boolean =
        context.participant.isSelectedInstrument() &&
            context.participant.isStrongestOrdinaryReading(SemanticRelation.INSTRUMENT) &&
            !DivahKarmaCaSutra.matches(context) &&
            !ParikrayaneSampradanamAnyatarasyamSutra.matches(context)

    override fun apply(context: KarakaRuleContext) = KarakaRuleResult.Assigned(
        Karaka.KARANA,
        KarakaEvidence(number, text, "The participant is construed as the most effective instrument."),
    )
}
