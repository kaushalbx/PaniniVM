package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 87: किसलयहृदयेन्द्रियोत्तरीयाणि नपुंसके । */
object KisalayaHrdayaIndriyaUttariyaSutra : LinganushasanaSutra(
    number = "87",
    text = "किसलयहृदयेन्द्रियोत्तरीयाणि नपुंसके",
    hindiExplanation = "किसलय, हृदय, इन्द्रिय और उत्तरीय नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLingas = setOf(Linga.NAPUMSAKA),
    priority = 50,
) {
    private val members = setOf("किसलय", "हृदय", "इन्द्रिय", "उत्तरीय")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

