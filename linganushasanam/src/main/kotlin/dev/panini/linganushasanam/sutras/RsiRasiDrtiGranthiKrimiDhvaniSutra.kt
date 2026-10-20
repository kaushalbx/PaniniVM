package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

object RsiRasiDrtiGranthiKrimiDhvaniSutra : LinganushasanaSutra(
    number = "109",
    text = "ऋषिराशिदृतिग्रन्थिक्रिमिध्वनिबलिकौलिमौलिरविकविकपिमुनयः",
    hindiExplanation = "सूत्र में निर्दिष्ट शब्द पुंलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLingas = setOf(Linga.PUMS),
    priority = 50,
) {
    private val members = setOf("ऋषि", "राशि", "दृति", "ग्रन्थि", "क्रिमि", "ध्वनि", "बलि", "कौलि", "मौलि", "रवि", "कवि", "कपि", "मुनि")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}
