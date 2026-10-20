package dev.panini.linganushasanam.adhyaya1

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

/** Liṅgānuśāsanam 6: मिन्यन्तः. */
object MinyantaSutra : LinganushasanaSutra(
    number = "6",
    text = "मिन्यन्तः ।",
    hindiExplanation = "मि अथवा नि प्रत्ययान्त धातुज शब्द स्त्रीलिङ्ग होते हैं।",
    section = LinganushasanaSection.STRILINGA,
    targetLinga = Linga.STRI,
    priority = 40,
) {
    override fun matches(context: LingaRuleContext): Boolean =
        context.isDhatuDerivative && context.pratyaya in setOf("मि", "नि")
}
