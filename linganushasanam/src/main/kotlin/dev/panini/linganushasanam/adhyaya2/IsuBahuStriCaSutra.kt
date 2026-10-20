package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 46: इषुबाहू स्त्रियाञ्च । */
object IsuBahuStriCaSutra : LinganushasanaSutra(
    number = "46",
    text = "इषुबाहू स्त्रियाञ्च ।",
    hindiExplanation = "इषु और बाहु पुंलिङ्ग तथा स्त्रीलिङ्ग दोनों में प्रयुक्त होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLinga = Linga.PUMS,
    priority = 50,
) {
    override val targetLingas = setOf(Linga.PUMS, Linga.STRI)
    private val members = setOf("इषु", "बाहु")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

