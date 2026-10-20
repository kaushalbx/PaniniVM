package dev.panini.linganushasanam.adhyaya1

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

/** Liṅgānuśāsanam 11: ऊङ्ङ्याबन्तश्च. */
object UngNgyabantaSutra : LinganushasanaSutra(
    number = "11",
    text = "ऊङ्ङ्याबन्तश्च ।",
    hindiExplanation = "ऊङ् तथा ङ्याप्-वर्ग के स्त्रीप्रत्ययान्त शब्द स्त्रीलिङ्ग होते हैं।",
    section = LinganushasanaSection.STRILINGA,
    targetLinga = Linga.STRI,
    priority = 40,
) {
    private val feminineAffixes = setOf("ऊङ्", "ङीप्", "ङीष्", "टाप्", "डाप्", "चाप्")

    override fun matches(context: LingaRuleContext): Boolean = context.pratyaya in feminineAffixes
}
