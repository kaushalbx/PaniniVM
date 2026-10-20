package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 68: ऋणलवणपर्णतोरणरणोष्णानि नपुंसके । */
object RnaLavanaParnaToranaRanaUsnaSutra : LinganushasanaSutra(
    number = "68",
    text = "ऋणलवणपर्णतोरणरणोष्णानि नपुंसके ।",
    hindiExplanation = "ऋण, लवण, पर्ण, तोरण, रण और उष्ण नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLinga = Linga.NAPUMSAKA,
    priority = 50,
) {
    private val members = setOf("ऋण", "लवण", "पर्ण", "तोरण", "रण", "उष्ण")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

