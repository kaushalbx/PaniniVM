package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 168: धान्याज्यशस्यरूप्यपण्यवर्ण्यधिष्ण्यहव्यकव्यकाव्यसत्यापत्य- मूल्यशिक्यकुड्यमद्यहर्म्यतूर्यसैन्यानि । */
object DhanyaAjyaSasyaRupyaSutra : LinganushasanaSutra(
    number = "168", text = "धान्याज्यसस्यरूप्यपण्यवर्ण्यधृष्यहव्यकव्यकाव्यसत्यापत्यमूल्यशिक्यकुड्यमद्यहर्म्यतूर्यसैन्यानि", hindiExplanation = "सूत्र में पठित धान्य आदि शब्द नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLingas = setOf(Linga.NAPUMSAKA), priority = 50,
) {
    private val members = setOf("धान्य", "आज्य", "सस्य", "रूप्य", "पण्य", "वर्ण्य", "धृष्य", "हव्य", "कव्य", "काव्य", "सत्य", "अपत्य", "मूल्य", "शिक्य", "कुड्य", "मद्य", "हर्म्य", "तूर्य", "सैन्य")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

