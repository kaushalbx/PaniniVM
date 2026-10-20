package dev.panini.linganushasanam.sutras
import dev.panini.core.Linga
import dev.panini.linganushasanam.*
/** Liṅgānuśāsanam 171: स्त्रीपुंसयोः । */
object StriPumsayohAdhikaraSutra : LinganushasanaSutra(
    "171", "स्त्रीपुंसयोः", "यह स्त्रीलिङ्ग और पुंलिङ्ग का अधिकारसूत्र है।",
    LinganushasanaSection.STRIPUMSALINGA, targetLingas = setOf(Linga.STRI, Linga.PUMS), kind = LinganushasanaRuleKind.ADHIKARA,
) { override fun matches(context: LingaRuleContext) = false }
