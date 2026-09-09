package dev.panini.ashtadhyayi.adhyaya6.pada3

import dev.panini.analysis.*
import dev.panini.core.SamasaType
import dev.panini.sutra.*

/** 6.3.25: आनङ् ऋतो द्वन्द्वे. */
object AnangRtoDvandveSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.25", text = "आनङ् ऋतो द्वन्द्वे",
    hindiExplanation = "विद्या अथवा योनि सम्बन्धवाची ऋकारान्त पूर्वपद को द्वन्द्व में आनङ् आदेश होता है।",
    type = SutraType.NITYA, chapter = 6, pada = 3, optional = false, kramaValue = 630025,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.DERIVATION,
    samasaType = SamasaType.DVANDVA, samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean = context.padas.size >= 2 &&
        context.samasaType == SamasaType.DVANDVA && (context.purvaPada.upadesha.endsWith("ऋ") || context.purvaPada.upadesha.endsWith("ृ")) &&
        context.semanticRelations.any { it == SamasaSemanticRelation.STUDY_RELATION || it == SamasaSemanticRelation.BLOOD_RELATION }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult = SamasaRuleResult.Formed(
        context.purvaPada.upadesha.removeSuffix("ऋ").removeSuffix("ृ") + "ा" + context.padas.drop(1).joinToString("") { it.upadesha },
        "6.3.25 substitutes ānaṅ for the first member's final ṛ.",
    )
}
