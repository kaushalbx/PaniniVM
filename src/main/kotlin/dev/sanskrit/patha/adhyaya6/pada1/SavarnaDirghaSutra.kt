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
import dev.sanskrit.sutra.SutraMetadata
import dev.sanskrit.sutra.SutraType

/**
 * English: Two adjacent सवर्ण अक् vowels combine into the corresponding long vowel.
 * हिन्दी: पास-पास आए सवर्ण अक् स्वरों के स्थान पर संबंधित दीर्घ स्वर होता है।
 * Code reference: dev.sanskrit.samjna.VarnaSamjna.isSavarna; अक् is handled directly by the sandhi rule's vowel mapping.
 */
object SavarnaDirghaSutra : BaseSandhiSutra(
    SutraMetadata(
        sutraNumber = "6.1.101",
        sutraText = "अकः सवर्णे दीर्घः",
        hindiVyakhya = "अक् प्रत्याहार के स्वर के बाद उसी सवर्ण का स्वर आए तो दोनों के स्थान पर दीर्घ स्वर होता है।",
        type = SutraType.NITYA,
        adhyaya = 6,
        pada = 1,
        vaikalpika = false,
        krama = 610101,
    ),
) {
    override fun matches(context: SandhiContext): Boolean {
        val left = Shiksha.endingSvara(context.left) ?: return false
        val right = Shiksha.startingSvara(context.right) ?: return false
        return Shiksha.savarnaDirgha(left.svara, right.svara) != null
    }

    override fun apply(context: SandhiContext): BoundaryChange {
        val left = Shiksha.endingSvara(context.left)
            ?: error("Sutra applied without final svara: $context")
        val right = Shiksha.startingSvara(context.right)
            ?: error("Sutra applied without initial svara: $context")
        val dirgha = Shiksha.savarnaDirgha(left.svara, right.svara)
            ?: error("Sutra applied to non-savarna svaras: $context")
        return BoundaryChange(Shiksha.replaceFinalSvara(left, dirgha) + right.rest)
    }

    override fun split(context: VicchedaContext): List<VicchedaChange> {
        val savarnaGroups = listOf(
            Svara.AA to listOf(Svara.A, Svara.AA),
            Svara.II to listOf(Svara.I, Svara.II),
            Svara.UU to listOf(Svara.U, Svara.UU),
            Svara.RR to listOf(Svara.R, Svara.RR),
            Svara.LL to listOf(Svara.L, Svara.LL),
        )
        return savarnaGroups.flatMap { (dirgha, rightSvaras) ->
            val matra = dirgha.matra ?: return@flatMap emptyList()
            splitBeforeEach(context.pada, matra).flatMap { (left, right) ->
                rightSvaras.map { rightSvara ->
                    VicchedaChange(left, withInitialSvara(right.drop(matra.length), rightSvara))
                }
            }
        }
    }
}
