package dev.panini.linganushasanam.sutras
import dev.panini.core.Linga
import dev.panini.linganushasanam.*
/** Liṅgānuśāsanam 187: कृत्याश्च । */
object KrtyasCaSutra : LinganushasanaSutra(
    "187", "कृत्याश्च", "कृत्यप्रत्ययान्त शब्द विशेष्य के अनुसार तीनों लिङ्गों में होते हैं।", LinganushasanaSection.AVISISHTALINGA, targetLingas = Linga.entries.toSet(), 40,
) { override fun matches(context: LingaRuleContext) = context.governingLinga != null && context.pratyaya in setOf("तव्य", "तव्यत्", "अनीयर्", "यत्", "ण्यत्", "क्यप्") }
