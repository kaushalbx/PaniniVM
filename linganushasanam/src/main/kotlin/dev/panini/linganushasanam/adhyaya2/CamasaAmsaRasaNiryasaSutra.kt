package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 98: चमसांसरसनिर्यासोपवासकार्पासवासमासकासकांसमांसानि नपुंसके च । (चमसांसरस स्थाने चमससरस इति क्वचितपाठः) */
object CamasaAmsaRasaNiryasaSutra : LinganushasanaSutra(
    number = "98",
    text = "चमसांसरसनिर्यासोपवासकार्पासवासमासकासकांसमांसानि नपुंसके च । (चमसांसरस स्थाने चमससरस इति क्वचितपाठः)",
    hindiExplanation = "सूत्र में पठित चमस आदि शब्द पुंलिङ्ग तथा नपुंसकलिङ्ग दोनों में प्रयुक्त होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLinga = Linga.PUMS,
    priority = 50,
) {
    override val targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA)
    private val members = setOf("चमस", "अंस", "रस", "निर्यास", "उपवास", "कार्पास", "वास", "मास", "कास", "कांस", "मांस")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

