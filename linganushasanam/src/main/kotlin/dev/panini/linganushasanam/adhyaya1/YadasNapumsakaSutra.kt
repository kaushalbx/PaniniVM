package dev.panini.linganushasanam.adhyaya1

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 19: यादो नपुंसकम् । */
object YadasNapumsakaSutra : LinganushasanaSutra(
    number = "19",
    text = "यादो नपुंसकम् ।",
    hindiExplanation = "यादस् शब्द नपुंसकलिङ्ग होता है।",
    section = LinganushasanaSection.STRILINGA,
    targetLinga = Linga.NAPUMSAKA,
    priority = 50,
) {
    private val members = setOf("यादस्")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

