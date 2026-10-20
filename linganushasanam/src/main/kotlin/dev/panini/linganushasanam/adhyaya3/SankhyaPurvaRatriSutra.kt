package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.Linga
import dev.panini.linganushasanam.*

/** Liṅgānuśāsanam 133: सङ्ख्यापूर्वा रात्रिः. */
object SankhyaPurvaRatriSutra : LinganushasanaSutra(
    number = "133", text = "सङ्ख्यापूर्वा रात्रिः ।", hindiExplanation = "संख्यापूर्वक रात्रि-समास पुंलिङ्ग होता है।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.PUMS, priority = 10,
) { override fun matches(context: LingaRuleContext): Boolean = context.numericalValue != null && context.pratipadika.endsWithVarnas("रात्रि") }
