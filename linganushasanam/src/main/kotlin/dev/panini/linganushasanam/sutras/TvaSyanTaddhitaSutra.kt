package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.*

/** Liṅgānuśāsanam 121: त्वष्यञौ तद्धितौ. */
object TvaSyanTaddhitaSutra : LinganushasanaSutra(
    number = "121", text = "त्वष्यञौ तद्धितौ", hindiExplanation = "त्व अथवा ष्यञ् तद्धितप्रत्ययान्त शब्द नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLingas = setOf(Linga.NAPUMSAKA), priority = 40,
) { override fun matches(context: LingaRuleContext): Boolean = context.pratyaya in setOf("त्व", "ष्यञ्") }
