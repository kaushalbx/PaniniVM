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
 * English: Before an अच् vowel, an इक् vowel is replaced by its यण् counterpart.
 * हिन्दी: अच् के आने पर इक् स्वर के स्थान पर उसका यण् आदेश होता है।
 * Code reference: dev.sanskrit.pratyahara.Pratyahara.AC, IK, and YAN.
 */
object IkoYanAciSutra : BaseSandhiSutra(
    SutraMetadata(
        sutraNumber = "6.1.77",
        sutraText = "इको यणचि",
        hindiVyakhya = "इक् वर्णों के बाद अच् आए तो उनके स्थान पर यण् वर्ण होते हैं।",
        type = SutraType.NITYA,
        adhyaya = 6,
        pada = 1,
        vaikalpika = false,
        krama = 610077,
    ),
) {
    override fun matches(context: SandhiContext): Boolean {
        val left = Shiksha.endingSvara(context.left) ?: return false
        Shiksha.startingSvara(context.right) ?: return false
        return left.svara in setOf(Svara.I, Svara.II, Svara.U, Svara.UU, Svara.R, Svara.RR, Svara.L, Svara.LL)
    }

    override fun apply(context: SandhiContext): BoundaryChange {
        val left = Shiksha.endingSvara(context.left)
            ?: error("Sutra applied without final svara: $context")
        val right = Shiksha.startingSvara(context.right)
            ?: error("Sutra applied without initial svara: $context")
        val replacement = when (left.svara) {
            Svara.I, Svara.II -> Vyanjana.YA
            Svara.U, Svara.UU -> Vyanjana.VA
            Svara.R, Svara.RR -> Vyanjana.RA
            Svara.L, Svara.LL -> Vyanjana.LA
            else -> error("Sutra applied to non-ik svara: $context")
        }
        return BoundaryChange(Shiksha.replaceFinalSvaraWithSamyoga(left, replacement) + right.rest)
    }

    override fun split(context: VicchedaContext): List<VicchedaChange> {
        return listOf(
            Vyanjana.YA to Svara.I,
            Vyanjana.VA to Svara.U,
            Vyanjana.RA to Svara.R,
            Vyanjana.LA to Svara.L,
        ).flatMap { (yan, ik) ->
            val samyogaMarker = Vyanjana.VIRAMA.toString() + yan.devanagari
            splitBeforeEach(context.pada, samyogaMarker).mapNotNull { (left, right) ->
                val rest = if (right.startsWith(samyogaMarker)) {
                    right.drop(samyogaMarker.length)
                } else {
                    return@mapNotNull null
                }
                val leftWithIk = if (ik.matra == null) left + ik.devanagari else left + ik.matra
                VicchedaChange(leftWithIk, withInitialSvara(rest, Svara.A))
            }
        }
    }
}
