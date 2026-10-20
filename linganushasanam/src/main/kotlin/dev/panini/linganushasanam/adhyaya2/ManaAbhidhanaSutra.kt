package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.core.LingaSemanticClass
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

/** Liṅgānuśāsanam 103: मानाभिधानानि. */
object ManaAbhidhanaSutra : LinganushasanaSutra(
    number = "103", text = "मानाभिधानानि ।",
    hindiExplanation = "माप अथवा परिमाण के अभिधान पुंलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA, targetLinga = Linga.PUMS, priority = 40,
) {
    override fun matches(context: LingaRuleContext): Boolean = LingaSemanticClass.MEASURE in context.semanticClasses
}
