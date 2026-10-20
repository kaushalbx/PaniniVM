package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 27: प्रतिपदापद्विपत्सम्पच्छरत्संसत्परिषदुषःसंवित्क्षुत्पुन्मुत्समिधः । */
object PratipadApadVipadSampadSutra : LinganushasanaSutra(
    number = "27",
    text = "प्रतिपदापद्विपत्सम्पच्छरत्संसत्परिषदुषः संवित्क्षुत्पुन्मुत्समिधः ।",
    hindiExplanation = "सूत्र में पठित प्रतिपद् आदि शब्द स्त्रीलिङ्ग होते हैं।",
    section = LinganushasanaSection.STRILINGA,
    targetLingas = setOf(Linga.STRI),
    priority = 50,
) {
    private val members = setOf("प्रतिपद्", "आपद्", "विपद्", "सम्पद्", "शरद्", "संसद्", "परिषद्", "उषस्", "संविद्", "क्षुध्", "पुम्", "मुत्", "समिध्")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

