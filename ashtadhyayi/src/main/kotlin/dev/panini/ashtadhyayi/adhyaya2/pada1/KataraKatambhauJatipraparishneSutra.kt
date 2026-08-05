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
 * Sūtra 2.1.65: कतरकतभौ जातिपरिप्रश्ने.
 * Prescribes Karmadhāraya compound of 'katara' or 'katama' in questions about class/lineage.
 * Example: कतरकठः, कतमकठः.
 */
object KataraKatambhauJatipraparishneSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.65",
    text = "कतरकतभौ जातिपरिप्रश्ने",
    hindiExplanation = "जाति प्रश्न अर्थ में कतर और कतम शब्दों का समर्थ सुबन्त के साथ कर्मधारय समास होता है (उदा. कतरकठः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210065,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.KARMADHARAYA,
    samasaPriority = 10,
), SamasaSutra {
    private val kataraWords = setOf("कतर", "कतम")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        return context.samasaType == SamasaType.KARMADHARAYA &&
            purva in kataraWords
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.65 forms Katara-Katama Karmadhāraya compound '$compoundStem'.",
        )
    }
}
