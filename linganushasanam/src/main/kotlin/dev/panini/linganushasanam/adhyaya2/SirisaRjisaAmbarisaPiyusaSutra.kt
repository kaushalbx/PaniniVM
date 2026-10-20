package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 94: शिरीषर्जीषाम्बरीषपीयूषपुरीषकिल्विषकल्मषाणि नपुंसके । (ऋजीष स्थाने जोष इति केचित्) */
object SirisaRjisaAmbarisaPiyusaSutra : LinganushasanaSutra(
    number = "94",
    text = "शिरीषर्जीषाम्बरीषपीयूषपुरीषकिल्विषकल्मषाणि नपुंसके । (ऋजीष स्थाने जोष इति केचित्)",
    hindiExplanation = "सूत्र में पठित शिरीष आदि शब्द तथा जोष पाठान्तर नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLinga = Linga.NAPUMSAKA,
    priority = 50,
) {
    private val members = setOf("शिरीष", "ऋजीष", "जोष", "अम्बरीष", "पीयूष", "पुरीष", "किल्विष", "कल्मष")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

