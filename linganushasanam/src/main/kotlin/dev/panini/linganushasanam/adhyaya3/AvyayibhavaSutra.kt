package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.Linga
import dev.panini.core.SamasaType
import dev.panini.linganushasanam.*

/** Liṅgānuśāsanam 124: अव्ययीभावः. */
object AvyayibhavaSutra : LinganushasanaSutra(
    number = "124", text = "अव्ययीभावः ।", hindiExplanation = "अव्ययीभाव समास नपुंसकलिङ्ग होता है।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.NAPUMSAKA, priority = 40,
) { override fun matches(context: LingaRuleContext): Boolean = context.samasaType == SamasaType.AVYAYIBHAVA }
