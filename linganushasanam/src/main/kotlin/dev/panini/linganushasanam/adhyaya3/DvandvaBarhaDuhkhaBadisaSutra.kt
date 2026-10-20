package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 171: द्वन्द्वबर्हदुःखबडिशपिच्छविम्बकुटुम्बकवचकवरवृन्दराकाणि । (विम्ब स्थाने क्वचिद्विश्व) */
object DvandvaBarhaDuhkhaBadisaSutra : LinganushasanaSutra(
    number = "171", text = "द्वन्द्वबर्हदुःखबडिशपिच्छविम्बकुटुम्बकवचकवरवृन्दराकाणि । (विम्ब स्थाने क्वचिद्विश्व)", hindiExplanation = "सूत्र में पठित द्वन्द्व आदि शब्द तथा विश्व पाठान्तर नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.NAPUMSAKA, priority = 50,
) {
    private val members = setOf("द्वन्द्व", "बर्ह", "दुःख", "बडिश", "पिच्छ", "विम्ब", "विश्व", "कुटुम्ब", "कवच", "कवर", "वृन्द", "राका")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

