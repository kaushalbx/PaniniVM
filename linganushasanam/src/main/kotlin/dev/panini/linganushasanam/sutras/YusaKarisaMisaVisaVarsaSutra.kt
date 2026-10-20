package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 95: यूषकरीषमिषविषवर्षाणि नपुंसके च । */
object YusaKarisaMisaVisaVarsaSutra : LinganushasanaSutra(
    number = "95",
    text = "यूषकरीषमिषविषवर्षाणि नपुंसके च",
    hindiExplanation = "यूष, करीष, मिष, विष और वर्ष पुंलिङ्ग तथा नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA),
    priority = 50,
) {
    private val members = setOf("यूष", "करीष", "मिष", "विष", "वर्ष")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

