package dev.panini.linganushasanam.adhyaya1

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 7: वह्निवृष्ण्यग्नयः पुंसि. */
object VahniVrsniAgniSutra : LinganushasanaSutra(
    number = "7",
    text = "वह्निवृष्ण्यग्नयः पुंसि ।",
    hindiExplanation = "वह्नि, वृष्णि और अग्नि पुंलिङ्ग होते हैं।",
    section = LinganushasanaSection.STRILINGA,
    targetLinga = Linga.PUMS,
    priority = 50,
) {
    private val members = setOf("वह्नि", "वृष्णि", "अग्नि")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}
