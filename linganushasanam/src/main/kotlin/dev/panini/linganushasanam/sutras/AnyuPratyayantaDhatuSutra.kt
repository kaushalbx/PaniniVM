package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

/** Liṅgānuśāsanam 4: अन्यूप्रत्ययान्ता धातुः. */
object AnyuPratyayantaDhatuSutra : LinganushasanaSutra(
    number = "4",
    text = "अन्यूप्रत्ययान्ता धातुः",
    hindiExplanation = "अनि अथवा ऊ प्रत्ययान्त धातुज शब्द स्त्रीलिङ्ग होते हैं।",
    section = LinganushasanaSection.STRILINGA,
    targetLingas = setOf(Linga.STRI),
    priority = 40,
) {
    override fun matches(context: LingaRuleContext): Boolean =
        context.isDhatuDerivative && context.pratyaya in setOf("अनि", "ऊ")
}
