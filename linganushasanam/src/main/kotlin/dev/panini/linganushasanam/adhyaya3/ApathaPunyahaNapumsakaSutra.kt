package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.Linga
import dev.panini.linganushasanam.*

/** Liṅgānuśāsanam 134: अपथपुण्याहे नपुंसके. */
object ApathaPunyahaNapumsakaSutra : LinganushasanaSutra(
    number = "134", text = "अपथपुण्याहे नपुंसके ।", hindiExplanation = "अपथ और पुण्याह नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.NAPUMSAKA, priority = 50,
) { override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(setOf("अपथ", "पुण्याह")) }
