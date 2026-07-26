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

object ShlaghahnusthashapamJnyipsyamanahSutra : Sutra<KarakaRuleContext, KarakaRuleResult>(
    number = "1.4.34", text = "श्लाघह्नुस्थाशपां ज्ञीप्स्यमानः",
    hindiExplanation = "श्लाघ्, ह्नु, स्था और शप् धातुओं के प्रयोग में जो ज्ञीप्स्यमान (जिसको जताया जाता है) है, उसकी सम्प्रदानसंज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 4, optional = false, kramaValue = 140034,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.SEMANTIC_FEATURE, SutraInput.KARAKA_CANDIDATE),
    adhikara = setOf("1.4.23"),
) {
    override fun matches(context: KarakaRuleContext): Boolean {
        val normalized = context.dhatu.surface.trimEnd('्', 'ँ')
        val isShlaghEtc = normalized == "श्लाघ" || normalized == "ह्नु" || normalized == "स्था" || normalized == "शप" ||
                          normalized.startsWith("श्लाघ") || normalized.startsWith("ह्नु") || normalized.startsWith("तिष्ठ") || normalized.startsWith("शप")
        return isShlaghEtc && SemanticRelation.RECIPIENT in context.participant.semanticRelations && Karaka.SAMPRADANA in context.candidates
    }

    override fun apply(context: KarakaRuleContext) = KarakaRuleResult.Assigned(
        Karaka.SAMPRADANA,
        KarakaEvidence(
            number,
            text,
            "The participant is the one to be informed/intended target of praise/conceal/stand/curse."
        ),
    )
}
