package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 159: पद्मकमलोत्पलानि पुंसि च । */
object PadmaKamalaUtpalaPumsiCaSutra : LinganushasanaSutra(
    number = "159", text = "पद्मकमलोत्पलानि पुंसि च", hindiExplanation = "पद्म, कमल और उत्पल पुंलिङ्ग तथा नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA), priority = 50,
) {
    private val members = setOf("पद्म", "कमल", "उत्पल")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

