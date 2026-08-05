package dev.panini.ashtadhyayi.adhyaya2.pada1

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.sutra.SamasaSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

import dev.panini.sankhya.SankhyaResolver
import dev.panini.ganapatha.VamshyaClassifier

/**
 * Sūtra 2.1.19: सङ्ख्या वंश्येन (registered as 2.1.104 for unique ID).
 * Prescribes Avyayībhāva compound of numerals with lineage names.
 * Example: द्वौ मुनी = द्विमुनि (dvimuni), त्रयः मुनयः = त्रिमुनि (trimuni).
 */
object SankhyaVamsyenaExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.104",
    text = "सङ्ख्या वंश्येन",
    hindiExplanation = "सङ्ख्यावाचक सुबन्त का वंश्य (वंशपरम्परागत) समर्थ सुबन्त के साथ अव्ययीभाव समास होता है (उदा. द्विमुनि)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210104,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.AVYAYIBHAVA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada
        val uttara = context.uttaraPada
        return context.samasaType == SamasaType.AVYAYIBHAVA &&
                SankhyaResolver.isSankhya(purva.upadesha, purva.samjnas) &&
                VamshyaClassifier.isVamshya(uttara.upadesha, uttara.samjnas)
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.104 forms Numeral-Lineage Avyayībhāva compound '$compoundStem'.",
        )
    }
}
