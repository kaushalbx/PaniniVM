package dev.panini.linganushasanam.adhyaya1

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

/** Liṅgānuśāsanam 9: क्तिन्नन्तः. */
object KtinnantaCanonicalSutra : LinganushasanaSutra(
    number = "9",
    text = "क्तिन्नन्तः ।",
    hindiExplanation = "क्तिन् प्रत्ययान्त शब्द स्त्रीलिङ्ग होते हैं।",
    section = LinganushasanaSection.STRILINGA,
    targetLinga = Linga.STRI,
    priority = 40,
) {
    override fun matches(context: LingaRuleContext): Boolean = context.pratyaya == "क्तिन्"
}
