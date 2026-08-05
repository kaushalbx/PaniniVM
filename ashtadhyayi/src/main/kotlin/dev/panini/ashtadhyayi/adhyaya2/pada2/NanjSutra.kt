package dev.panini.ashtadhyayi.adhyaya2.pada2

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.shiksha.Varnamala
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 2.2.6: नञ्.
 * Prescribes Tatpuruṣa compound of the negative particle 'nañ' (न / नञ्) with a syntactically connected nominal.
 * Morphophonological rules applied during derivation:
 * - 6.3.73 (नलोपो नञः): Deletes 'n' of 'nañ' before a consonant-initial stem, leaving 'a' (e.g. न + ब्राह्मण = अब्राह्मण).
 * - 6.3.74 (तस्मान्नुडचि): Inserts 'nuṭ' ('n') augment after 'a' before a vowel-initial stem (e.g. न + अनघ = अनघ, न + अश्व = अनश्व).
 */
object NanjSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.6",
    text = "नञ्",
    hindiExplanation = "नञ् (न) का समर्थ सुबन्त के साथ समास होता है (उदा. अब्राह्मणः, अनश्वः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220006,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
) {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        if (context.samasaType == SamasaType.NAN_TATPURUSA) return true
        val purva = context.purvaPada.upadesha
        return purva == "न" || purva == "नञ्"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val uttara = context.uttaraPada.upadesha
        val firstChar = uttara.firstOrNull() ?: ' '

        val isVowelInitial = firstChar in setOf('अ', 'आ', 'इ', 'ई', 'उ', 'ऊ', 'ऋ', 'ॠ', 'ॡ', 'ए', 'ऐ', 'ओ', 'औ') ||
                Varnamala.isVowel(firstChar)

        val compoundStem = if (isVowelInitial) {
            // 6.3.73 (नलोपो नञः) -> 'अ' + 6.3.74 (तस्मान्नुडचि) -> 'न्' + uttaraPada
            "अन" + uttara.drop(if (firstChar == 'अ') 1 else 0)
        } else {
            // 6.3.73 (नलोपो नञः) -> 'अ' + uttaraPada
            "अ" + uttara
        }

        val sutraRef = if (isVowelInitial) "2.2.6 (नञ्) with 6.3.73 (नलोपो नञः) & 6.3.74 (तस्मान्नुडचि)" else "2.2.6 (नञ्) with 6.3.73 (नलोपो नञः)"

        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "$sutraRef forms Nañ Tatpuruṣa compound '$compoundStem'.",
        )
    }
}
