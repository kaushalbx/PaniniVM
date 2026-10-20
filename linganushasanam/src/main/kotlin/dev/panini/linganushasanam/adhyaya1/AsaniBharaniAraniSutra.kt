package dev.panini.linganushasanam.adhyaya1

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 5: अशनिभरण्यरणयः पुंसि च. */
object AsaniBharaniAraniSutra : LinganushasanaSutra(
    number = "5",
    text = "अशनिभरण्यरणय पुंसि च ।",
    hindiExplanation = "अशनि, भरणी और अरणि स्त्रीलिङ्ग तथा पुंलिङ्ग दोनों में प्रयुक्त होते हैं।",
    section = LinganushasanaSection.STRILINGA,
    targetLinga = Linga.STRI,
    priority = 50,
) {
    override val targetLingas = setOf(Linga.STRI, Linga.PUMS)
    private val members = setOf("अशनि", "भरणी", "अरणि")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}
