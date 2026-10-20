package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 166: वियज्जगत्सकृच्छकन्पृषच्छकृद्यकृदुदश्वितः । (शकत् स्थाने कृषत् इति केचित्) */
object ViyatJagatSakrtSakanSutra : LinganushasanaSutra(
    number = "166", text = "वियज्जगत्सकृच्छकन्पृषच्छकृद्यकृदुदश्वितः । (शकत् स्थाने कृषत् इति केचित्)", hindiExplanation = "सूत्र में पठित वियत् आदि शब्द तथा कृषत् पाठान्तर नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.NAPUMSAKA, priority = 50,
) {
    private val members = setOf("वियत्", "जगत्", "सकृत्", "शकन्", "पृषत्", "शकृत्", "कृषत्", "यकृत्", "उदश्वित्")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

