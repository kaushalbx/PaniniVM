package dev.panini.ashtadhyayi.adhyaya2.pada1

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
 * 2.1.68: पापे कुत्सितैः / पापकैः कुत्सितैः.
 *
 * Deprecatory adjectives 'pāpa', 'kutsita' compound with depreciated nominals in Karmadhāraya.
 */
object PapakeKutsitaihsutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.68",
    text = "पापे कुत्सितैः",
    hindiExplanation = "पाप-शब्दः कुत्सितैः सुबन्तैः सह समस्यते, सोऽपि कर्मधारयः।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210068,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.KARMADHARAYA,
), SamasaSutra {
    private val deprecatoryWords = setOf("पाप", "कुत्सित", "कुत्सि")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        return purva in deprecatoryWords
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }

        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.68: Formed Karmadhāraya compound with deprecatory adjective ($compoundStem).",
        )
    }
}
