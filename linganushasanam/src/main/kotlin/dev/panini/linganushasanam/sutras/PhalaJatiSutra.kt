package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.core.LingaSemanticClass
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

/** Liṅgānuśāsanam 162: फलजातिः । */
object PhalaJatiSutra : LinganushasanaSutra(
    number = "162", text = "फलजातिः", hindiExplanation = "फलजातिवाचक शब्द नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLingas = setOf(Linga.NAPUMSAKA), priority = 40,
) {
    override fun matches(context: LingaRuleContext): Boolean = LingaSemanticClass.FRUIT_KIND in context.semanticClasses
}
