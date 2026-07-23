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

object KrudhaDruhorUpasrstayohKarmaSutra : Sutra<KarakaRuleContext, KarakaRuleResult>(
    number = "1.4.38", text = "क्रुधद्रुहोरुपसृष्टयोः कर्म",
    hindiExplanation = "सोपसर्ग क्रुध् और द्रुह् धातु के योग में जिसके प्रति कोप किया जाए, उसकी कर्मसंज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 4, optional = false, kramaValue = 140038,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.SEMANTIC_FEATURE, SutraInput.KARAKA_CANDIDATE),
    adhikara = setOf("1.4.23"),
) {
    override fun matches(context: KarakaRuleContext): Boolean {
        val normalized = context.dhatu.surface.trimEnd('्', 'ँ')
        val hasUpasarga = normalized.startsWith("अभि") || normalized.startsWith("प्र") || normalized.startsWith("प्रति") || normalized.startsWith("अनु")
        val isAngerVerb = normalized.endsWith("क्रुध्") || normalized.endsWith("द्रुह्") || normalized.endsWith("क्रुध") || normalized.endsWith("द्रुह") ||
                           normalized.contains("क्रुध्य") || normalized.contains("द्रुह्य")
        return hasUpasarga && isAngerVerb && SemanticRelation.RECIPIENT in context.participant.semanticRelations && Karaka.KARMAN in context.candidates
    }

    override fun apply(context: KarakaRuleContext) = KarakaRuleResult.Assigned(
        Karaka.KARMAN,
        KarakaEvidence(
            number,
            text,
            "The object of anger/malice for prefixed krudh/druh roots is designated as karma."
        ),
    )
}
