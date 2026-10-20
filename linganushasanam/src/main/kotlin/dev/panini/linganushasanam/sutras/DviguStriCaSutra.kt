package dev.panini.linganushasanam.sutras

import dev.panini.core.*
import dev.panini.linganushasanam.*

/** Liṅgānuśāsanam 133: द्विगुः स्त्रियाञ्च. */
object DviguStriCaSutra : LinganushasanaSutra(
    number = "133", text = "द्विगुः स्त्रियां च", hindiExplanation = "द्विगु समास नपुंसकलिङ्ग तथा स्त्रीलिङ्ग होता है।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLingas = setOf(Linga.STRI, Linga.NAPUMSAKA), priority = 40,
) { override fun matches(context: LingaRuleContext): Boolean = context.samasaType == SamasaType.DVIGU }
