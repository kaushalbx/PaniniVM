package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 168: श्राद्धकुलिशदैवपीठकुण्डाङ्काङ्गदधिसक्थ्यक्ष्यस्थ्यास्पदाकाश कण्ववीजानि ।(क्वचित् भाण्डोऽपि) */
object SraddhaKulisaDaivaPithaSutra : LinganushasanaSutra(
    number = "168", text = "श्राद्धकुलिशदैवपीठकुण्डाङ्काङ्गदधिसक्थ्यक्ष्यस्थ्यास्पदाकाश कण्ववीजानि ।(क्वचित् भाण्डोऽपि)", hindiExplanation = "सूत्र में पठित श्राद्ध आदि शब्द तथा भाण्ड पाठान्तर नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.NAPUMSAKA, priority = 50,
) {
    private val members = setOf("श्राद्ध", "कुलिश", "दैव", "पीठ", "कुण्ड", "अङ्क", "अङ्ग", "दधि", "सक्थि", "अक्षि", "अस्थि", "आस्पद", "आकाश", "कण्व", "बीज", "भाण्ड")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

