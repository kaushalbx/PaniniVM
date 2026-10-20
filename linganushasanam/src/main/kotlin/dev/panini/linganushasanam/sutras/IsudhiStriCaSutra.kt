package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 42: इषुधिः स्त्री च । */
object IsudhiStriCaSutra : LinganushasanaSutra(
    number = "42",
    text = "इषुधि स्त्री च",
    hindiExplanation = "इषुधि पुंलिङ्ग तथा स्त्रीलिङ्ग दोनों में प्रयुक्त होता है।",
    section = LinganushasanaSection.PUMLINGA,
    targetLingas = setOf(Linga.PUMS, Linga.STRI),
    priority = 50,
) {
    private val members = setOf("इषुधि")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

