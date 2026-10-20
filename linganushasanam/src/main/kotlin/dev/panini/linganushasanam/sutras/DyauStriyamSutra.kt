package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 45: द्यौः स्त्रियाम् । */
object DyauStriyamSutra : LinganushasanaSutra(
    number = "45",
    text = "द्यौः स्त्रियाम्",
    hindiExplanation = "दिव् अथवा द्यौ स्त्रीलिङ्ग होता है।",
    section = LinganushasanaSection.PUMLINGA,
    targetLingas = setOf(Linga.STRI),
    priority = 50,
) {
    private val members = setOf("दिव्", "द्यौ")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

