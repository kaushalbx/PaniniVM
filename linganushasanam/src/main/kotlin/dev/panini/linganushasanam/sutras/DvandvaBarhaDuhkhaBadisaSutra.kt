package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 169: द्वन्द्वबर्हदुःखबडिशपिच्छबिम्बकुटुम्बकवचवरशरवृन्दारकाणि । */
object DvandvaBarhaDuhkhaBadisaSutra : LinganushasanaSutra(
    number = "169", text = "द्वन्द्वबर्हदुःखबडिशपिच्छबिम्बकुटुम्बकवचवरशरवृन्दारकाणि", hindiExplanation = "सूत्र में पठित द्वन्द्व आदि शब्द नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLingas = setOf(Linga.NAPUMSAKA), priority = 50,
) {
    private val members = setOf("द्वन्द्व", "बर्ह", "दुःख", "बडिश", "पिच्छ", "बिम्ब", "कुटुम्ब", "कवच", "वर", "शर", "वृन्दारक")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

