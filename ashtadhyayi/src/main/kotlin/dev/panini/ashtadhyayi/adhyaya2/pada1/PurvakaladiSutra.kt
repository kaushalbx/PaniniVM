package dev.panini.ashtadhyayi.adhyaya2.pada1

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

import dev.panini.sutra.SamasaSutra

/**
 * 2.1.58: पूर्वकालैकसर्वजरत्पुराणनवकेवलाः समानाधिकरणेन.
 *
 * Stems 'pūrvakāla', 'eka', 'sarva', 'jarat', 'purāṇa', 'nava', 'kevala' compound with a co-referential nominal in Karmadhāraya.
 */
object PurvakaladiSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.58",
    text = "पूर्वकालैकसर्वजरत्पुराणनवकेवलाः समानाधिकरणेन",
    hindiExplanation = "पूर्वकाल, एक, सर्व, जरत्, पुराण, नव, केवल एते समानाधिकरणेन सुबन्तेन समस्यन्ते, सोऽपि कर्मधारयः।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210058,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
), SamasaSutra {
    override val samasaType: SamasaType = SamasaType.KARMADHARAYA
    private val purvakaladiWords = setOf("पूर्वकाल", "एक", "सर्व", "जरत्", "पुराण", "नव", "केवल")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        return purva in purvakaladiWords
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }

        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.58: Formed Karmadhāraya compound with pūrvakālādi modifier ($compoundStem).",
        )
    }
}
