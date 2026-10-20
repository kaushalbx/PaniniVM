package dev.panini.linganushasanam.adhyaya3
import dev.panini.core.Linga
import dev.panini.linganushasanam.*
/** Liṅgānuśāsanam 180: शृङ्गार्धनिदाघोद्यमशल्यदृढाः । */
object SrngaArdhaNidaghaUdyamaSutra : LinganushasanaSutra(
    "180", "श‍ृङ्गार्धनिदाघोद्यमशल्यदृढाः ।", "सूत्रोक्त शब्द पुंलिङ्ग और नपुंसकलिङ्ग दोनों में होते हैं।", LinganushasanaSection.PUMNAPUMSAKALINGA, Linga.PUMS, 50,
) { override val targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA); private val members = setOf("शृङ्ग", "अर्ध", "निदाघ", "उद्यम", "शल्य", "दृढ"); override fun matches(context: LingaRuleContext) = context.pratipadika.phonologicallyIn(members) }
