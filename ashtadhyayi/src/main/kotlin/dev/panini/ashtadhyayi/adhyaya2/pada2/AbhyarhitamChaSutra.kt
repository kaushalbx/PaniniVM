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
 * 2.2.32: अभ्यर्हितं च.
 */
object AbhyarhitamChaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.32",
    text = "अभ्यर्हितं च",
    hindiExplanation = "द्वन्द्वे अभ्यर्हितं पूजितं पूर्वं प्रयोक्तव्यम्।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220032,
    role = SutraRole.Vidhi,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
), SamasaSutra {
    override val samasaType: SamasaType = SamasaType.DVANDVA
    private val venerableWords = setOf("माता", "मातृ", "गुरु", "हरि", "ईश", "पितृ")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val padas = context.padas.map { it.upadesha }
        return context.samasaType == SamasaType.DVANDVA && padas.any { it in venerableWords }
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val sortedPadas = context.padas.sortedByDescending { it.upadesha in venerableWords }
        val compoundStem = sortedPadas.joinToString("") { it.upadesha }

        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.2.32: Ordered venerable member first in Dvandva ($compoundStem).",
        )
    }
}
