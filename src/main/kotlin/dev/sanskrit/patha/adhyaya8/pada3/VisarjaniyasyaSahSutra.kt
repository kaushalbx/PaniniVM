package dev.sanskrit.patha.adhyaya8.pada3

import dev.sanskrit.sandhi.BaseSandhiSutra
import dev.sanskrit.sandhi.BoundaryChange
import dev.sanskrit.sandhi.SandhiContext
import dev.sanskrit.sandhi.Shiksha
import dev.sanskrit.sandhi.VicchedaChange
import dev.sanskrit.sandhi.VicchedaContext
import dev.sanskrit.sandhi.splitBeforeEach
import dev.sanskrit.shiksha.Ayogavaha
import dev.sanskrit.shiksha.Vyanjana
import dev.sanskrit.sutra.SutraMetadata
import dev.sanskrit.sutra.SutraType

/**
 * English: A visarga is replaced by स् when followed by a खर् sound.
 * हिन्दी: विसर्ग के बाद खर् वर्ण आए तो विसर्ग के स्थान पर स् आदेश होता है।
 * Code reference: dev.sanskrit.pratyahara.Pratyahara.KHAR and dev.sanskrit.sandhi.Shiksha.khar.
 */
object VisarjaniyasyaSahSutra : BaseSandhiSutra(
    SutraMetadata(
        sutraNumber = "8.3.34",
        sutraText = "विसर्जनीयस्य सः",
        hindiVyakhya = "विसर्ग के बाद खर् वर्ण आने पर विसर्ग के स्थान पर स् होता है।",
        type = SutraType.NITYA,
        adhyaya = 8,
        pada = 3,
        vaikalpika = false,
        krama = 83034,
    ),
) {
    override fun matches(context: SandhiContext): Boolean =
        context.left.endsWith(Ayogavaha.VISARGA.devanagari) && context.right.firstOrNull() in Shiksha.khar

    override fun apply(context: SandhiContext): BoundaryChange {
        return BoundaryChange(context.left.dropLast(1) + Shiksha.halanta(Vyanjana.SA) + context.right)
    }

    override fun split(context: VicchedaContext): List<VicchedaChange> {
        return splitBeforeEach(context.pada, Vyanjana.SA.halanta).map { (left, right) ->
            VicchedaChange(left + Ayogavaha.VISARGA.devanagari, right.drop(Vyanjana.SA.halanta.length))
        }
    }
}
