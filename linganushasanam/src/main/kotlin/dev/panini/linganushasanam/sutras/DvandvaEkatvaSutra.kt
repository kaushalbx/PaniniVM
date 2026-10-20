package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.core.SamasaType
import dev.panini.core.Vacana
import dev.panini.linganushasanam.*

/** Liṅgānuśāsanam 124: द्वन्द्वैकत्वम्. */
object DvandvaEkatvaSutra : LinganushasanaSutra(
    number = "124", text = "द्वन्द्वैकत्वम्", hindiExplanation = "एकवचन में समाहार-द्वन्द्व नपुंसकलिङ्ग होता है।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLingas = setOf(Linga.NAPUMSAKA), priority = 40,
) { override fun matches(context: LingaRuleContext): Boolean = context.samasaType == SamasaType.DVANDVA && context.vacana == Vacana.EKAVACANA }
