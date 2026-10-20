package dev.panini.linganushasanam.adhyaya3
import dev.panini.core.Linga
import dev.panini.core.LingaSemanticClass
import dev.panini.linganushasanam.*
/** Liṅgānuśāsanam 177: अपत्यार्थस्तद्धिते । */
object ApatyarthaTaddhitaSutra : LinganushasanaSutra(
    "177", "अपत्यार्थस्तद्धिते ।", "अपत्यार्थक तद्धितान्त शब्द स्त्रीलिङ्ग और पुंलिङ्ग दोनों में होते हैं।",
    LinganushasanaSection.STRIPUMSALINGA, Linga.STRI, 40,
) { override val targetLingas = setOf(Linga.STRI, Linga.PUMS); override fun matches(context: LingaRuleContext) = LingaSemanticClass.DESCENDANT in context.semanticClasses }
