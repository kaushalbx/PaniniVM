package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.core.LingaSemanticClass
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

/** Liṅgānuśāsanam 100: रश्मिदिवसाभिधानानि. */
object RasmiDivasaAbhidhanaSutra : LinganushasanaSutra(
    number = "100", text = "रश्मिदिवसाभिधानानि ।",
    hindiExplanation = "रश्मि अथवा दिवस के अभिधान पुंलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA, targetLinga = Linga.PUMS, priority = 40,
) {
    override fun matches(context: LingaRuleContext): Boolean = LingaSemanticClass.RAY_OR_DAY in context.semanticClasses
}
