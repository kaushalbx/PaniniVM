package dev.panini.linganushasanam.adhyaya5

import dev.panini.core.Linga
import dev.panini.core.SamasaType
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

/**
 * Liṅgānuśāsanam 5.3: अनेकमन्यपदार्थे (Aṣṭādhyāyī 2.2.24).
 * Bahuvrīhi compounds take Anyapadārtha gender (default Masculine).
 */
object AnehamAnyapadartheSutra : LinganushasanaSutra(
    number = "5.3",
    text = "अनेकमन्यपदार्थे",
    hindiExplanation = "बहुव्रीहि समास अन्यपदार्थ के लिङ्ग को धारण करता है।",
    section = LinganushasanaSection.SAMASALINGA,
    targetLinga = Linga.PUMS,
    priority = 40,
) {
    override fun matches(context: LingaRuleContext): Boolean {
        return context.samasaType == SamasaType.BAHUVRIHI
    }
}
