package dev.sanskrit.patha.adhyaya8.pada3

import dev.sanskrit.sandhi.BaseSandhiSutra
import dev.sanskrit.sandhi.BoundaryChange
import dev.sanskrit.sandhi.SandhiContext
import dev.sanskrit.sandhi.Shiksha
import dev.sanskrit.shiksha.Ayogavaha
import dev.sanskrit.shiksha.Vyanjana
import dev.sanskrit.sutra.SutraMetadata
import dev.sanskrit.sutra.SutraType

object KharavasanayorVisarjaniyahSutra : BaseSandhiSutra(
    SutraMetadata(
        sutraNumber = "8.3.15",
        sutraText = "खरवसानयोर्विसर्जनीयः",
        hindiVyakhya = "र् या स् के बाद खर् वर्ण हो या पद का अवसान हो तो विसर्ग आदेश होता है।",
        type = SutraType.NITYA,
        adhyaya = 8,
        pada = 3,
        vaikalpika = false,
        krama = 83015,
    ),
) {
    override fun matches(context: SandhiContext): Boolean {
        val last = context.left.lastOrNull() ?: return false
        return (last == Vyanjana.RA.devanagari.single() || last == Vyanjana.SA.devanagari.single()) &&
            (context.right.isEmpty() || context.right.first() in Shiksha.khar)
    }

    override fun apply(context: SandhiContext): BoundaryChange {
        return BoundaryChange(context.left.dropLast(1) + Ayogavaha.VISARGA.devanagari + context.right)
    }
}
