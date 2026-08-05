package dev.panini.ashtadhyayi.adhyaya6.pada3

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 6.3.21: आत्मनश्च पूरणे.
 * Prescribes Aluk of case endings for 'ātmane' / 'parasmai' before 'pada' etc.
 * Examples: आत्मनेपदम्, परस्मैपदम्.
 */
object AtmanascaPuraneSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.21",
    text = "आत्मनश्च पूरणे",
    hindiExplanation = "आत्मने तथा परस्मै पदों का पद उत्तरपद परे होने पर अलुक् होता है (उदा. आत्मनेपदम्, परस्मैपदम्)।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630021,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
) {
    private val alukPurvapadas = setOf("आत्मने", "परस्मै")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        return purva in alukPurvapadas
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val stem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = stem,
            explanation = "6.3.21 (आत्मनश्च पूरणे) preserves case ending for Aluk compound '$stem'.",
        )
    }
}
