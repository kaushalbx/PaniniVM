package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.hasAkarantaUpadha
import dev.panini.shiksha.Vyanjana

/** Liṅgānuśāsanam 86: योपधः । */
object YopadhaSutra : LinganushasanaSutra(
    number = "86", text = "योपधः", hindiExplanation = "यकारोपध अकारान्त शब्द पुंलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA, targetLingas = setOf(Linga.PUMS),
) {
    override fun matches(context: LingaRuleContext): Boolean = context.hasAkarantaUpadha(Vyanjana.YA)
}

