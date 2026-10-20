package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 73: तीर्थप्रोथयूथगूथानि नपुंसके च । */
object TirthaProthaYuthaGuthaSutra : LinganushasanaSutra(
    number = "73",
    text = "तीर्थप्रोथयूथगूथानि नपुंसके च ।",
    hindiExplanation = "तीर्थ, प्रोथ, यूथ और गूथ पुंलिङ्ग तथा नपुंसकलिङ्ग दोनों में प्रयुक्त होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLinga = Linga.PUMS,
    priority = 50,
) {
    override val targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA)
    private val members = setOf("तीर्थ", "प्रोथ", "यूथ", "गूथ")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

