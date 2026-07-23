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

object AdharoAdhikaranamSutra : Sutra<KarakaRuleContext, KarakaRuleResult>(
    number = "1.4.45", text = "आधारोऽधिकरणम्",
    hindiExplanation = "कर्तृकर्मद्वारा तन्निष्ठक्रियायाः आधारस्य कारकस्य अधिकरणसंज्ञा भवति।",
    type = SutraType.SAMJNA, chapter = 1, pada = 4, optional = false, kramaValue = 140045,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.SEMANTIC_FEATURE, SutraInput.KARAKA_CANDIDATE),
    adhikara = setOf("1.4.23"),
) {
    override fun matches(context: KarakaRuleContext): Boolean {
        val normalized = context.dhatu.surface.trimEnd('्', 'ँ')
        val isAdhishi = normalized == "अधिशी" || normalized == "अधिस्था" || normalized == "अधिआस्" ||
                        normalized == "अधिशे" || normalized == "अधितिष्ठ्" || normalized == "अध्यास्" ||
                        normalized.startsWith("अधिशे") || normalized.startsWith("अधितिष्ठ") || normalized.startsWith("अध्यास्")
        return !isAdhishi && SemanticRelation.LOCATION in context.participant.semanticRelations && Karaka.ADHIKARANA in context.candidates
    }

    override fun apply(context: KarakaRuleContext) = KarakaRuleResult.Assigned(
        Karaka.ADHIKARANA,
        KarakaEvidence(number, text, "The participant is construed as the spatial or temporal locus of action."),
    )
}
