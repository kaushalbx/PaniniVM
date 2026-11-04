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

object KrudhaDruhersyasuyarthanamYamPratiKopahSutra : Sutra<KarakaRuleContext, KarakaRuleResult>(
    number = "1.4.37", text = "क्रुधद्रुहेर्ष्यासूयार्थानां यं प्रति कोपः",
    hindiExplanation = "क्रुध्, द्रुह्, ईर्ष्या, असूया धातुओं के योग में जिसके प्रति कोप किया जाए, उसकी सम्प्रदानसंज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 4, optional = false, kramaValue = 140037,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.SEMANTIC_FEATURE, SutraInput.KARAKA_CANDIDATE),
    adhikara = setOf("1.4.23"),
    anuvrtti = setOf("1.4.32"),
) {
    override fun matches(context: KarakaRuleContext): Boolean {
        val isAngerVerb = KarakaDhatuDomains.isAnger(context)
        val hasUpasarga = KarakaDhatuDomains.hasAngerUpasarga(context)
        return hasUpasarga != null && !KarakaDhatuDomains.isPrefixedKrudhDruh(context) &&
            isAngerVerb && context.participant.hasEstablishedMeaning(SemanticRelation.RECIPIENT) &&
            context.participant.hasEstablishedMeaning(SemanticRelation.ANGER_TARGET)
    }

    override fun apply(context: KarakaRuleContext) = KarakaRuleResult.Assigned(
        Karaka.SAMPRADANA,
        KarakaEvidence(
            number,
            text,
            "The participant is the recipient of anger, malice, jealousy or detraction."
        ),
    )
}
