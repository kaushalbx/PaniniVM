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

object BhuvahPrabhavahSutra : Sutra<KarakaRuleContext, KarakaRuleResult>(
    number = "1.4.31", text = "भुवः प्रभवः",
    hindiExplanation = "भू धातु के कर्त्ता का जो उत्पत्तिस्थान है, उसकी अपादानसंज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 4, optional = false, kramaValue = 140031,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.SEMANTIC_FEATURE, SutraInput.KARAKA_CANDIDATE),
    adhikara = setOf("1.4.23"),
    anuvrtti = setOf("1.4.24", "1.4.30"),
) {
    override fun matches(context: KarakaRuleContext): Boolean {
        val isBhu = KarakaDhatuDomains.isOriginBhu(context.dhatu)
        return isBhu && context.participant.hasEstablishedMeaning(SemanticRelation.SOURCE) &&
            context.participant.hasEstablishedMeaning(SemanticRelation.FIRST_MANIFESTATION_SOURCE)
    }

    override fun apply(context: KarakaRuleContext) = KarakaRuleResult.Assigned(
        Karaka.APADANA,
        KarakaEvidence(
            number,
            text,
            "The participant is the place where the bhū-agent first becomes manifest."
        ),
    )
}
