package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 140: अटवी स्त्रियाम् । */
object AtaviStriyamSutra : LinganushasanaSutra(
    number = "140", text = "अटवी स्त्रियाम्", hindiExplanation = "अटवी स्त्रीलिङ्ग होती है।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLingas = setOf(Linga.STRI), priority = 50,
) {
    private val members = setOf("अटवी")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

