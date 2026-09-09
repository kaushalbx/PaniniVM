package dev.panini.ashtadhyayi.adhyaya6.pada3

import dev.panini.analysis.*
import dev.panini.core.SamasaType
import dev.panini.sutra.*

/** 6.3.26: देवताद्वन्द्वे च. */
object DevataDvandveCaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.26", text = "देवताद्वन्द्वे च",
    hindiExplanation = "देवतानामों के द्वन्द्व में पूर्वपद को आनङ् आदेश होता है।",
    type = SutraType.NITYA, chapter = 6, pada = 3, optional = false, kramaValue = 630026,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.DERIVATION,
    samasaType = SamasaType.DVANDVA, samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean = context.padas.size >= 2 &&
        context.samasaType == SamasaType.DVANDVA && SamasaSemanticRelation.DEVATA_COORDINATION in context.semanticRelations &&
        context.purvaPada.upadesha != "अग्नि" && context.purvaPada.upadesha != "दिव्" && context.purvaPada.upadesha != "उषस्"

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val first = context.purvaPada.upadesha
        val anang = if (first.endsWith("अ")) first.dropLast(1) + "ा" else first + "ा"
        return SamasaRuleResult.Formed(anang + context.padas.drop(1).joinToString("") { it.upadesha }, "6.3.26 applies ānaṅ in a devatā-dvandva.")
    }
}
