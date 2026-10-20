package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.Linga
import dev.panini.core.SamasaType
import dev.panini.core.Vacana
import dev.panini.linganushasanam.*

/** Liṅgānuśāsanam 125: द्वन्द्वैकत्वम्. */
object DvandvaEkatvaSutra : LinganushasanaSutra(
    number = "125", text = "द्वन्द्वैकत्वम् ।", hindiExplanation = "एकवचन में समाहार-द्वन्द्व नपुंसकलिङ्ग होता है।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.NAPUMSAKA, priority = 40,
) { override fun matches(context: LingaRuleContext): Boolean = context.samasaType == SamasaType.DVANDVA && context.vacana == Vacana.EKAVACANA }
