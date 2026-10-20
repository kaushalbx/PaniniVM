package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 164: वियज्जगत्सकृत्शकन्पृषत्शकृद्यकृदुदश्वितः । */
object ViyatJagatSakrtSakanSutra : LinganushasanaSutra(
    number = "164", text = "वियज्जगत्सकृत्शकन्पृषत्शकृद्यकृदुदश्वितः", hindiExplanation = "सूत्र में पठित वियत् आदि शब्द नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLingas = setOf(Linga.NAPUMSAKA), priority = 50,
) {
    private val members = setOf("वियत्", "जगत्", "सकृत्", "शकन्", "पृषत्", "शकृत्", "यकृत्", "उदश्वित्")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

