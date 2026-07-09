package dev.sanskrit.patha.adhyaya6.pada1

import dev.sanskrit.sandhi.BaseSandhiSutra
import dev.sanskrit.sandhi.BoundaryChange
import dev.sanskrit.sandhi.SandhiContext
import dev.sanskrit.sandhi.Shiksha
import dev.sanskrit.sandhi.VicchedaChange
import dev.sanskrit.sandhi.VicchedaContext
import dev.sanskrit.sandhi.splitBeforeEach
import dev.sanskrit.sandhi.withInitialSvara
import dev.sanskrit.shiksha.Svara
import dev.sanskrit.shiksha.Vyanjana
import dev.sanskrit.sutra.SutraMetadata
import dev.sanskrit.sutra.SutraType

/**
 * English: After अ or आ, following इक् vowels take their गुण substitute in sandhi.
 * हिन्दी: अ या आ के बाद इक् स्वर आए तो संधि में उसका गुणादेश होता है।
 * Code reference: dev.sanskrit.pratyahara.Pratyahara.IK and dev.sanskrit.samjna.SvaraSamjna.gunaForIk.
 */
object AdGunaSutra : BaseSandhiSutra(
    SutraMetadata(
        sutraNumber = "6.1.87",
        sutraText = "आद्गुणः",
        hindiVyakhya = "अ या आ के बाद इ/ई, उ/ऊ, ऋ/ॠ आदि स्वर आएं तो गुणादेश होता है।",
        type = SutraType.NITYA,
        adhyaya = 6,
        pada = 1,
        vaikalpika = false,
        krama = 610087,
    ),
) {
    override fun matches(context: SandhiContext): Boolean {
        val left = Shiksha.endingSvara(context.left) ?: return false
        val right = Shiksha.startingSvara(context.right) ?: return false
        return left.svara in setOf(Svara.A, Svara.AA) &&
            right.svara in setOf(Svara.I, Svara.II, Svara.U, Svara.UU, Svara.R, Svara.RR, Svara.L, Svara.LL)
    }

    override fun apply(context: SandhiContext): BoundaryChange {
        val left = Shiksha.endingSvara(context.left)
            ?: error("Sutra applied without final svara: $context")
        val right = Shiksha.startingSvara(context.right)
            ?: error("Sutra applied without initial svara: $context")
        val replacement = when (right.svara) {
            Svara.I, Svara.II -> Svara.E
            Svara.U, Svara.UU -> Svara.O
            Svara.R, Svara.RR -> Svara.A
            Svara.L, Svara.LL -> Svara.A
            else -> error("Sutra applied to non-guna svara: $context")
        }
        val yanalopa = when (right.svara) {
            Svara.R, Svara.RR -> Vyanjana.RA.devanagari
            Svara.L, Svara.LL -> Vyanjana.LA.devanagari
            else -> ""
        }
        return BoundaryChange(Shiksha.replaceFinalSvara(left, replacement) + yanalopa + right.rest)
    }

    override fun split(context: VicchedaContext): List<VicchedaChange> {
        val eSplits = splitBeforeEach(context.pada, Svara.E.matra ?: return emptyList()).map { (left, right) ->
            VicchedaChange(left, withInitialSvara(right.drop(1), Svara.I))
        }
        val oSplits = splitBeforeEach(context.pada, Svara.O.matra ?: return emptyList()).map { (left, right) ->
            VicchedaChange(left, withInitialSvara(right.drop(1), Svara.U))
        }
        return eSplits + oSplits
    }
}
