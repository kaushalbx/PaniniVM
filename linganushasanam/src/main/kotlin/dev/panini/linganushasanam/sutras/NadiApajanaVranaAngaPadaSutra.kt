package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.core.LingaSemanticClass
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

/** Liṅgānuśāsanam 107: नाड्यपजनोपपदानि व्रणाङ्गपदानि. */
object NadiApajanaVranaAngaPadaSutra : LinganushasanaSutra(
    number = "107", text = "नाड्यपजनोपपदानि व्रणाङ्गपदानि",
    hindiExplanation = "नाडी/अपजन उपपद वाले तथा व्रण या अङ्गवाचक पद पुंलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA, targetLingas = setOf(Linga.PUMS), priority = 40,
) {
    override fun matches(context: LingaRuleContext): Boolean = context.semanticClasses.any {
        it == LingaSemanticClass.NADI_OR_APAJANA_UPAPADA || it == LingaSemanticClass.WOUND_OR_LIMB
    }
}
