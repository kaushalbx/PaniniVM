package dev.panini.ashtadhyayi.adhyaya2.pada1

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.core.KrtAffix
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

import dev.panini.sutra.SamasaSutra

/**
 * 2.1.60: क्तेन नञ्विशिष्टेनानञ्.
 *
 * A non-negated kta-participial word compounds with a negated (Nñ) kta-participle in Karmadhāraya to form antonym pairs (e.g. 'kṛtākṛtam').
 */
object KtenaNanjVisistenaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.60",
    text = "क्तेन नञ्विशिष्टेनानञ्",
    hindiExplanation = "नञ्विशिष्टेन क्तान्तेन सह अनञ् क्तान्तं समस्यते, सोऽपि कर्मधारयः।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210060,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.KARMADHARAYA,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size != 2) return false
        val purva = context.purvaPada
        val uttara = context.uttaraPada
        val base = uttara.nanjBase ?: return false
        return purva.nanjBase == null && base.nanjBase == null &&
            purva.krtAffix == KrtAffix.KTA && base.krtAffix == KrtAffix.KTA &&
            purva.varnas == base.varnas && purva.vibhakti == uttara.vibhakti
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        // Classification changes no member sounds; preserve spelling/signs for the engine's boundary renderer.
        val compoundStem = context.padas.joinToString("") { it.upadesha }

        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.60: Formed Karmadhāraya compound of kta-participial antonym pair ($compoundStem).",
        )
    }
}
