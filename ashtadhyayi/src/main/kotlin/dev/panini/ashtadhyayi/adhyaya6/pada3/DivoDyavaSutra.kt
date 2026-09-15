package dev.panini.ashtadhyayi.adhyaya6.pada3

import dev.panini.analysis.*
import dev.panini.core.SamasaType
import dev.panini.sutra.*

/** 6.3.29: दिवो द्यावा. */
object DivoDyavaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.29", text = "दिवो द्यावा", hindiExplanation = "देवताद्वन्द्व में दिव् को द्यावा आदेश होता है।",
    type = SutraType.NITYA, chapter = 6, pada = 3, optional = false, kramaValue = 630029,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.DERIVATION,
    samasaType = SamasaType.DVANDVA, samasaPriority = 20,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean = context.padas.size >= 2 &&
        context.samasaType == SamasaType.DVANDVA && SamasaSemanticRelation.DEVATA_COORDINATION in context.semanticRelations &&
        context.purvaPada.upadesha == "दिव्"
    override fun apply(context: SamasaRuleContext): SamasaRuleResult = SamasaRuleResult.Formed(
        "द्यावा" + context.padas.drop(1).joinToString("") { it.upadesha }, "6.3.29 substitutes dyāvā for div.",memberEdits=mapOf(0 to "द्यावा"),
    )
}
