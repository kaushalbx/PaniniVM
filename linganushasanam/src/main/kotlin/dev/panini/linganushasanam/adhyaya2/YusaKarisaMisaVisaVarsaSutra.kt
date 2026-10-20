package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 95: यूषकरीषमिषविषवर्षाणि नपुंसके च । */
object YusaKarisaMisaVisaVarsaSutra : LinganushasanaSutra(
    number = "95",
    text = "यूषकरीषमिषविषवर्षाणि नपुंसके च ।",
    hindiExplanation = "यूष, करीष, मिष, विष और वर्ष पुंलिङ्ग तथा नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLinga = Linga.PUMS,
    priority = 50,
) {
    override val targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA)
    private val members = setOf("यूष", "करीष", "मिष", "विष", "वर्ष")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

