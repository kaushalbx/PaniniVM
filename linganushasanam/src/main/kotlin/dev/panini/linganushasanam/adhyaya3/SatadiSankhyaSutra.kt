package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

/** Liṅgānuśāsanam 146: शतादिः सङ्ख्या । */
object SatadiSankhyaSutra : LinganushasanaSutra(
    number = "146", text = "शतादिः सङ्ख्या ।", hindiExplanation = "शत से आरम्भ होने वाली संख्याएँ नपुंसकलिङ्ग होती हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.NAPUMSAKA, priority = 40,
) {
    override fun matches(context: LingaRuleContext): Boolean = context.numericalValue?.let { it >= 100 } == true
}
