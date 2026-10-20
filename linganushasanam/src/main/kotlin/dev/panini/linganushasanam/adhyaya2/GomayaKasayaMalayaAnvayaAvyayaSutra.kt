package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 88: गोमयकषायमलयान्वयाव्ययानि नपुंसके च । */
object GomayaKasayaMalayaAnvayaAvyayaSutra : LinganushasanaSutra(
    number = "88",
    text = "गोमयकषायमलयान्वयाव्ययानि नपुंसके च ।",
    hindiExplanation = "गोमय, कषाय, मलय, अन्वय और अव्यय पुंलिङ्ग तथा नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLinga = Linga.PUMS,
    priority = 50,
) {
    override val targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA)
    private val members = setOf("गोमय", "कषाय", "मलय", "अन्वय", "अव्यय")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

