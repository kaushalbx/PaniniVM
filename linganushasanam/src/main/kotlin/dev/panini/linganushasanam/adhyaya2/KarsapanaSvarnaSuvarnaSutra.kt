package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 69: कार्षापणस्वर्णसुवर्णव्रणचरणवृषणविषाणचूर्णतृणानि नपुंसके च । */
object KarsapanaSvarnaSuvarnaSutra : LinganushasanaSutra(
    number = "69",
    text = "कार्षापणस्वर्णसुवर्णव्रणचरणवृषणविषाणचूर्णतृणानि नपुंसके च ।",
    hindiExplanation = "सूत्र में पठित कार्षापण आदि शब्द पुंलिङ्ग तथा नपुंसकलिङ्ग दोनों में प्रयुक्त होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLinga = Linga.PUMS,
    priority = 50,
) {
    override val targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA)
    private val members = setOf("कार्षापण", "स्वर्ण", "सुवर्ण", "व्रण", "चरण", "वृषण", "विषाण", "चूर्ण", "तृण")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

