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
 * Sūtra 2.1.58: पूर्वकालादिएकसर्वजरत्पुराणनवकेवलाः समानाधिकरणेन (registered as 2.1.111 for unique ID).
 * Prescribes Karmadhāraya with pūrvakāla, eka, sarva, jarat, purāṇa, nava, kevala.
 * Example: पूर्वं जाता = पूर्वजाता (pūrvajātā).
 */
object PurvakaladiExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.111",
    text = "पूर्वकालादिएकसर्वजरत्पुराणनवकेवलाः समानाधिकरणेन",
    hindiExplanation = "पूर्वकाल, एक, सर्व, जरत्, पुराण, नव, और केवल सुबन्तों का समानाधिकरण समर्थ सुबन्त के साथ कर्मधारय समास होता है (उदा. पूर्वजाता)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210111,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.KARMADHARAYA,
    samasaPriority = 10,
), SamasaSutra {
    private val purvakaladi = setOf("पूर्व", "एक", "सर्व", "जरत्", "पुराण", "नव", "केवल")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        return context.samasaType == SamasaType.KARMADHARAYA && purvakaladi.contains(purva)
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.111 forms Pūrvakālādi Karmadhāraya compound '$compoundStem'.",
        )
    }
}
