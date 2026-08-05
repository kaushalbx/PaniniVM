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
 * Sūtra 2.1.69: कुत्सितानि कुत्सितैः (registered as 2.1.101 for unique ID).
 * Prescribes Karmadhāraya compound with blameworthy terms.
 */
object KutsitaniKutsitaihExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.101",
    text = "कुत्सितानि कुत्सितैः",
    hindiExplanation = "कुत्सित अर्थ वाले सुबन्त का कुत्सितवाचक समर्थ सुबन्त के साथ कर्मधारय समास होता है (उदा. वैयाकरणखसूचिः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210101,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.KARMADHARAYA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        return context.padas.size >= 2 && context.samasaType == SamasaType.KARMADHARAYA
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.101 forms Kutsita Karmadhāraya compound '$compoundStem'.",
        )
    }
}
