package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 21: स्थूणोर्णे नपुंसके च । */
object SthunaUrnaNapumsakeCaSutra : LinganushasanaSutra(
    number = "21",
    text = "स्थूणोर्णे नपुंसके च",
    hindiExplanation = "स्थूणा और ऊर्णा स्त्रीलिङ्ग तथा नपुंसकलिङ्ग दोनों में प्रयुक्त होते हैं।",
    section = LinganushasanaSection.STRILINGA,
    targetLingas = setOf(Linga.STRI, Linga.NAPUMSAKA),
    priority = 50,
) {
    private val members = setOf("स्थूणा", "ऊर्णा")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

