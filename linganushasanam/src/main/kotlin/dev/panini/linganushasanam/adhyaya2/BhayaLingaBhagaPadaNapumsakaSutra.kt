package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 38: भयलिङ्गभगपदानि नपुंसके । */
object BhayaLingaBhagaPadaNapumsakaSutra : LinganushasanaSutra(
    number = "38",
    text = "भयलिङ्गभगपदानि नपुंसके ।",
    hindiExplanation = "भय, लिङ्ग, भग और पद नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLinga = Linga.NAPUMSAKA,
    priority = 50,
) {
    private val members = setOf("भय", "लिङ्ग", "भग", "पद")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

