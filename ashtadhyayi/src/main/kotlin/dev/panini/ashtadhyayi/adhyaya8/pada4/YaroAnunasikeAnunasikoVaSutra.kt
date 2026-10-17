package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.*
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.*
import dev.panini.sutra.*

/** Optional external-pada nasal assimilation. The suffix-specific obligatory
 * varttika has a separate domain and is not inferred from a following spelling.
 */
object YaroAnunasikeAnunasikoVaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.45", text = "यरोऽनुनासिकेऽनुनासिको वा",
    hindiExplanation = "पदान्त यर् का अनुनासिक परे होने पर विकल्प से अनुनासिक आदेश होता है।",
    type = SutraType.VIBHASHA, chapter = 8, pada = 4, optional = true, kramaValue = 840045,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.PADA_BOUNDARY,
    stage = SutraStage.SANDHI,
), DerivationSutra {
    private data class Target(val term: DerivationTerm, val replacement: NasalParasavarna)

    private fun target(context: DerivationState): Target? {
        for ((index, term) in context.terms.withIndex()) {
            if (!context.isPadaFinalVarna(term, term.varnas.lastIndex)) continue
            val source = term.phonologicalText.effectiveVarnas.lastOrNull() ?: continue
            if (!Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.YAR, source.varna)) continue
            val replacement = source.varna.nasalParasavarna() ?: continue
            if (source.varna == replacement.varna && source.nasalized == replacement.nasalized) continue
            val follower = context.terms.asSequence().drop(index + 1)
                .mapNotNull { it.phonologicalText.effectiveVarnas.firstOrNull() }.firstOrNull() ?: continue
            val nasalStop = (follower.varna as? Vyanjana)?.sthana?.contains(Sthana.NASIKA) == true
            if (nasalStop || follower.nasalized) return Target(term, replacement)
        }
        return null
    }

    override fun matches(context: DerivationState): Boolean = target(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val (term, replacement) = requireNotNull(target(context))
        return DerivationChange(
            context.replaceTermVarna(term.id, term.varnas.lastIndex, listOf(replacement.varna), sutra,
                nasalizedReplacement = replacement.nasalized),
            "8.4.45 optionally replaces pada-final yar with its nasal counterpart before an anunāsika.",
        )
    }
}
