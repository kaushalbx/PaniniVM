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

object BhitharthanamBhayahetuhSutra : Sutra<KarakaRuleContext, KarakaRuleResult>(
    number = "1.4.25", text = "भीत्रार्थानां भयहेतुः",
    hindiExplanation = "भी और त्रा अर्थ वाली धातुओं के प्रयोग में जो भय का कारण है, उसकी अपादानसंज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 4, optional = false, kramaValue = 140025,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.SEMANTIC_FEATURE, SutraInput.KARAKA_CANDIDATE),
    adhikara = setOf("1.4.23"),
    anuvrtti = setOf("1.4.24"),
) {
    override fun matches(context: KarakaRuleContext): Boolean {
        val isFearOrProtect = KarakaDhatuDomains.isFearOrProtection(context.dhatu)
        return isFearOrProtect && context.participant.hasEstablishedMeaning(SemanticRelation.SOURCE) &&
            context.participant.hasEstablishedMeaning(SemanticRelation.FEAR_CAUSE)
    }

    override fun apply(context: KarakaRuleContext) = KarakaRuleResult.Assigned(
        Karaka.APADANA,
        KarakaEvidence(
            number,
            text,
            "The participant is the feared danger, including that from which protection is sought."
        ),
    )
}
