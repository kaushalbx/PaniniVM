package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Ayogavaha
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.Varna
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
            val next = context.terms[i + 1].varnas.firstOrNull() ?: return@any false
            eligible(context, i) && Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.ASH, next)
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val targetIndex = (0 until context.terms.size - 1).first { i ->
            val next = context.terms[i + 1].varnas.firstOrNull() ?: return@first false
            eligible(context, i) && Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.ASH, next)
        }

        val targetTerm = context.terms[targetIndex]

        return DerivationChange(
            state = context.replaceTermVarna(targetTerm.id, targetTerm.varnas.lastIndex, listOf(Vyanjana.YA), sutra),
            explanation = "8.3.17: Replaced ru/visarga with 'y' before aś sound."
        )
    }

    private fun eligible(context: DerivationState, index: Int): Boolean {
        val term = context.terms[index]
        val final = term.varnas.lastOrNull()
        if (final !in setOf(Vyanjana.SA, Ayogavaha.VISARGA, Vyanjana.RA)) return false
        // A native lexical r is not ru: prātar/punar are explicit counterexamples.
        if (final == Vyanjana.RA && context.substitutions.none { it.targetId == term.id && it.sutra == "8.2.66" }) return false
        val base = term.varnas.dropLast(1)
        return base.lastOrNull() in setOf(Svara.A, Svara.AA) || base in lexicalBases
    }

    private val lexicalBases: Set<List<Varna>> = setOf(
        listOf(Vyanjana.BHA, Svara.O),
        listOf(Vyanjana.BHA, Svara.A, Vyanjana.GA, Svara.O),
        listOf(Svara.A, Vyanjana.GHA, Svara.O),
    )
}
