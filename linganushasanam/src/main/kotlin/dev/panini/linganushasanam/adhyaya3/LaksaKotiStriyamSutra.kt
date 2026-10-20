package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 148: लक्षाकोटी स्त्रियाम् । */
object LaksaKotiStriyamSutra : LinganushasanaSutra(
    number = "148", text = "लक्षाकोटी स्त्रियाम् ।", hindiExplanation = "लक्षा और कोटी स्त्रीलिङ्ग होती हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.STRI, priority = 50,
) {
    private val members = setOf("लक्षा", "कोटी")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

