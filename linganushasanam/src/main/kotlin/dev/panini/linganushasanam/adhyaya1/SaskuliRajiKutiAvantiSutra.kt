package dev.panini.linganushasanam.adhyaya1

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 26: शष्कुलिराजिकुट्यवन्तिवर्त्तिभ्रुकुटित्रुटिबलिपङ्क्तयः । */
object SaskuliRajiKutiAvantiSutra : LinganushasanaSutra(
    number = "26",
    text = "शष्कुलिराजिकुट्यवन्तिवर्त्तिभ्रुकुटित्रुटिबलिपङ्क्तयः ।",
    hindiExplanation = "सूत्र में पठित शष्कुलि आदि शब्द स्त्रीलिङ्ग होते हैं।",
    section = LinganushasanaSection.STRILINGA,
    targetLinga = Linga.STRI,
    priority = 50,
) {
    private val members = setOf("शष्कुलि", "राजि", "कुटि", "अवन्ति", "वर्ति", "भ्रुकुटि", "त्रुटि", "बलि", "पङ्क्ति")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

