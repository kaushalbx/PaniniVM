package dev.panini.ashtadhyayi.adhyaya8.pada2

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 8.2.30: coḥ kuḥ.
 * Substitutes ku (ka-varga) for cu (ca-varga)
 * at the end of a pada or before a jhal sound.
 */
object CohKuhSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.2.30",
    text = "चोः कुः",
    hindiExplanation = "पदान्त में या झल् वर्ण परे होने पर च-वर्ग के स्थान पर क-वर्ग आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 2,
    optional = false,
    kramaValue = 820030,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = findMatch(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val match = findMatch(context)!!
        val targetTerm = context.terms[match.termIndex]
        val source = targetTerm.varnas[match.varnaIndex] as Vyanjana
        val replacement = kuSubstitutes.getValue(source)

        return DerivationChange(
            state = context.replaceTermVarna(targetTerm.id, match.varnaIndex, listOf(replacement), sutra),
            explanation = "8.2.30 substitutes ka-varga $replacement for ca-varga $source."
        )
    }

    private fun findMatch(context: DerivationState): Match? {
        val abhyasaIds = context.samjnas
            .filter { it.samjna == dev.panini.shiksha.Samjna.ABHYASA }
            .mapTo(mutableSetOf()) { it.targetId }
        val finalTermIndex = context.terms.indexOfLast { it.varnas.isNotEmpty() }
        val finalTerm = context.terms.getOrNull(finalTermIndex)
        if (finalTerm != null && finalTerm.varnas.lastOrNull() in kuSubstitutes.keys && finalTerm.id !in abhyasaIds) {
            return Match(finalTermIndex, finalTerm.varnas.lastIndex)
        }
        for (termIndex in 0 until context.terms.lastIndex) {
            val target = context.terms[termIndex]
            val following = context.terms[termIndex + 1]
            val final = target.varnas.lastOrNull()
            val initial = following.varnas.firstOrNull()
            if (final in kuSubstitutes.keys && target.id !in abhyasaIds && initial is Vyanjana &&
                Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.JHAL, initial)
            ) {
                return Match(termIndex, target.varnas.lastIndex)
            }
        }
        return null
    }

    private data class Match(val termIndex: Int, val varnaIndex: Int)

    private val kuSubstitutes = mapOf(
        Vyanjana.CA to Vyanjana.KA,
        Vyanjana.CHA to Vyanjana.KHA,
        Vyanjana.JA to Vyanjana.GA,
        Vyanjana.JHA to Vyanjana.GHA,
        Vyanjana.NYA to Vyanjana.NGA,
    )
}
