package dev.panini.linganushasanam.adhyaya1

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

/** Liṅgānuśāsanam 13: विंशत्यादिरानवतेः. */
object VimsatyadiAnavatiSutra : LinganushasanaSutra(
    number = "13",
    text = "विंशत्यादिरानवतेः ।",
    hindiExplanation = "विंशति से नवति के पहले तक की दशकीय संख्याएँ स्त्रीलिङ्ग होती हैं।",
    section = LinganushasanaSection.STRILINGA,
    targetLinga = Linga.STRI,
    priority = 40,
) {
    override fun matches(context: LingaRuleContext): Boolean = context.numericalValue in 20L..89L
}
