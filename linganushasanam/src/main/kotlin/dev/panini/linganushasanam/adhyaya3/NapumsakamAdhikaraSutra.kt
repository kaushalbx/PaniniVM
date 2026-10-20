package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.Linga
import dev.panini.linganushasanam.*

/** Liṅgānuśāsanam 118: नपुंसकम् — begins the neuter section. */
object NapumsakamAdhikaraSutra : LinganushasanaSutra(
    number = "118", text = "नपुंसकम् ।", hindiExplanation = "यह नपुंसकलिङ्ग का अधिकारसूत्र है।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.NAPUMSAKA,
    kind = LinganushasanaRuleKind.ADHIKARA,
) { override fun matches(context: LingaRuleContext): Boolean = false }
