package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Ayogavaha
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

/**
 * 8.3.17: bho-bhago-agho-apūrvasya yo'śi.
 * The sound 'ru' (s/ḥ) following bhoḥ, bhagoḥ, aghoḥ, or a short/long 'a' (a-pūrva / ā-pūrva)
 * is replaced by 'y' before an aś sound (vowels + voiced consonants).
 */
object BhoBhagoAghoApurvasyaYoshiSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.17",
    text = "भोभगोअघोअपूर्वस्य योऽशि",
    hindiExplanation = "भोः, भगोः, अघोः तथा अ/आ पूर्व वाले रुँ (विसर्ग/सकार) के स्थान पर 'अश्' परे होने पर 'य' आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 3,
    optional = false,
    kramaValue = 830017,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PADA_BOUNDARY,
    stage = SutraStage.SANDHI,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        return (0 until context.terms.size - 1).any { i ->
            val curr = context.terms[i].varnas
            val next = context.terms[i + 1].varnas.firstOrNull() ?: return@any false
            ruSpan(curr) != null && Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.ASH, next)
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val targetIndex = (0 until context.terms.size - 1).first { i ->
            val curr = context.terms[i].varnas
            val next = context.terms[i + 1].varnas.firstOrNull() ?: return@first false
            ruSpan(curr) != null && Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.ASH, next)
        }

        val targetTerm = context.terms[targetIndex]
        val (dropCount, source) = requireNotNull(ruSpan(targetTerm.varnas))
        val replacement = if (dropCount == 2 || source == Ayogavaha.VISARGA) {
            listOf(Vyanjana.YA, Svara.A)
        } else {
            listOf(Vyanjana.YA)
        }

        return DerivationChange(
            state = context.replaceTermVarnaRange(
                targetTerm.id, targetTerm.varnas.size - dropCount, dropCount, replacement,
                if (dropCount == 2) mapOf(0 to 0, 1 to 1) else mapOf(0 to 0), sutra,
            ),
            explanation = "8.3.17: Replaced ru/visarga with 'y' before aś sound."
        )
    }

    private fun ruSpan(varnas: List<dev.panini.shiksha.Varna>): Pair<Int, dev.panini.shiksha.Varna>? = when {
        varnas.takeLast(2) == listOf(Vyanjana.SA, Svara.A) -> 2 to Vyanjana.SA
        varnas.lastOrNull() == Vyanjana.SA -> 1 to Vyanjana.SA
        varnas.lastOrNull() == Ayogavaha.VISARGA -> 1 to Ayogavaha.VISARGA
        else -> null
    }
}
