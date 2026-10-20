package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.core.LingaSemanticClass
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

/** Liṅgānuśāsanam 41: क्यन्तो घुः. */
object KyantaGhuSutra : LinganushasanaSutra(
    number = "41",
    text = "क्यन्तो घुः",
    hindiExplanation = "घु-संज्ञक धातु से कि प्रत्ययान्त शब्द पुंलिङ्ग होता है।",
    section = LinganushasanaSection.PUMLINGA,
    targetLingas = setOf(Linga.PUMS),
    priority = 40,
) {
    override fun matches(context: LingaRuleContext): Boolean =
        context.pratyaya == "कि" && LingaSemanticClass.GHU_ROOT in context.semanticClasses
}
