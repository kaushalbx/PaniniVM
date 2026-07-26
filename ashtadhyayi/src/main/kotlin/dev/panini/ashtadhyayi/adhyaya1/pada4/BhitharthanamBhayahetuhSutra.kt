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

object BhitharthanamBhayahetuhSutra : Sutra<KarakaRuleContext, KarakaRuleResult>(
    number = "1.4.25", text = "भीत्रार्थानां भयहेतुः",
    hindiExplanation = "भी और त्रा अर्थ वाली धातुओं के प्रयोग में जो भय का कारण है, उसकी अपादानसंज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 4, optional = false, kramaValue = 140025,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.SEMANTIC_FEATURE, SutraInput.KARAKA_CANDIDATE),
    adhikara = setOf("1.4.23"),
) {
    override fun matches(context: KarakaRuleContext): Boolean {
        val normalized = context.dhatu.surface.trimEnd('्', 'ँ')
        val isFearOrProtect = normalized == "भी" || normalized == "बिभ" || normalized == "त्रा" || normalized == "त्राय" ||
                              normalized.startsWith("बिभे") || normalized.startsWith("त्राय")
        return isFearOrProtect && SemanticRelation.SOURCE in context.participant.semanticRelations && Karaka.APADANA in context.candidates
    }

    override fun apply(context: KarakaRuleContext) = KarakaRuleResult.Assigned(
        Karaka.APADANA,
        KarakaEvidence(
            number,
            text,
            "The participant is the cause of fear or source of danger/protection."
        ),
    )
}
