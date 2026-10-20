package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 102: दिनाहनी नपुंसके । */
object DinaAhaniNapumsakaSutra : LinganushasanaSutra(
    number = "102", text = "दिनाहनी नपुंसके ।", hindiExplanation = "दिन और अहन् नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA, targetLinga = Linga.NAPUMSAKA, priority = 50,
) {
    private val members = setOf("दिन", "अहन्")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

