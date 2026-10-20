package dev.panini.linganushasanam.sutras

import dev.panini.core.*
import dev.panini.linganushasanam.*

/** Liṅgānuśāsanam 130: परवत्. */
object ParavatSutra : LinganushasanaSutra(
    number = "130", text = "परवत्", hindiExplanation = "निर्दिष्ट समास उत्तरपद के लिङ्ग के समान होता है।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLingas = Linga.entries.toSet(), priority = 40,
) {
    private val governedTypes = setOf(SamasaType.TATPURUSA, SamasaType.NAN_TATPURUSA, SamasaType.UPAPADA_TATPURUSA, SamasaType.DVANDVA)
    override fun matches(context: LingaRuleContext): Boolean = context.samasaType in governedTypes && context.governingLinga != null
}
