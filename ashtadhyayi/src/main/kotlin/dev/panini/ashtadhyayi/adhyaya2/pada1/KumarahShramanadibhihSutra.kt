package dev.panini.ashtadhyayi.adhyaya2.pada1

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.ganapatha.ShramanadiGana
import dev.panini.sutra.SamasaSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 2.1.70: कुमारः श्रमणादिभिः.
 * Prescribes Tatpuruṣa / Karmadhāraya compounding of 'kumāra' stem with members of the śramaṇādi gaṇa.
 * Examples: कुमारश्रमणा (kumāraśramaṇā), कुमारप्रव्रजिता, कुमारतापसी.
 */
object KumarahShramanadibhihSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.70",
    text = "कुमारः श्रमणादिभिः",
    hindiExplanation = "कुमार शब्द का श्रमणा आदि गण के शब्दों के साथ कर्मधारय तत्पुरुष समास होता है (उदा. कुमारश्रमणा)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210070,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.KARMADHARAYA,
    samasaPriority = 10,
), SamasaSutra {

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        val uttara = context.uttaraPada.upadesha

        val isPurvaKumara = purva == "कुमार" || purva == "कुमारी"
        val isUttaraShramanadi = ShramanadiGana.contains(uttara) || ShramanadiGana.members.any { it.text.startsWith(uttara) }

        return (context.samasaType == SamasaType.KARMADHARAYA || context.samasaType == SamasaType.TATPURUSA) &&
                isPurvaKumara && isUttaraShramanadi
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.70 forms Kumāra-śramaṇādi Tatpuruṣa compound '$compoundStem'.",
        )
    }
}
