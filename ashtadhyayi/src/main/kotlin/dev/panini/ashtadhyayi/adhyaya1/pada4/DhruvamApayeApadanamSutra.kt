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

object DhruvamApayeApadanamSutra : Sutra<KarakaRuleContext, KarakaRuleResult>(
    number = "1.4.24", text = "ध्रुवमपायेऽपादानम्",
    hindiExplanation = "अपाय में स्थिर अवधिभूत कारक की अपादानसंज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 4, optional = false, kramaValue = 140024,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.SEMANTIC_FEATURE, SutraInput.KARAKA_CANDIDATE),
    adhikara = setOf("1.4.23"),
) {
    private val specialSourceRules by lazy {
        listOf(BhitharthanamBhayahetuhSutra, ParajerasodhahSutra,
            VaranarthanamIpsitahSutra, AntardhauYenadarsanamIcchatiSutra,
            AkhyatopayogeSutra, JanikartuhPrakrtihSutra, BhuvahPrabhavahSutra)
    }

    override fun matches(context: KarakaRuleContext): Boolean {
        // A lexical family alone does not exclude an independently established
        // separation reading. Defer only to an applicable canonical construction.
        val isExcludedApadana = specialSourceRules.any { it.matches(context) }
        return !isExcludedApadana && context.participant.hasEstablishedMeaning(SemanticRelation.SOURCE) &&
            context.participant.isStrongestOrdinaryReading(SemanticRelation.SOURCE) &&
            context.participant.hasEstablishedMeaning(SemanticRelation.SEPARATION_REFERENCE)
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
