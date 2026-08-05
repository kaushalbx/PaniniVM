package dev.panini.ashtadhyayi.adhyaya2.pada2

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.sutra.SamasaSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 2.2.26: तेन सहेति तुल्ययोगे (registered as 2.2.99 for unique ID).
 * Prescribes Bahuvrīhi compound with saha in equal association.
 * Example: सह पत्न्या वर्तते = सपत्नीकः / सहपत्नीकः.
 */
object TenaSahetiTulyayogeExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.99",
    text = "तेन सहेति तुल्ययोगे",
    hindiExplanation = "तुल्ययोग (समान सम्बन्ध) अर्थ में सह शब्द का तृतीयान्त सुबन्त के साथ बहुव्रीहि समास होता है (उदा. सपत्नीकः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220099,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.BAHUVRIHI,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        return context.samasaType == SamasaType.BAHUVRIHI && (purva == "सह" || purva == "स")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.2.99 forms Saha Bahuvrīhi compound '$compoundStem'.",
        )
    }
}
