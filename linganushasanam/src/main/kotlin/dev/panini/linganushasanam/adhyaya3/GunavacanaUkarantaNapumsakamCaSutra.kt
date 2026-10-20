package dev.panini.linganushasanam.adhyaya3
import dev.panini.core.Linga
import dev.panini.shiksha.Svara
import dev.panini.shiksha.toVarnas
import dev.panini.linganushasanam.*
/** Liṅgānuśāsanam 176: गुणवचनमुकारान्तं नपुंसकञ्च । */
object GunavacanaUkarantaNapumsakamCaSutra : LinganushasanaSutra(
    "176", "गुणवचनमुकारान्तं नपुंसकञ्च ।", "उकारान्त गुणवाचक शब्द तीनों लिङ्गों में होते हैं।",
    LinganushasanaSection.STRIPUMSALINGA, Linga.STRI, 10,
) { override val targetLingas = Linga.entries.toSet(); override fun matches(context: LingaRuleContext) = context.isQualityExpression && context.pratipadika.toVarnas().lastOrNull() in setOf(Svara.U, Svara.UU) }
