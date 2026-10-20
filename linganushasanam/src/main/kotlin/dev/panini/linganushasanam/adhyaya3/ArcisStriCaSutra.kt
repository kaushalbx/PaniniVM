package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 137: अर्चिः स्त्रियाञ्च । */
object ArcisStriCaSutra : LinganushasanaSutra(
    number = "137", text = "अर्चिः स्त्रियाञ्च ।", hindiExplanation = "अर्चिस् नपुंसकलिङ्ग तथा स्त्रीलिङ्ग दोनों में प्रयुक्त होता है।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.NAPUMSAKA, priority = 50,
) {
    override val targetLingas = setOf(Linga.STRI, Linga.NAPUMSAKA)
    private val members = setOf("अर्चिस्")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

