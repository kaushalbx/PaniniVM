package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 82: जृम्भं नपुंसके च । */
object JrmbhaNapumsakeCaSutra : LinganushasanaSutra(
    number = "82",
    text = "जृम्भं नपुंसके च",
    hindiExplanation = "जृम्भ पुंलिङ्ग तथा नपुंसकलिङ्ग दोनों में प्रयुक्त होता है।",
    section = LinganushasanaSection.PUMLINGA,
    targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA),
    priority = 50,
) {
    private val members = setOf("जृम्भ")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

