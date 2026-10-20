package dev.panini.linganushasanam.adhyaya3
import dev.panini.core.Linga
import dev.panini.linganushasanam.*
/** Liṅgānuśāsanam 184: गृहमेहदेहपट्टपटहाष्टापदार्बुदककुदाश्च । */
object GrhaMehaDehaPattaSutra : LinganushasanaSutra(
    "184", "गृहमेहदेहपट्टपटहाष्टापदार्बुदककुदाश्च ।", "सूत्रोक्त शब्द पुंलिङ्ग और नपुंसकलिङ्ग दोनों में होते हैं।", LinganushasanaSection.PUMNAPUMSAKALINGA, Linga.PUMS, 50,
) { override val targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA); private val members = setOf("गृह", "मेह", "देह", "पट्ट", "पटह", "अष्टापद", "अर्बुद", "ककुद"); override fun matches(context: LingaRuleContext) = context.pratipadika.phonologicallyIn(members) }
