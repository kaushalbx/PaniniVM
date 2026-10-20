package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 5: अशनिभरण्यरणयः पुंसि च. */
object AsaniBharaniAraniSutra : LinganushasanaSutra(
    number = "5",
    text = "अशनिभरण्यरणयः पुंसि च",
    hindiExplanation = "अशनि, भरणी और अरणि स्त्रीलिङ्ग तथा पुंलिङ्ग दोनों में प्रयुक्त होते हैं।",
    section = LinganushasanaSection.STRILINGA,
    targetLingas = setOf(Linga.STRI, Linga.PUMS),
    priority = 50,
) {
    private val members = setOf("अशनि", "भरणी", "अरणि")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}
