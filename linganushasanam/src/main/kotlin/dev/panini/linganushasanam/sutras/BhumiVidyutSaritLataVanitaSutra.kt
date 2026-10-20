package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 18: भूमिविद्युत्सरिल्लतावनिताभिधानानि । */
object BhumiVidyutSaritLataVanitaSutra : LinganushasanaSutra(
    number = "18",
    text = "भूमिविद्युत्सरिल्लतावनिताभिधानानि",
    hindiExplanation = "भूमि, विद्युत्, सरित्, लता और वनिता के अभिधान स्त्रीलिङ्ग होते हैं।",
    section = LinganushasanaSection.STRILINGA,
    targetLingas = setOf(Linga.STRI),
    priority = 50,
) {
    private val members = setOf("भूमि", "विद्युत्", "सरित्", "लता", "वनिता")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

