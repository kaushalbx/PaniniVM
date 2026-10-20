package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 155: अप्सराः स्त्रियाम् । */
object ApsarasStriyamSutra : LinganushasanaSutra(
    number = "155", text = "अप्सराः स्त्रियाम् ।", hindiExplanation = "अप्सरस् स्त्रीलिङ्ग होता है।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.STRI, priority = 50,
) {
    private val members = setOf("अप्सरस्")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

