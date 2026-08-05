package dev.panini.ashtadhyayi.adhyaya2.pada1

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
 * Sūtra 2.1.68: कुत्सितानि कुत्सितैः.
 * Prescribes Karmadhāraya compound when a blameworthy word (kutsita) compounds with another blameworthy word.
 * Example: वैयाकरणखसूचिः (vaiyākaraṇakhasūciḥ - a bad grammarian looking at sky).
 */
object KutsitaniKutsitaihSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.53",
    text = "कुत्सितानि कुत्सितैः",
    hindiExplanation = "कुत्सित (निन्दित) अर्थ वाले प्रथमान्त सुबन्तों का कुत्सितवाचक सुबन्तों के साथ कर्मधारय समास होता है (उदा. वैयाकरणखसूचिः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210053,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.KARMADHARAYA,
    samasaPriority = 10,
), SamasaSutra {
    private val kutsitaUttaras = setOf("खसूचि", "हटक", "कुकुर्द", "कुत्सित", "हीन")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val uttara = context.uttaraPada.upadesha
        return context.samasaType == SamasaType.KARMADHARAYA &&
            kutsitaUttaras.contains(uttara)
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.68 forms Kutsita Karmadhāraya compound '$compoundStem'.",
        )
    }
}
