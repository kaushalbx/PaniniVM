package dev.panini.ashtadhyayi.adhyaya5.pada4

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
 * Sūtra 5.4.117: प्रमाणे द्वयसज्दघ्नञ्मात्रचः.
 * Prescribes measurement suffixes dvayasac, daghnañ, mātrac.
 */
object PramaneDvayasajDaghnanSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.117",
    text = "प्रमाणे द्वयसज्दघ्नञ्मात्रचः",
    hindiExplanation = "प्रमाण अर्थ में द्वयसच्, दघ्नञ् तथा मात्रच् प्रत्यय होते हैं।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540117,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.BAHUVRIHI,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return last == "द्वयस" || last == "दघ्न" || last == "मात्र"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.117 applies measurement suffix rule for '$compoundStem'.",
        )
    }
}
