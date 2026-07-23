package dev.panini.ashtadhyayi.adhyaya1.pada4

import dev.panini.core.Karaka
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.vyakaranam.analysis.KarakaEvidence
import dev.panini.vyakaranam.analysis.KarakaRuleContext
import dev.panini.vyakaranam.analysis.KarakaRuleResult
import dev.panini.vyakaranam.analysis.SemanticRelation

object KrudhaDruhersyasuyarthanamYamPratiKopahSutra : Sutra<KarakaRuleContext, KarakaRuleResult>(
    number = "1.4.37", text = "क्रुधद्रुहेर्ष्यासूयार्थानां यं प्रति कोपः",
    hindiExplanation = "क्रुध्, द्रुह्, ईर्ष्या, असूया धातुओं के योग में जिसके प्रति कोप किया जाए, उसकी सम्प्रदानसंज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 4, optional = false, kramaValue = 140037,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.SEMANTIC_FEATURE, SutraInput.KARAKA_CANDIDATE),
    adhikara = setOf("1.4.23"),
) {
    override fun matches(context: KarakaRuleContext): Boolean {
        val normalized = context.dhatu.surface.trimEnd('्', 'ँ')
        val isAngerVerb = normalized.contains("क्रुध") || normalized.contains("द्रुह") ||
                           normalized == "ईर्ष्या" || normalized == "असूया" ||
                           normalized.startsWith("ईर्ष्य") || normalized.startsWith("असूय")
        val hasUpasarga = normalized.startsWith("अभि") || normalized.startsWith("प्र") || normalized.startsWith("प्रति") || normalized.startsWith("अनु")
        return !hasUpasarga && isAngerVerb && SemanticRelation.RECIPIENT in context.participant.semanticRelations && Karaka.SAMPRADANA in context.candidates
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
