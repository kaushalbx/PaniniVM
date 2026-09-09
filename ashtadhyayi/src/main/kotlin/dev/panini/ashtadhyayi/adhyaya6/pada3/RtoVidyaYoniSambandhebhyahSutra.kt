package dev.panini.ashtadhyayi.adhyaya6.pada3

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.analysis.SamasaSemanticRelation
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import dev.panini.sutra.SamasaSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 6.3.23: ऋतो विद्यायोनिसम्बन्धेभ्यः. */
object RtoVidyaYoniSambandhebhyahSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.23",
    text = "ऋतो विद्यायोनिसम्बन्धेभ्यः",
    hindiExplanation = "विद्या अथवा योनि सम्बन्ध में ऋकारान्त पूर्वपद की षष्ठी विभक्ति का अलुक् होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630023,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.ALUK_TATPURUSA,
    samasaPriority = 20,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean =
        context.padas.size >= 2 &&
            context.samasaType == SamasaType.ALUK_TATPURUSA &&
            context.purvaPada.vibhakti == Vibhakti.SASTHI &&
            (context.purvaPada.upadesha.endsWith("ऋ") || context.purvaPada.upadesha.endsWith("ृ")) &&
            context.semanticRelations.any {
                it == SamasaSemanticRelation.STUDY_RELATION || it == SamasaSemanticRelation.BLOOD_RELATION
            }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult = SamasaRuleResult.Formed(
        compoundStem = context.padas.joinToString("") { it.upadesha },
        explanation = "6.3.23 preserves the genitive ending after an ṛ-final relationship word.",
    )
}
