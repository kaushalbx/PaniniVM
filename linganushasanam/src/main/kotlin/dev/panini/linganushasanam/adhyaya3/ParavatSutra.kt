package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.*
import dev.panini.linganushasanam.*

/** Liṅgānuśāsanam 131: परवत्. */
object ParavatSutra : LinganushasanaSutra(
    number = "131", text = "परवत् ।", hindiExplanation = "निर्दिष्ट समास उत्तरपद के लिङ्ग के समान होता है।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.PUMS, priority = 40,
) {
    override val targetLingas = Linga.entries.toSet()
    private val governedTypes = setOf(SamasaType.TATPURUSA, SamasaType.NAN_TATPURUSA, SamasaType.UPAPADA_TATPURUSA, SamasaType.DVANDVA)
    override fun matches(context: LingaRuleContext): Boolean = context.samasaType in governedTypes && context.governingLinga != null
}
