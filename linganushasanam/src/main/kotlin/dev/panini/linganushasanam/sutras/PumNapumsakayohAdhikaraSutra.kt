package dev.panini.linganushasanam.sutras
import dev.panini.core.Linga
import dev.panini.linganushasanam.*
/** Liṅgānuśāsanam 176: पुंनपुंसकयोः । */
object PumNapumsakayohAdhikaraSutra : LinganushasanaSutra(
    "176", "पुंनपुंसकयोः", "यह पुंलिङ्ग और नपुंसकलिङ्ग का अधिकारसूत्र है।",
    LinganushasanaSection.PUMNAPUMSAKALINGA, targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA), kind = LinganushasanaRuleKind.ADHIKARA,
) { override fun matches(context: LingaRuleContext) = false }
