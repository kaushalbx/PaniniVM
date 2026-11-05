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
import dev.panini.analysis.KarakaDhatuDomains
import dev.panini.analysis.hasEstablishedMeaning

object AnupratigrnasCaSutra : Sutra<KarakaRuleContext, KarakaRuleResult>(
    number = "1.4.41", text = "अनुप्रतिगृणश्च",
    hindiExplanation = "अनु-गृ और प्रति-गृ धातुओं के प्रयोग में जो पूर्व क्रिया का कर्ता था, उसकी सम्प्रदानसंज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 4, optional = false, kramaValue = 140041,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.SEMANTIC_FEATURE, SutraInput.KARAKA_CANDIDATE),
    adhikara = setOf("1.4.23"),
    anuvrtti = setOf("1.4.32", "1.4.40"),
) {
    override fun matches(context: KarakaRuleContext): Boolean {
        return KarakaDhatuDomains.isEncouragementGri(context) &&
            context.participant.hasEstablishedMeaning(SemanticRelation.PRIOR_ACTION_AGENT)
    }

    override fun apply(context: KarakaRuleContext) = KarakaRuleResult.Assigned(
        Karaka.SAMPRADANA,
        KarakaEvidence(
            number,
            text,
            "The participant is the original agent to be encouraged under roots anu-gri or prati-gri."
        ),
    )
}
