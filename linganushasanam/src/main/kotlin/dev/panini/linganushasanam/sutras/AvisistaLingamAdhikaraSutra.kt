package dev.panini.linganushasanam.sutras
import dev.panini.core.Linga
import dev.panini.linganushasanam.*
/** Liṅgānuśāsanam 183: अविशिष्टलिङ्गम् । */
object AvisistaLingamAdhikaraSutra : LinganushasanaSutra(
    "183", "अविशिष्टलिङ्गम्", "यह तीनों लिङ्गों में प्रयोग का अधिकारसूत्र है।", LinganushasanaSection.AVISISHTALINGA, targetLingas = Linga.entries.toSet(), kind = LinganushasanaRuleKind.ADHIKARA,
) { override fun matches(context: LingaRuleContext) = false }
