package dev.panini.linganushasanam.sutras
import dev.panini.core.Linga
import dev.panini.linganushasanam.*
/** Liṅgānuśāsanam 186, including the transmitted डति variant. */
object AvyayaKatiYusmadAsmadSutra : LinganushasanaSutra(
    "184", "अव्ययं कतियुष्मदः", "अव्यय तथा कति/डति, युष्मद् और अस्मद् तीनों लिङ्गों में प्रयुक्त होते हैं।", LinganushasanaSection.AVISISHTALINGA, targetLingas = Linga.entries.toSet(), 40,
) { override fun matches(context: LingaRuleContext) = context.isIndeclinable || context.pratipadika.phonologicallyIn(setOf("कति", "डति", "युष्मद्", "अस्मद्")) }
