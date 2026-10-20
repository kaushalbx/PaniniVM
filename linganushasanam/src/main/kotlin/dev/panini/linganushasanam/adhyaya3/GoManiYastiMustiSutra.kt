package dev.panini.linganushasanam.adhyaya3
import dev.panini.core.Linga
import dev.panini.linganushasanam.*
/** Liṅgānuśāsanam 174: गोमणियष्टिमुष्टिपाटलिबस्तिशाल्मलित्रुटिमसिमरीचयः । */
object GoManiYastiMustiSutra : LinganushasanaSutra(
    "174", "गोमणियष्टिमुष्टिपाटलिबस्तिशाल्मलित्रुटिमसिमरीचयः ।", "सूत्रोक्त शब्द स्त्रीलिङ्ग और पुंलिङ्ग दोनों में होते हैं।",
    LinganushasanaSection.STRIPUMSALINGA, Linga.STRI, 50,
) { override val targetLingas = setOf(Linga.STRI, Linga.PUMS); private val members = setOf("गो", "मणि", "यष्टि", "मुष्टि", "पाटलि", "बस्ति", "शाल्मलि", "त्रुटि", "मसि", "मरीचि"); override fun matches(context: LingaRuleContext) = context.pratipadika.phonologicallyIn(members) }
