package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.endsWithVarnas

/** Liṅgānuśāsanam 17: तलन्तः. */
object TalantaCanonicalSutra : LinganushasanaSutra(
    number = "17",
    text = "तलन्तः",
    hindiExplanation = "तल् प्रत्ययान्त, सामान्यतः ता-अन्त, शब्द स्त्रीलिङ्ग होते हैं।",
    section = LinganushasanaSection.STRILINGA,
    targetLingas = setOf(Linga.STRI),
    priority = 30,
) {
    override fun matches(context: LingaRuleContext): Boolean =
        context.pratyaya == "तल्" || context.pratipadika.endsWithVarnas("ता")
}
