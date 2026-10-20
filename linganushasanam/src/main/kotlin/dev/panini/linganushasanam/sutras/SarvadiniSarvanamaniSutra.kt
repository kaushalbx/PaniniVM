package dev.panini.linganushasanam.sutras
import dev.panini.core.Linga
import dev.panini.linganushasanam.*
/** Liṅgānuśāsanam 189: सर्वादीनि सर्वनामानि । */
object SarvadiniSarvanamaniSutra : LinganushasanaSutra(
    "189", "सर्वादीनि सर्वनामानि", "सर्वादि सर्वनाम विशेष्य के अनुसार तीनों लिङ्गों में होते हैं।", LinganushasanaSection.AVISISHTALINGA, targetLingas = Linga.entries.toSet(), 40,
) { override fun matches(context: LingaRuleContext) = context.isPronoun && context.governingLinga != null }
