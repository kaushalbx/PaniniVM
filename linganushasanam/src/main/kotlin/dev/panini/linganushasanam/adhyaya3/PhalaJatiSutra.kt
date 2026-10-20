package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.Linga
import dev.panini.core.LingaSemanticClass
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

/** Liṅgānuśāsanam 164: फलजातिः । */
object PhalaJatiSutra : LinganushasanaSutra(
    number = "164", text = "फलजातिः ।", hindiExplanation = "फलजातिवाचक शब्द नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.NAPUMSAKA, priority = 40,
) {
    override fun matches(context: LingaRuleContext): Boolean = LingaSemanticClass.FRUIT_KIND in context.semanticClasses
}
