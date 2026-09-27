package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.OrthographicSign
import dev.panini.shiksha.OrthographicSignPlacement
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

/**
 * 8.4.60: tor li.
 * The sounds of ta-varga (t, th, d, dh, n) are replaced by 'l' (or nasalized l̐ for 'n')
 * when immediately followed by 'l'.
 */
object TorliSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.60",
    text = "तोर्लि",
    hindiExplanation = "त-वर्ग (त्, थ्, द्, ध्, न्) के स्थान पर ल-कार आदेश होता है, यदि ल-कार परे हो (न् का अनुनासिक लँ्)।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 4,
    optional = false,
    kramaValue = 840060,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PADA_BOUNDARY,
    stage = SutraStage.SANDHI,
), DerivationSutra {
    private val tuVarga = setOf(Vyanjana.TA, Vyanjana.THA, Vyanjana.DA, Vyanjana.DHA, Vyanjana.NA)

    override fun matches(context: DerivationState): Boolean = findMatch(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val match = requireNotNull(findMatch(context))
        val targetTerm = context.terms[match.termIndex]
        val source = targetTerm.varnas[match.varnaIndex]
        val result = targetTerm.varnas.toMutableList().also { it[match.varnaIndex] = Vyanjana.LA }
        val signs = if (source == Vyanjana.NA) {
            targetTerm.orthographicSigns + OrthographicSignPlacement(OrthographicSign.CHANDRABINDU, match.varnaIndex)
        } else {
            targetTerm.orthographicSigns
        }

        return DerivationChange(
            state = context.substituteTermVarnas(
                targetTerm.id, result, signs, source, listOf(Vyanjana.LA), sutra,
            ),
            explanation = "8.4.60: Assimilated ta-varga to l before l."
        )
    }

    private fun findMatch(context: DerivationState): Match? {
        val varnas = context.terms.flatMapIndexed { termIndex, term ->
            term.varnas.mapIndexed { varnaIndex, varna -> OwnedVarna(termIndex, varnaIndex, varna) }
        }
        for (index in 0 until varnas.lastIndex) {
            val target = varnas[index]
            val trigger = varnas[index + 1]
            if (target.varna in tuVarga && trigger.varna == Vyanjana.LA) {
                return Match(target.termIndex, target.varnaIndex)
            }
        }
        return null
    }

    private data class OwnedVarna(val termIndex: Int, val varnaIndex: Int, val varna: Varna)
    private data class Match(val termIndex: Int, val varnaIndex: Int)
}
