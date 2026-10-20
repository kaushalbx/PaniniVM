package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 157: पत्रपात्रपवित्रसूत्रच्छत्राः पुंसि च । */
object PatraPatraPavitraSutraChatraSutra : LinganushasanaSutra(
    number = "157", text = "पत्रपात्रपवित्रसूत्रच्छत्राः पुंसि च", hindiExplanation = "पत्र, पात्र, पवित्र, सूत्र और छत्र पुंलिङ्ग तथा नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA), priority = 50,
) {
    private val members = setOf("पत्र", "पात्र", "पवित्र", "सूत्र", "छत्र")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

