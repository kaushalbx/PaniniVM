package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 66: कुटकूटकपटकपाटकर्पटनटनिकटकीटकटानि नपुंसके च । */
object KutaKutaKapataKapataSutra : LinganushasanaSutra(
    number = "66",
    text = "कुटकूटकटपटकवाटकर्पटनटनिकटकीटकटानि नपुंसके च",
    hindiExplanation = "सूत्र में पठित कुट आदि शब्द पुंलिङ्ग तथा नपुंसकलिङ्ग दोनों में प्रयुक्त होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA),
    priority = 50,
) {
    private val members = setOf("कुट", "कूट", "कट", "पटक", "वाटक", "कर्पट", "नट", "निकट", "कीट")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

