package dev.panini.linganushasanam.sutras
import dev.panini.core.Linga
import dev.panini.linganushasanam.*
/** Liṅgānuśāsanam 172: गोमणियष्टिमुष्टिपाटलिवस्तिशाल्मलित्रुटिमसिमरीचयः । */
object GoManiYastiMustiSutra : LinganushasanaSutra(
    "172", "गोमणियष्टिमुष्टिपाटलिवस्तिशाल्मलित्रुटिमसिमरीचयः", "सूत्रोक्त शब्द स्त्रीलिङ्ग और पुंलिङ्ग दोनों में होते हैं।",
    LinganushasanaSection.STRIPUMSALINGA, targetLingas = setOf(Linga.STRI, Linga.PUMS), 50,
) { private val members = setOf("गो", "मणि", "यष्टि", "मुष्टि", "पाटलि", "वस्ति", "शाल्मलि", "त्रुटि", "मसि", "मरीचि"); override fun matches(context: LingaRuleContext) = context.pratipadika.phonologicallyIn(members) }
