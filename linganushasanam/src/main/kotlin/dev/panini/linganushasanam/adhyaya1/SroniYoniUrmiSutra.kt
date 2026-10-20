package dev.panini.linganushasanam.adhyaya1

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 8: श्रोणियोन्यूर्मयः पुंसि च. */
object SroniYoniUrmiSutra : LinganushasanaSutra(
    number = "8",
    text = "श्रोणियोन्यूर्मय पुंसि च ।",
    hindiExplanation = "श्रोणि, योनि और ऊर्मि स्त्रीलिङ्ग तथा पुंलिङ्ग दोनों में प्रयुक्त होते हैं।",
    section = LinganushasanaSection.STRILINGA,
    targetLinga = Linga.STRI,
    priority = 50,
) {
    override val targetLingas = setOf(Linga.STRI, Linga.PUMS)
    private val members = setOf("श्रोणि", "योनि", "ऊर्मि")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}
