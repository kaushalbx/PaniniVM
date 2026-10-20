package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 50: अभ्रं नपुंसकम् । */
object AbhraNapumsakaSutra : LinganushasanaSutra(
    number = "50",
    text = "अभ्रं नपुंसकम् ।",
    hindiExplanation = "अभ्र शब्द नपुंसकलिङ्ग होता है।",
    section = LinganushasanaSection.PUMLINGA,
    targetLinga = Linga.NAPUMSAKA,
    priority = 50,
) {
    private val members = setOf("अभ्र")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

