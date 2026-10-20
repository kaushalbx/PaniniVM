package dev.panini.linganushasanam.adhyaya3
import dev.panini.core.Linga
import dev.panini.linganushasanam.*
/** Liṅgānuśāsanam 185: अविशिष्टलिङ्गम् । */
object AvisistaLingamAdhikaraSutra : LinganushasanaSutra(
    "185", "अविशिष्टलिङ्गम् ।", "यह तीनों लिङ्गों में प्रयोग का अधिकारसूत्र है।", LinganushasanaSection.AVISISHTALINGA, Linga.PUMS, kind = LinganushasanaRuleKind.ADHIKARA,
) { override val targetLingas = Linga.entries.toSet(); override fun matches(context: LingaRuleContext) = false }
