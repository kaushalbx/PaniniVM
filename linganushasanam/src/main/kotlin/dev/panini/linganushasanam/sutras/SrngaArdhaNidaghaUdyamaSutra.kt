package dev.panini.linganushasanam.sutras
import dev.panini.core.Linga
import dev.panini.linganushasanam.*
/** Liṅgānuśāsanam 178: शृङ्गार्धनिदाघोद्यमशल्यदृढाः । */
object SrngaArdhaNidaghaUdyamaSutra : LinganushasanaSutra(
    "178", "शृङ्गार्घनिदाघोद्यमशल्यदृढाः", "सूत्रोक्त शब्द पुंलिङ्ग और नपुंसकलिङ्ग दोनों में होते हैं।", LinganushasanaSection.PUMNAPUMSAKALINGA, targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA), 50,
) { private val members = setOf("शृङ्ग", "अर्घ", "निदाघ", "उद्यम", "शल्य", "दृढ"); override fun matches(context: LingaRuleContext) = context.pratipadika.phonologicallyIn(members) }
