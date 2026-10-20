package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 94: शिरीषर्जीषाम्बरीषपीयूषपुरीषकिल्विषकल्मषाणि नपुंसके । (ऋजीष स्थाने जोष इति केचित्) */
object SirisaRjisaAmbarisaPiyusaSutra : LinganushasanaSutra(
    number = "94",
    text = "शिरीषर्जीषाम्बरीषपीयूषपुरीषकिल्बिषकल्माषाणि नपुंसके",
    hindiExplanation = "सूत्र में पठित शिरीष आदि शब्द तथा जोष पाठान्तर नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLingas = setOf(Linga.NAPUMSAKA),
    priority = 50,
) {
    private val members = setOf("शिरीष", "ऋजीष", "अम्बरीष", "पीयूष", "पुरीष", "किल्विष", "कल्माष")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

