package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 146: लक्षा कोटिः स्त्रियाम् । */
object LaksaKotiStriyamSutra : LinganushasanaSutra(
    number = "146", text = "लक्षा कोटिः स्त्रियाम्", hindiExplanation = "लक्षा और कोटि स्त्रीलिङ्ग होती हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLingas = setOf(Linga.STRI), priority = 50,
) {
    private val members = setOf("लक्षा", "कोटि")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

