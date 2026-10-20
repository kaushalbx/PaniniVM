package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 160: आहवसङ्ग्रामौ पुंसि । */
object AhavaSangramaPumsiSutra : LinganushasanaSutra(
    number = "160", text = "आहवसङ्ग्रामौ पुंसि", hindiExplanation = "आहव और सङ्ग्राम पुंलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLingas = setOf(Linga.PUMS), priority = 50,
) {
    private val members = setOf("आहव", "सङ्ग्राम")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

