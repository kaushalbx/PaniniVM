package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 98: चमसांसरसनिर्यासोपवासकार्पासवासमासकासकांसमांसानि नपुंसके च । (चमसांसरस स्थाने चमससरस इति क्वचितपाठः) */
object CamasaAmsaRasaNiryasaSutra : LinganushasanaSutra(
    number = "98",
    text = "चमसांसररनिर्यासोपवासकार्पासवासमासकासकांसमांसानि नपुंसके च",
    hindiExplanation = "सूत्र में पठित चमस आदि शब्द पुंलिङ्ग तथा नपुंसकलिङ्ग दोनों में प्रयुक्त होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA),
    priority = 50,
) {
    private val members = setOf("चमस", "अंस", "रर", "निर्यास", "उपवास", "कार्पास", "वास", "मास", "कास", "कांस", "मांस")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

