package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.Linga
import dev.panini.core.LingaSemanticClass
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

/** Liṅgānuśāsanam 165: वृक्षजातिः स्त्रियामेव । */
object VrksaJatiStriyamEvaSutra : LinganushasanaSutra(
    number = "165", text = "वृक्षजातिः स्त्रियामेव ।", hindiExplanation = "वृक्षजातिवाचक शब्द केवल स्त्रीलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.STRI, priority = 40,
) {
    override fun matches(context: LingaRuleContext): Boolean = LingaSemanticClass.TREE_KIND in context.semanticClasses
}
