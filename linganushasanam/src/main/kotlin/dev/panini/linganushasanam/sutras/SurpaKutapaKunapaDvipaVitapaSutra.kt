package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 79: शूर्पकुतपकुणपद्वीपविटपानि नपुंसके च । */
object SurpaKutapaKunapaDvipaVitapaSutra : LinganushasanaSutra(
    number = "79",
    text = "शूर्पकुतपकुणपद्वीपविटपानि नपुंसके च",
    hindiExplanation = "शूर्प, कुतप, कुणप, द्वीप और विटप पुंलिङ्ग तथा नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA),
    priority = 50,
) {
    private val members = setOf("शूर्प", "कुतप", "कुणप", "द्वीप", "विटप")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

