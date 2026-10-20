package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.Linga
import dev.panini.core.LingaSemanticClass
import dev.panini.linganushasanam.*

/** Liṅgānuśāsanam 119: भावे ल्युडन्तः. */
object BhaveLyudantaSutra : LinganushasanaSutra(
    number = "119", text = "भावे ल्युडन्तः ।", hindiExplanation = "भावार्थक ल्युट्-प्रत्ययान्त शब्द नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.NAPUMSAKA, priority = 40,
) { override fun matches(context: LingaRuleContext): Boolean = context.pratyaya == "ल्युट्" && LingaSemanticClass.BHAVA in context.semanticClasses }
