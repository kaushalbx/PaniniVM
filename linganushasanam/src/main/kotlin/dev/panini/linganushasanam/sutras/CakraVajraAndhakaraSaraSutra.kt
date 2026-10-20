package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 92: चक्रवज्रान्धकारसारावारपारक्षीरतोमरश‍ृङ्गारभृङ्गार- मन्दारोशीरतिमिरशिशिराणि नपुंसके च । */
object CakraVajraAndhakaraSaraSutra : LinganushasanaSutra(
    number = "92",
    text = "चक्रवज्रान्धकारसारावारपारक्षीरतोमरशृङ्गारबषङ्गारमन्दारोशीरतिमितशिशिराणि नपुंसके च",
    hindiExplanation = "सूत्र में पठित चक्र आदि शब्द पुंलिङ्ग तथा नपुंसकलिङ्ग दोनों में प्रयुक्त होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA),
    priority = 50,
) {
    private val members = setOf("चक्र", "वज्र", "अन्धकार", "सार", "आवार", "पार", "क्षीर", "तोमर", "शृङ्गार", "बषङ्गार", "मन्दार", "उशीर", "तिमित", "शिशिर")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

