package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 78: पापरूपोडुपतल्पशिल्पपुष्पशष्पसमीपान्तरीपाणि नपुंसके । (ताप इत्यपि केचित्) */
object PapaRupaUdupaTalpaSilpaSutra : LinganushasanaSutra(
    number = "78",
    text = "पापरूपोडुपतल्पशिल्पपुष्पशष्पसमीपान्तरीपाणि नपुंसके । (ताप इत्यपि केचित्)",
    hindiExplanation = "सूत्र में पठित पाप आदि शब्द तथा ताप पाठान्तर नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLinga = Linga.NAPUMSAKA,
    priority = 50,
) {
    private val members = setOf("पाप", "रूप", "उडुप", "तल्प", "शिल्प", "पुष्प", "शष्प", "समीप", "अन्तरीप", "ताप")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

