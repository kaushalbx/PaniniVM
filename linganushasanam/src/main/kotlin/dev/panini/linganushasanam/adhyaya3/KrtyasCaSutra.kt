package dev.panini.linganushasanam.adhyaya3
import dev.panini.core.Linga
import dev.panini.linganushasanam.*
/** Liṅgānuśāsanam 189: कृत्याश्च । */
object KrtyasCaSutra : LinganushasanaSutra(
    "189", "कृत्याश्च ।", "कृत्यप्रत्ययान्त शब्द विशेष्य के अनुसार तीनों लिङ्गों में होते हैं।", LinganushasanaSection.AVISISHTALINGA, Linga.PUMS, 40,
) { override val targetLingas = Linga.entries.toSet(); override fun matches(context: LingaRuleContext) = context.governingLinga != null && context.pratyaya in setOf("तव्य", "तव्यत्", "अनीयर्", "यत्", "ण्यत्", "क्यप्") }
