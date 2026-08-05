package dev.panini.ashtadhyayi.adhyaya2.pada2

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 2.2.33: अजाद्यदन्तम्.
 */
object AjadyadantamSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.33",
    text = "अजाद्यदन्तम्",
    hindiExplanation = "अजादि अदन्तं च पदं द्वन्द्वे पूर्वं प्रयोक्तव्यम्।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220033,
    role = SutraRole.Vidhi,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
) {
    private val vowels = setOf('अ', 'आ', 'इ', 'ई', 'उ', 'ऊ', 'ऋ', 'ॠ', 'ए', 'ऐ', 'ओ', 'औ')

    private fun isAjadyadanta(s: String): Boolean =
        s.isNotEmpty() && s.first() in vowels && s.endsWith("अ")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val padas = context.padas.map { it.upadesha }
        return context.samasaType == SamasaType.DVANDVA && padas.any { isAjadyadanta(it) }
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val sortedPadas = context.padas.sortedByDescending { isAjadyadanta(it.upadesha) }
        val compoundStem = sortedPadas.joinToString("") { it.upadesha }

        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.2.33: Placed vowel-initial a-ending member first ($compoundStem).",
        )
    }
}
