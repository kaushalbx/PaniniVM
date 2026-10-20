package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 153: अप्सराः स्त्रियाम् । */
object ApsarasStriyamSutra : LinganushasanaSutra(
    number = "153", text = "अप्सराः स्त्रियाम्", hindiExplanation = "अप्सरस् स्त्रीलिङ्ग होता है।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLingas = setOf(Linga.STRI), priority = 50,
) {
    private val members = setOf("अप्सरस्")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

