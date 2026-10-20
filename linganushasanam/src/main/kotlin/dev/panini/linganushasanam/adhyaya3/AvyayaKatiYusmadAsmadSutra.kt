package dev.panini.linganushasanam.adhyaya3
import dev.panini.core.Linga
import dev.panini.linganushasanam.*
/** Liṅgānuśāsanam 186, including the transmitted डति variant. */
object AvyayaKatiYusmadAsmadSutra : LinganushasanaSutra(
    "186", "अव्ययं कतियुष्मदस्मदः । (कति स्थाने डति इति केचित्)", "अव्यय तथा कति/डति, युष्मद् और अस्मद् तीनों लिङ्गों में प्रयुक्त होते हैं।", LinganushasanaSection.AVISISHTALINGA, Linga.PUMS, 40,
) { override val targetLingas = Linga.entries.toSet(); override fun matches(context: LingaRuleContext) = context.isIndeclinable || context.pratipadika.phonologicallyIn(setOf("कति", "डति", "युष्मद्", "अस्मद्")) }
