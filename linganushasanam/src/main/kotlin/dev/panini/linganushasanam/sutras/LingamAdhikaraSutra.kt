package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaRuleKind
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

/** Liṅgānuśāsanam 1: लिङ्गम् — opens the gender-governance domain. */
object LingamAdhikaraSutra : LinganushasanaSutra(
    number = "1",
    text = "लिङ्गम्",
    hindiExplanation = "यह सम्पूर्ण लिङ्गानुशासन का अधिकारसूत्र है।",
    section = LinganushasanaSection.STRILINGA,
    targetLingas = setOf(Linga.STRI),
    kind = LinganushasanaRuleKind.ADHIKARA,
) {
    override fun matches(context: LingaRuleContext): Boolean = false
}
