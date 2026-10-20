package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 162: आहवसङ्ग्रामौ पुंसि । */
object AhavaSangramaPumsiSutra : LinganushasanaSutra(
    number = "162", text = "आहवसङ्ग्रामौ पुंसि ।", hindiExplanation = "आहव और सङ्ग्राम पुंलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.PUMS, priority = 50,
) {
    private val members = setOf("आहव", "सङ्ग्राम")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

