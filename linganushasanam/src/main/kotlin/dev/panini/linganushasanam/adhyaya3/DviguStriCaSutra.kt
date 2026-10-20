package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.*
import dev.panini.linganushasanam.*

/** Liṅgānuśāsanam 135: द्विगुः स्त्रियाञ्च. */
object DviguStriCaSutra : LinganushasanaSutra(
    number = "135", text = "द्विगुः स्त्रियाञ्च ।", hindiExplanation = "द्विगु समास नपुंसकलिङ्ग तथा स्त्रीलिङ्ग होता है।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.NAPUMSAKA, priority = 40,
) { override val targetLingas = setOf(Linga.STRI, Linga.NAPUMSAKA); override fun matches(context: LingaRuleContext): Boolean = context.samasaType == SamasaType.DVIGU }
