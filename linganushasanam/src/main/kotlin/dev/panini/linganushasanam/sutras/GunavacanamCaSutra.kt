package dev.panini.linganushasanam.sutras
import dev.panini.core.Linga
import dev.panini.linganushasanam.*
/** Liṅgānuśāsanam 186: गुणवचनञ्च । */
object GunavacanamCaSutra : LinganushasanaSutra(
    "186", "गुणवचनं च", "गुणवाचक शब्द विशेष्य के अनुसार तीनों लिङ्गों में होते हैं।", LinganushasanaSection.AVISISHTALINGA, targetLingas = Linga.entries.toSet(), 40,
) { override fun matches(context: LingaRuleContext) = context.isQualityExpression && context.governingLinga != null }
