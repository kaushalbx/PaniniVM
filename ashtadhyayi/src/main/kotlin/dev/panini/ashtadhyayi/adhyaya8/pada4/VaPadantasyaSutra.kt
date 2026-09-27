package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Ayogavaha
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

/**
 * 8.4.59: vā padāntasya.
 * Word-final Anusvāra is optionally replaced by the parasavarṇa (5th nasal member of the following stop's class)
 * when followed by a yay sound.
 */
object VaPadantasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.59",
    text = "वा पदान्तस्य",
    hindiExplanation = "पदान्त अनुस्वार का ययि परे रहते विकल्प से परसवर्ण (वर्ग का ५वाँ अनुनासिक वर्ण) आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 4,
    optional = true,
    kramaValue = 840059,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PADA_BOUNDARY,
    stage = SutraStage.SANDHI,
), DerivationSutra {

    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        return (0 until context.terms.size - 1).any { i ->
            val curr = context.terms[i].varnas
            val follower = context.terms[i + 1].varnas.firstOrNull() ?: return@any false
            curr.lastOrNull() == Ayogavaha.ANUSVARA &&
                Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.YAY, follower) && nasalFor(follower) != null
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val targetIndex = (0 until context.terms.size - 1).first { i ->
            val curr = context.terms[i].varnas
            val follower = context.terms[i + 1].varnas.firstOrNull() ?: return@first false
            curr.lastOrNull() == Ayogavaha.ANUSVARA &&
                Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.YAY, follower) && nasalFor(follower) != null
        }

        val targetTerm = context.terms[targetIndex]
        val nextTerm = context.terms[targetIndex + 1]
        val follower = requireNotNull(nextTerm.varnas.firstOrNull())
        val replacement = requireNotNull(nasalFor(follower))
        val result = targetTerm.varnas.dropLast(1) + replacement

        return DerivationChange(
            state = context.substituteTermVarnas(targetTerm.id, result, Ayogavaha.ANUSVARA, listOf(replacement), sutra),
            explanation = "8.4.59: Replaced final Anusvāra with parasavarṇa '${replacement.devanagari}' before yay sound."
        )
    }

    private fun nasalFor(follower: dev.panini.shiksha.Varna): Vyanjana? = when (follower) {
        Vyanjana.KA, Vyanjana.KHA, Vyanjana.GA, Vyanjana.GHA, Vyanjana.NGA -> Vyanjana.NGA
        Vyanjana.CA, Vyanjana.CHA, Vyanjana.JA, Vyanjana.JHA, Vyanjana.NYA -> Vyanjana.NYA
        Vyanjana.TTA, Vyanjana.TTHA, Vyanjana.DDA, Vyanjana.DDHA, Vyanjana.NNA -> Vyanjana.NNA
        Vyanjana.TA, Vyanjana.THA, Vyanjana.DA, Vyanjana.DHA, Vyanjana.NA -> Vyanjana.NA
        Vyanjana.PA, Vyanjana.PHA, Vyanjana.BA, Vyanjana.BHA, Vyanjana.MA -> Vyanjana.MA
        else -> null
    }
}
