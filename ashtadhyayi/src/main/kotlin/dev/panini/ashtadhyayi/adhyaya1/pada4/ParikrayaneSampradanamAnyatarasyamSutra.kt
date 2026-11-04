package dev.panini.ashtadhyayi.adhyaya1.pada4
import dev.panini.analysis.isSelectedInstrument

import dev.panini.core.Karaka
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.analysis.KarakaEvidence
import dev.panini.analysis.meaningEvidenceStrength
import dev.panini.analysis.KarakaRuleContext
import dev.panini.analysis.KarakaRuleResult
import dev.panini.analysis.SemanticRelation
import dev.panini.analysis.OptionalKarakaRuleOutcome

object ParikrayaneSampradanamAnyatarasyamSutra : Sutra<KarakaRuleContext, KarakaRuleResult>(
    number = "1.4.44", text = "परिक्रयणे सम्प्रदानमन्यतरस्याम्",
    hindiExplanation = "परिक्रयण (नियत समय के लिए मजदूरी देकर खरीदना) में साधकतम कारक की सम्प्रदान संज्ञा भी विकल्प से होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 4, optional = true, kramaValue = 140044,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.SEMANTIC_FEATURE, SutraInput.KARAKA_CANDIDATE),
    adhikara = setOf("1.4.23"),
    anuvrtti = setOf("1.4.42"),
), OptionalKarakaRuleOutcome {
    override fun declinedDesignation(context: KarakaRuleContext) = SadhakatamamKaranamSutra.apply(context)
    override fun matches(context: KarakaRuleContext): Boolean {
        return context.participant.meaningEvidenceStrength(SemanticRelation.FIXED_TERM_HIRING_PAYMENT) >= 2 &&
            context.participant.isSelectedInstrument()
    }

    override fun apply(context: KarakaRuleContext) = KarakaRuleResult.Assigned(
        Karaka.SAMPRADANA,
        KarakaEvidence(
            number,
            text,
            "The instrument of purchase is optionally designated as sampradana."
        ),
    )
}
