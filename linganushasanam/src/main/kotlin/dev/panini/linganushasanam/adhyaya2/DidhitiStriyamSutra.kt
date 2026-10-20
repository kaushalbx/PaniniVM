package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 101: दीधितिः स्त्रियाम् । */
object DidhitiStriyamSutra : LinganushasanaSutra(
    number = "101", text = "दीधितिः स्त्रियाम् ।", hindiExplanation = "दीधिति शब्द स्त्रीलिङ्ग होता है।",
    section = LinganushasanaSection.PUMLINGA, targetLinga = Linga.STRI, priority = 50,
) {
    private val members = setOf("दीधिति")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

