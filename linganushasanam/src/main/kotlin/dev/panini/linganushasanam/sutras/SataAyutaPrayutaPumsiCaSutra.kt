package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 145: शतायुतप्रयुताः पुंसि च । */
object SataAyutaPrayutaPumsiCaSutra : LinganushasanaSutra(
    number = "145", text = "शतायुतप्रयुताः पुंसि च", hindiExplanation = "शत, अयुत और प्रयुत पुंलिङ्ग तथा नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA), priority = 50,
) {
    private val members = setOf("शत", "अयुत", "प्रयुत")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

