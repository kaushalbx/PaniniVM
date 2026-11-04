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
import dev.panini.analysis.isStrongestOrdinaryReading
import dev.panini.analysis.hasEstablishedMeaning
import dev.panini.analysis.meaningEvidenceStrength

object KarturIpsitatamamKarmaSutra : Sutra<KarakaRuleContext, KarakaRuleResult>(
    number = "1.4.49", text = "कर्तुरीप्सिततमं कर्म",
    hindiExplanation = "कर्तुः क्रियया आप्तुमिष्टतमं कारकं कर्मसंज्ञं स्यात्।",
    type = SutraType.SAMJNA, chapter = 1, pada = 4, optional = false, kramaValue = 140049,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.SEMANTIC_FEATURE, SutraInput.KARAKA_CANDIDATE),
    adhikara = setOf("1.4.23"),
) {
    override fun matches(context: KarakaRuleContext): Boolean =
        SemanticRelation.DESIRED_OBJECT in context.participant.semanticRelations &&
            context.participant.meaningEvidenceStrength(SemanticRelation.DESIRED_OBJECT) >=
                context.participant.meaningEvidenceStrength(SemanticRelation.INDIFFERENT_OBJECT) &&
            context.participant.isStrongestOrdinaryReading(SemanticRelation.DESIRED_OBJECT) &&
            SemanticRelation.CAUSEE_AGENT !in context.participant.semanticRelations &&
            SemanticRelation.SECONDARY_OBJECT !in context.participant.semanticRelations &&
            (Karaka.KARMAN in context.candidates ||
                (context.dhatu.sakarmaka &&
                    context.participant.hasEstablishedMeaning(SemanticRelation.DESIRED_OBJECT)))

    override fun apply(context: KarakaRuleContext) = KarakaRuleResult.Assigned(
        Karaka.KARMAN,
        KarakaEvidence(number, text, "The participant is most desired to be reached by the agent's action."),
    )
}
