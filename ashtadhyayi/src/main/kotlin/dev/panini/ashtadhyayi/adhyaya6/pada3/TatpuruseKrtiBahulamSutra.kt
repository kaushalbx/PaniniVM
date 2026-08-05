package dev.panini.ashtadhyayi.adhyaya6.pada3

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
 * Sūtra 6.3.14: तत्पुरुषे कृति बहुलम्.
 * Prescribes Aluk of Saptamī (7th case) in a Tatpuruṣa compound before a Kṛdanta uttarapada.
 * Examples: युधिष्ठिरः, सरसिजम्, मनसिजः.
 */
object TatpuruseKrtiBahulamSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.14",
    text = "तत्पुरुषे कृति बहुलम्",
    hindiExplanation = "कृदन्त उत्तरपद परे होने पर तत्पुरुष में सप्तमी विभक्ति का अलुक् (अलोप) बहुलता से होता है (उदा. युधिष्ठिरः, सरसिजम्)।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630014,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
), SamasaSutra {
    override val samasaType: SamasaType = SamasaType.ALUK_TATPURUSA
    private val alukSaptamiPurvapadas = setOf("युधि", "सरसि", "मनसि", "खे", "हृदि")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        return purva in alukSaptamiPurvapadas
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val purva = context.purvaPada.upadesha
        val uttara = context.uttaraPada.upadesha

        // Sūtra 8.4.16 / 8.3.15 retroflexion for स्थिर -> ष्ठिर after युधि
        val adjustedUttara = if (purva == "युधि" && uttara == "स्थिर") "ष्ठिर" else uttara
        val compoundStem = purva + adjustedUttara

        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.14 (तत्पुरुषे कृति बहुलम्) preserves Saptamī vibhakti for '$compoundStem'.",
        )
    }
}
