package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.hasAkarantaUpadha
import dev.panini.shiksha.Vyanjana

/** Liṅgānuśāsanam 70: थोपधः. */
object ThopadhaSutra : LinganushasanaSutra(
    number = "70", text = "थोपधः ।", hindiExplanation = "थकारोपध अकारान्त शब्द पुंलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA, targetLinga = Linga.PUMS,
) {
    override fun matches(context: LingaRuleContext): Boolean = context.hasAkarantaUpadha(Vyanjana.THA)
}
