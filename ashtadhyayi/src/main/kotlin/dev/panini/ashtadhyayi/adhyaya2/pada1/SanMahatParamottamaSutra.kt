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
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toDevanagari

/**
 * 2.1.61: सन्महत्परमोत्तमोत्कृष्टाः पूज्यमानैः.
 *
 * Praise adjectives 'sat', 'mahat', 'parama', 'uttama', 'utkṛṣṭa' compound with praised nominals in Karmadhāraya.
 * Note: 'mahat' becomes 'mahā-' before samānādhikaraṇa nominals via 6.3.46.
 */
object SanMahatParamottamaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.61",
    text = "सन्महत्परमोत्तमोत्कृष्टाः पूज्यमानैः",
    hindiExplanation = "सत्, महत्, परम, उत्तम, उत्कृष्ट एते पूज्यमानैः समस्यन्ते, सोऽपि कर्मधारयः।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210061,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.KARMADHARAYA,
), SamasaSutra {
    private val praiseAdjectives = setOf("सत्", "महत्", "परम", "उत्तम", "उत्कृष्ट")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        return purva in praiseAdjectives
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val purva = context.purvaPada.upadesha
        val uttara = context.uttaraPada.upadesha
        val compoundStem = if (purva == "महत्" || purva == "महान्" || purva == "महा") {
            joinMahat(context.uttaraPada.varnas)
        } else {
            purva + uttara
        }

        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.61: Formed Karmadhāraya compound with praise adjective ($compoundStem).",
        )
    }

    private fun joinMahat(uttara: List<Varna>): String {
        val first = uttara.firstOrNull() ?: return mahaa.toDevanagari()
        val replacement = when (first) {
            Svara.A, Svara.AA -> mahaa + uttara.drop(1)
            Svara.I, Svara.II -> mahesha + uttara.drop(1)
            Svara.U, Svara.UU -> maho + uttara.drop(1)
            Svara.R, Svara.RR -> mahar + uttara.drop(1)
            else -> mahaa + uttara
        }
        return replacement.toDevanagari()
    }

    private val mahaa: List<Varna> = listOf(Vyanjana.MA, Svara.A, Vyanjana.HA, Svara.AA)
    private val mahesha: List<Varna> = listOf(Vyanjana.MA, Svara.A, Vyanjana.HA, Svara.E, Vyanjana.SHA, Svara.A)
    private val maho: List<Varna> = listOf(Vyanjana.MA, Svara.A, Vyanjana.HA, Svara.O)
    private val mahar: List<Varna> = listOf(Vyanjana.MA, Svara.A, Vyanjana.HA, Svara.A, Vyanjana.RA)
}
