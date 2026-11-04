package dev.panini.ashtadhyayi.adhyaya1.pada4

import dev.panini.core.Karaka
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.analysis.KarakaEvidence
import dev.panini.analysis.KarakaDhatuDomains
import dev.panini.analysis.KarakaRuleContext
import dev.panini.analysis.KarakaRuleResult
import dev.panini.analysis.SemanticRelation
import dev.panini.analysis.hasEstablishedMeaning

object SprherIpsitahSutra : Sutra<KarakaRuleContext, KarakaRuleResult>(
    number = "1.4.36", text = "स्पृहेरीप्सितः",
    hindiExplanation = "स्पृह् धातु के प्रयोग में जो ईप्सित (चाहा हुआ) है, उसकी सम्प्रदानसंज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 4, optional = false, kramaValue = 140036,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.SEMANTIC_FEATURE, SutraInput.KARAKA_CANDIDATE),
    adhikara = setOf("1.4.23"),
    anuvrtti = setOf("1.4.32"),
) {
    override fun matches(context: KarakaRuleContext): Boolean {
        val isSprha = KarakaDhatuDomains.isSprha(context)
        // Nyāsa allows the later 1.4.49 when īpsitatamatva is independently intended.
        return isSprha && context.participant.hasEstablishedMeaning(SemanticRelation.RECIPIENT) &&
            context.participant.hasEstablishedMeaning(SemanticRelation.LONGED_FOR_OBJECT) &&
            !context.participant.hasEstablishedMeaning(SemanticRelation.DESIRED_OBJECT)
    }

    override fun apply(context: KarakaRuleContext) = KarakaRuleResult.Assigned(
        Karaka.SAMPRADANA,
        KarakaEvidence(
            number,
            text,
            "The participant is the desired object of root sprha."
        ),
    )
}
