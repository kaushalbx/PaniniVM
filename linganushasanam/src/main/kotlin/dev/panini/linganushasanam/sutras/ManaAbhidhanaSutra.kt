package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.core.LingaSemanticClass
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

/** Liṅgānuśāsanam 103: मानाभिधानानि. */
object ManaAbhidhanaSutra : LinganushasanaSutra(
    number = "103", text = "मानाभिधामनि",
    hindiExplanation = "माप अथवा परिमाण के अभिधान पुंलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA, targetLingas = setOf(Linga.PUMS), priority = 40,
) {
    override fun matches(context: LingaRuleContext): Boolean = LingaSemanticClass.MEASURE in context.semanticClasses
}
