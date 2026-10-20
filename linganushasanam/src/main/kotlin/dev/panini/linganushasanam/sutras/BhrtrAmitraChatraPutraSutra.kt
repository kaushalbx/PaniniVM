package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 156: भृत्रामित्रच्छात्रपुत्रमन्त्रवृत्रमेढ्रोष्ट्राः पुंसि । */
object BhrtrAmitraChatraPutraSutra : LinganushasanaSutra(
    number = "156",
    text = "भृत्रामित्रछात्रपुत्रमन्त्रवृत्रमेढोष्ट्राः पुंस",
    hindiExplanation = "त्रान्त नपुंसकाधिकार में पठित ये शब्द पुंलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA,
    targetLingas = setOf(Linga.PUMS),
    priority = 40,
) {
    private val members = setOf("भृत्र", "अमित्र", "छात्र", "पुत्र", "मन्त्र", "वृत्र", "मेढ", "उष्ट्र")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}
