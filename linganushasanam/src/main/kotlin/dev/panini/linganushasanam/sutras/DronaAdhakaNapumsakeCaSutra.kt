package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 104: द्रोणाढकौ नपुंसके च । */
object DronaAdhakaNapumsakeCaSutra : LinganushasanaSutra(
    number = "104", text = "द्रौणाढकौ नपुंसके च", hindiExplanation = "द्रोण और आढक पुंलिङ्ग तथा नपुंसकलिङ्ग दोनों में प्रयुक्त होते हैं।",
    section = LinganushasanaSection.PUMLINGA, targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA), priority = 50,
) {
    private val members = setOf("द्रोण", "आढक")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

