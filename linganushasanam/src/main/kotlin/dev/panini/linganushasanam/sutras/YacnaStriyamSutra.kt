package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 40: याच्ञा स्त्रियाम् । */
object YacnaStriyamSutra : LinganushasanaSutra(
    number = "40",
    text = "याच्ञा स्त्रियाम्",
    hindiExplanation = "याच्ञा शब्द स्त्रीलिङ्ग होता है।",
    section = LinganushasanaSection.PUMLINGA,
    targetLingas = setOf(Linga.STRI),
    priority = 50,
) {
    private val members = setOf("याच्ञा")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

