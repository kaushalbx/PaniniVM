package dev.panini.linganushasanam.adhyaya4

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

/**
 * Liṅgānuśāsanam 4.1: विशेष्यनिघ्नम्.
 * Adjectives take the gender of their head noun (विशेष्य).
 */
object VisesyanighnaSutra : LinganushasanaSutra(
    number = "4.1",
    text = "विशेष्यनिघ्नम्",
    hindiExplanation = "विशेषण शब्द विशेष्य के लिङ्ग के अधीन होते हैं (उदा. सुंदरः पुरुषः, सुंदरी नारी, सुंदरम् फलम्)।",
    section = LinganushasanaSection.VISESYANIGHNALINGA,
    targetLinga = Linga.PUMS,
    priority = 10,
) {
    override fun matches(context: LingaRuleContext): Boolean {
        return context.pratipadika in setOf("सुन्दर", "महत्", "प्रिय", "शुभ", "उत्तम")
    }
}
