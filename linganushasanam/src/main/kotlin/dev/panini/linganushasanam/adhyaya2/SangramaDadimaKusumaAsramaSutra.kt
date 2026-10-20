package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 85: सङ्ग्रामदाडिमकुसुमाश्रमक्षेमक्षौमहोमोद्दामानि नपुंसके च */
object SangramaDadimaKusumaAsramaSutra : LinganushasanaSutra(
    number = "85",
    text = "सङ्ग्रामदाडिमकुसुमाश्रमक्षेमक्षौमहोमोद्दामानि नपुंसके च",
    hindiExplanation = "सूत्र में पठित सङ्ग्राम आदि शब्द पुंलिङ्ग तथा नपुंसकलिङ्ग दोनों में प्रयुक्त होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLinga = Linga.PUMS,
    priority = 50,
) {
    override val targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA)
    private val members = setOf("सङ्ग्राम", "दाडिम", "कुसुम", "आश्रम", "क्षेम", "क्षौम", "होम", "उद्दाम")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

