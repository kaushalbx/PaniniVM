package dev.panini.ashtadhyayi.adhyaya6.pada3

import dev.panini.analysis.*
import dev.panini.core.SamasaType
import dev.panini.shiksha.*
import dev.panini.sutra.*

/** 6.3.137 is usage-governed, not a general license to lengthen arbitrary compounds. */
object AnyesamApiDrsyateSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.137", text = "अन्येषामपि दृश्यते",
    hindiExplanation = "शिष्टप्रयोग से सिद्ध कर्मव्यतिहार समासों में पूर्वपद का दीर्घ होता है।",
    type = SutraType.NITYA, chapter = 6, pada = 3, optional = false, kramaValue = 630137,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION,
    samasaType = SamasaType.BAHUVRIHI, samasaPriority = 20,
), SamasaSutra {
    private val combatStems = setOf("केश", "कच", "दण्ड", "मुष्टि", "बाहु", "अङ्ग")
    override fun matches(context: SamasaRuleContext): Boolean = context.padas.size == 2 &&
        context.samasaType == SamasaType.BAHUVRIHI &&
        SamasaSemanticRelation.RECIPROCAL_ACTION in context.semanticRelations &&
        context.purvaPada.upadesha == context.uttaraPada.upadesha &&
        context.purvaPada.upadesha in combatStems

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val first = context.purvaPada.varnas.dropLast(1) + Svara.AA
        return SamasaRuleResult.Formed(
            (first + context.uttaraPada.varnas).toDevanagari(),
            "6.3.137 supplies the usage-attested lengthening in a combat compound.",
            memberEdits = mapOf(0 to first.toDevanagari()),
        )
    }
}
