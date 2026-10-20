package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

/** Liṅgānuśāsanam 39: नङन्तः. */
object NangantaSutra : LinganushasanaSutra(
    number = "39",
    text = "नङन्तः ।",
    hindiExplanation = "नङ् प्रत्ययान्त शब्द पुंलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLinga = Linga.PUMS,
    priority = 40,
) {
    override fun matches(context: LingaRuleContext): Boolean = context.pratyaya == "नङ्"
}
