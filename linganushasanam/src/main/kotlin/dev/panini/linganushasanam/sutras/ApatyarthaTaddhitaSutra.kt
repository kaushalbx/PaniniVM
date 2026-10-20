package dev.panini.linganushasanam.sutras
import dev.panini.core.Linga
import dev.panini.core.LingaSemanticClass
import dev.panini.linganushasanam.*
/** Liṅgānuśāsanam 175: अपत्यार्थस्तद्धिते । */
object ApatyarthaTaddhitaSutra : LinganushasanaSutra(
    "175", "अपत्यार्थतद्धिते", "अपत्यार्थक तद्धितान्त शब्द स्त्रीलिङ्ग और पुंलिङ्ग दोनों में होते हैं।",
    LinganushasanaSection.STRIPUMSALINGA, targetLingas = setOf(Linga.STRI, Linga.PUMS), 40,
) { override fun matches(context: LingaRuleContext) = LingaSemanticClass.DESCENDANT in context.semanticClasses }
