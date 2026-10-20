package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas

/** Liṅgānuśāsanam 48: नान्तः. */
object NantaSutra : LinganushasanaSutra(
    number = "48",
    text = "नान्तः ।",
    hindiExplanation = "नकारान्त शब्द पुंलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLinga = Linga.PUMS,
    priority = 10,
) {
    override fun matches(context: LingaRuleContext): Boolean =
        context.pratipadika.toVarnas().lastOrNull() == Vyanjana.NA
}
