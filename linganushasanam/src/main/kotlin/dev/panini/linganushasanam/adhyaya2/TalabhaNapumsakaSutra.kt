package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 81: तलभं नपुंसकम् । */
object TalabhaNapumsakaSutra : LinganushasanaSutra(
    number = "81",
    text = "तलभं नपुंसकम् ।",
    hindiExplanation = "तलभ शब्द नपुंसकलिङ्ग होता है।",
    section = LinganushasanaSection.PUMLINGA,
    targetLinga = Linga.NAPUMSAKA,
    priority = 50,
) {
    private val members = setOf("तलभ")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

