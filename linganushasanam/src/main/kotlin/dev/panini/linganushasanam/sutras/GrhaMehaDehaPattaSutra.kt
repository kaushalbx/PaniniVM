package dev.panini.linganushasanam.sutras
import dev.panini.core.Linga
import dev.panini.linganushasanam.*
/** Liṅgānuśāsanam 182: गृहमेहदेहपट्टपटहाष्टापदार्बुदककुदाश्च । */
object GrhaMehaDehaPattaSutra : LinganushasanaSutra(
    "182", "गृहमेहदेहपट्टपटहाष्टापदाम्बुदककुदाश्च ।", "सूत्रोक्त शब्द पुंलिङ्ग और नपुंसकलिङ्ग दोनों में होते हैं।", LinganushasanaSection.PUMNAPUMSAKALINGA, targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA), 50,
) { private val members = setOf("गृह", "मेह", "देह", "पट्ट", "पटह", "अष्टापद", "अम्बुद", "ककुद"); override fun matches(context: LingaRuleContext) = context.pratipadika.phonologicallyIn(members) }
