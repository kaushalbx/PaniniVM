package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 160: बलकुसुमशुल्बपत्तनरणाभिधानानि । */
object BalaKusumaSulbaPattanaRanaSutra : LinganushasanaSutra(
    number = "160", text = "बलकुसुमशुल्बपत्तनरणाभिधानानि ।", hindiExplanation = "बल, कुसुम, शुल्ब, पत्तन और रण के अभिधान नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.NAPUMSAKA, priority = 50,
) {
    private val members = setOf("बल", "कुसुम", "शुल्ब", "पत्तन", "रण")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

