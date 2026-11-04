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
import dev.panini.analysis.hasEstablishedMeaning
import dev.panini.analysis.isStrongestOrdinaryReading
import dev.panini.core.Prayoga

object SvatantrahKartaSutra : Sutra<KarakaRuleContext, KarakaRuleResult>(
    number = "1.4.54", text = "स्वतन्त्रः कर्ता",
    hindiExplanation = "क्रियायाम् स्वातन्त्र्येण विवक्षितोऽर्थः कर्ता स्यात्।",
    type = SutraType.SAMJNA, chapter = 1, pada = 4, optional = false, kramaValue = 140054,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.SEMANTIC_FEATURE, SutraInput.KARAKA_CANDIDATE),
    adhikara = setOf("1.4.23"),
) {
    override fun matches(context: KarakaRuleContext): Boolean =
        SemanticRelation.INDEPENDENT_AGENT in context.participant.semanticRelations &&
            ((context.prayoga == Prayoga.KARTARI && context.governingKrt == null) ||
                context.participant.hasEstablishedMeaning(SemanticRelation.INDEPENDENT_AGENT)) &&
            context.participant.isStrongestOrdinaryReading(SemanticRelation.INDEPENDENT_AGENT) &&
            SemanticRelation.PROMPTER_CAUSE !in context.participant.semanticRelations &&
            (Karaka.KARTR in context.candidates ||
                context.participant.hasEstablishedMeaning(SemanticRelation.INDEPENDENT_AGENT))

    override fun apply(context: KarakaRuleContext) = KarakaRuleResult.Assigned(
        Karaka.KARTR,
        KarakaEvidence(number, text, "The participant is construed as the independent agent of the action."),
    )
}
