package dev.panini.ashtadhyayi.adhyaya2.pada2

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.shiksha.Varnamala
import dev.panini.sutra.SamasaSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 2.2.6: नञ्.
 * Prescribes Nañ Tatpuruṣa compound for negative prefix 'nañ' (na) with a subanta.
 * Sūtra 6.3.73: नलोपो नञः (deletes 'n' of nañ -> 'a').
 * Sūtra 6.3.74: तस्मान्नुडचि (inserts 'n' augment before vowel-initial terms).
 * Examples: न + ब्राह्मणः = अब्राह्मणः, न + अश्वः = अनश्वः, न + ईश्वरः = अनीश्वरः.
 */
object NanjSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.6",
    text = "नञ्",
    hindiExplanation = "नञ् समर्थेन सुबन्तेन सह समस्यते, सोऽपि तत्पुरुषः। (६.३.७३ नलोपो नञः एवं ६.३.७४ तस्मान्नुडचि)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220006,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.NAN_TATPURUSA,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        if (context.samasaType == SamasaType.NAN_TATPURUSA) return true
        val purva = context.purvaPada.upadesha
        return purva == "न" || purva == "नञ्" || purva == "अ"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val uttara = context.uttaraPada.upadesha
        val firstChar = uttara.firstOrNull() ?: ' '

        val isVowelInitial = firstChar in setOf('अ', 'आ', 'इ', 'ई', 'उ', 'ऊ', 'ऋ', 'ॠ', 'ॡ', 'ए', 'ऐ', 'ओ', 'औ') ||
                Varnamala.isVowel(firstChar)

        val compoundStem = if (isVowelInitial) {
            // 6.3.73 (नलोपो नञः) -> 'अ' + 6.3.74 (तस्मान्नुडचि) -> 'न्' + uttaraPada
            combineHalantaNaWithVowel(uttara)
        } else {
            // 6.3.73 (नलोपो नञः) -> 'अ' + uttaraPada
            "अ$uttara"
        }

        val sutraRef = if (isVowelInitial) "2.2.6 (नञ्) with 6.3.73 (नलोपो नञः) & 6.3.74 (तस्मान्नुडचि)" else "2.2.6 (नञ्) with 6.3.73 (नलोपो नञः)"

        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "$sutraRef forms Nañ Tatpuruṣa compound '$compoundStem'.",
        )
    }

    private fun combineHalantaNaWithVowel(uttara: String): String {
        val firstChar = uttara.firstOrNull() ?: return uttara
        val rest = uttara.drop(1)
        val matra = when (firstChar) {
            'अ' -> ""
            'आ' -> "ा"
            'इ' -> "ि"
            'ई' -> "ी"
            'उ' -> "ु"
            'ऊ' -> "ू"
            'ऋ' -> "ृ"
            'ए' -> "े"
            'ऐ' -> "ै"
            'ओ' -> "ो"
            'औ' -> "ौ"
            else -> return "अन्$uttara"
        }
        return "अन$matra$rest"
    }
}
