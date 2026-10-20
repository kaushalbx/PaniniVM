package dev.panini.linganushasanam.adhyaya3
import dev.panini.core.Linga
import dev.panini.linganushasanam.*
/** Liṅgānuśāsanam 191: सर्वादीनि सर्वनामानि । */
object SarvadiniSarvanamaniSutra : LinganushasanaSutra(
    "191", "सर्वादीनि सर्वनामानि ।", "सर्वादि सर्वनाम विशेष्य के अनुसार तीनों लिङ्गों में होते हैं।", LinganushasanaSection.AVISISHTALINGA, Linga.PUMS, 40,
) { override val targetLingas = Linga.entries.toSet(); override fun matches(context: LingaRuleContext) = context.isPronoun && context.governingLinga != null }
