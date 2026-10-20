package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 59: सक्तुर्नपुंसके च । */
object SaktuNapumsakeCaSutra : LinganushasanaSutra(
    number = "59",
    text = "सक्तुर्नपुंसके च",
    hindiExplanation = "सक्तु पुंलिङ्ग तथा नपुंसकलिङ्ग दोनों में प्रयुक्त होता है।",
    section = LinganushasanaSection.PUMLINGA,
    targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA),
    priority = 50,
) {
    private val members = setOf("सक्तु")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

