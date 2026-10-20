package dev.panini.linganushasanam.adhyaya3
import dev.panini.core.Linga
import dev.panini.linganushasanam.*
/** Liṅgānuśāsanam 178: पुंनपुंसकयोः । */
object PumNapumsakayohAdhikaraSutra : LinganushasanaSutra(
    "178", "पुंनपुंसकयोः ।", "यह पुंलिङ्ग और नपुंसकलिङ्ग का अधिकारसूत्र है।",
    LinganushasanaSection.PUMNAPUMSAKALINGA, Linga.PUMS, kind = LinganushasanaRuleKind.ADHIKARA,
) { override val targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA); override fun matches(context: LingaRuleContext) = false }
