package dev.panini.linganushasanam.adhyaya3
import dev.panini.core.Linga
import dev.panini.linganushasanam.*
/** Liṅgānuśāsanam 188: गुणवचनञ्च । */
object GunavacanamCaSutra : LinganushasanaSutra(
    "188", "गुणवचनञ्च ।", "गुणवाचक शब्द विशेष्य के अनुसार तीनों लिङ्गों में होते हैं।", LinganushasanaSection.AVISISHTALINGA, Linga.PUMS, 40,
) { override val targetLingas = Linga.entries.toSet(); override fun matches(context: LingaRuleContext) = context.isQualityExpression && context.governingLinga != null }
