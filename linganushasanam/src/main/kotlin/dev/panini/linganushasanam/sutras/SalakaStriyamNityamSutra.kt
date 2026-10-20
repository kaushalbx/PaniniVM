package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 34: शलाका स्त्रियां नित्यम् । */
object SalakaStriyamNityamSutra : LinganushasanaSutra(
    number = "34",
    text = "शलाका स्त्रियां नित्यम्",
    hindiExplanation = "शलाका शब्द नित्य स्त्रीलिङ्ग होता है।",
    section = LinganushasanaSection.STRILINGA,
    targetLingas = setOf(Linga.STRI),
    priority = 50,
) {
    private val members = setOf("शलाका")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

