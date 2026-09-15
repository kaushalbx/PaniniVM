package dev.panini.ashtadhyayi.adhyaya6.pada3

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.analysis.SamasaSemanticRelation
import dev.panini.core.SamasaType
import dev.panini.sutra.*

/** 6.3.24: विभाषा स्वसृपत्योः. */
object VibhashaSvasrPatyorSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.24", text = "विभाषा स्वसृपत्योः",
    hindiExplanation = "स्वसृ अथवा पति उत्तरपद होने पर सम्बन्धवाची ऋकारान्त पूर्वपद की विभक्ति का विकल्प से अलुक् होता है।",
    type = SutraType.VIBHASHA, chapter = 6, pada = 3, optional = true, kramaValue = 630024,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.DERIVATION,
    samasaType = SamasaType.ALUK_TATPURUSA, samasaPriority = 30,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean = context.padas.size >= 2 &&
        context.samasaType == SamasaType.ALUK_TATPURUSA &&
        (context.purvaPada.upadesha.endsWith("ऋ") || context.purvaPada.upadesha.endsWith("ृ")) && context.uttaraPada.upadesha in setOf("स्वसृ", "पति") &&
        context.semanticRelations.any { it == SamasaSemanticRelation.STUDY_RELATION || it == SamasaSemanticRelation.BLOOD_RELATION }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult = SamasaRuleResult.Formed(
        context.padas.joinToString("") { it.upadesha }, "6.3.24 optionally preserves the case ending before svasṛ or pati.",
    )
}
