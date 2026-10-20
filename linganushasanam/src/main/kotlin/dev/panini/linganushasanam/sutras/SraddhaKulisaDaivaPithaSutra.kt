package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 166: श्राद्धकुलिशदैवपीठकुण्डाङ्काङ्गदधिसक्थक्ष्यास्यास्पदाकाशकण्वबीजानि । */
object SraddhaKulisaDaivaPithaSutra : LinganushasanaSutra(
    number = "166", text = "श्राद्धकुलिशदैवपीठकुण्डाङ्काङ्गदधिसक्थक्ष्यास्यास्पदाकाशकण्वबीजानि", hindiExplanation = "सूत्र में पठित श्राद्ध आदि शब्द नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLingas = setOf(Linga.NAPUMSAKA), priority = 50,
) {
    private val members = setOf("श्राद्ध", "कुलिश", "दैव", "पीठ", "कुण्ड", "अङ्क", "अङ्ग", "दधि", "सक्थि", "अक्षि", "आस्य", "आस्पद", "आकाश", "कण्व", "बीज")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

