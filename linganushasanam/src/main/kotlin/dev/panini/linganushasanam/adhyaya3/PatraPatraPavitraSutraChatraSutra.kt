package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 159: पत्रपात्रपवित्रसूत्रच्छत्राः पुंसि च । */
object PatraPatraPavitraSutraChatraSutra : LinganushasanaSutra(
    number = "159", text = "पत्रपात्रपवित्रसूत्रच्छत्राः पुंसि च ।", hindiExplanation = "पत्र, पात्र, पवित्र, सूत्र और छत्र पुंलिङ्ग तथा नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.NAPUMSAKA, priority = 50,
) {
    override val targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA)
    private val members = setOf("पत्र", "पात्र", "पवित्र", "सूत्र", "छत्र")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

