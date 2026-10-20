package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 79: शूर्पकुतपकुणपद्वीपविटपानि नपुंसके च । */
object SurpaKutapaKunapaDvipaVitapaSutra : LinganushasanaSutra(
    number = "79",
    text = "शूर्पकुतपकुणपद्वीपविटपानि नपुंसके च ।",
    hindiExplanation = "शूर्प, कुतप, कुणप, द्वीप और विटप पुंलिङ्ग तथा नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLinga = Linga.PUMS,
    priority = 50,
) {
    override val targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA)
    private val members = setOf("शूर्प", "कुतप", "कुणप", "द्वीप", "विटप")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

