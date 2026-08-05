package dev.panini.ashtadhyayi.adhyaya2.pada2

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
 * 2.2.34: अल्पाच्तरम्.
 */
object AlpactaramSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.34",
    text = "अल्पाच्तरम्",
    hindiExplanation = "अल्पस्वरयुक्तं पदं द्वन्द्वे पूर्वं प्रयोक्तव्यम्।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220034,
    role = SutraRole.Vidhi,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.DVANDVA,
    samasaPriority = 1,
), SamasaSutra {
    private val vowels = setOf('अ', 'आ', 'इ', 'ई', 'उ', 'ऊ', 'ऋ', 'ॠ', 'ए', 'ऐ', 'ओ', 'औ')

    private fun countVowels(s: String): Int = s.count { it in vowels }

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        return context.samasaType == SamasaType.DVANDVA
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val sortedPadas = context.padas.sortedBy { countVowels(it.upadesha) }
        val compoundStem = sortedPadas.joinToString("") { it.upadesha }

        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.2.34: Placed member with fewer vowels first in Dvandva ($compoundStem).",
        )
    }
}
