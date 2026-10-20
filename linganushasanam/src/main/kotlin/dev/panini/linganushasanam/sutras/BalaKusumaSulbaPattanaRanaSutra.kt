package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 158: बलकुसुमशुल्बपत्तनरणाभिधानानि । */
object BalaKusumaSulbaPattanaRanaSutra : LinganushasanaSutra(
    number = "158", text = "बलकुसुमशुल्बपत्तनरणाभिधानानि", hindiExplanation = "बल, कुसुम, शुल्ब, पत्तन और रण के अभिधान नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLingas = setOf(Linga.NAPUMSAKA), priority = 50,
) {
    private val members = setOf("बल", "कुसुम", "शुल्ब", "पत्तन", "रण")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

