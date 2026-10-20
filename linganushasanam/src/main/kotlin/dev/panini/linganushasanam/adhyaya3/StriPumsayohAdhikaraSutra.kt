package dev.panini.linganushasanam.adhyaya3
import dev.panini.core.Linga
import dev.panini.linganushasanam.*
/** Liṅgānuśāsanam 173: स्त्रीपुंसयोः । */
object StriPumsayohAdhikaraSutra : LinganushasanaSutra(
    "173", "स्त्रीपुंसयोः ।", "यह स्त्रीलिङ्ग और पुंलिङ्ग का अधिकारसूत्र है।",
    LinganushasanaSection.STRIPUMSALINGA, Linga.STRI, kind = LinganushasanaRuleKind.ADHIKARA,
) { override val targetLingas = setOf(Linga.STRI, Linga.PUMS); override fun matches(context: LingaRuleContext) = false }
