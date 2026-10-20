package dev.panini.linganushasanam.sutras
import dev.panini.core.Linga
import dev.panini.shiksha.Svara
import dev.panini.shiksha.toVarnas
import dev.panini.linganushasanam.*
/** Liṅgānuśāsanam 174: गुणवचनमुकारान्तं नपुंसकञ्च । */
object GunavacanaUkarantaNapumsakamCaSutra : LinganushasanaSutra(
    "174", "गुणवचनमुकारान्तं नपुंसकं च", "उकारान्त गुणवाचक शब्द तीनों लिङ्गों में होते हैं।",
    LinganushasanaSection.STRIPUMSALINGA, targetLingas = Linga.entries.toSet(), 10,
) { override fun matches(context: LingaRuleContext) = context.isQualityExpression && context.pratipadika.toVarnas().lastOrNull() in setOf(Svara.U, Svara.UU) }
