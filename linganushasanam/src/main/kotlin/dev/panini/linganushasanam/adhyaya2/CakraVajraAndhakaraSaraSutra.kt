package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 92: चक्रवज्रान्धकारसारावारपारक्षीरतोमरश‍ृङ्गारभृङ्गार- मन्दारोशीरतिमिरशिशिराणि नपुंसके च । */
object CakraVajraAndhakaraSaraSutra : LinganushasanaSutra(
    number = "92",
    text = "चक्रवज्रान्धकारसारावारपारक्षीरतोमरश‍ृङ्गारभृङ्गार- मन्दारोशीरतिमिरशिशिराणि नपुंसके च ।",
    hindiExplanation = "सूत्र में पठित चक्र आदि शब्द पुंलिङ्ग तथा नपुंसकलिङ्ग दोनों में प्रयुक्त होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLinga = Linga.PUMS,
    priority = 50,
) {
    override val targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA)
    private val members = setOf("चक्र", "वज्र", "अन्धकार", "सार", "आवार", "पार", "क्षीर", "तोमर", "शृङ्गार", "भृङ्गार", "मन्दार", "उशीर", "तिमिर", "शिशिर")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

