package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 23: प्रावड्विप्रुड्रुड्विटत्विषः । */
object PravrsViprusRutVitTvisSutra : LinganushasanaSutra(
    number = "23",
    text = "प्रावृट्विप्रट्रुट्विट्त्विषः",
    hindiExplanation = "प्रावृष्, विप्रुष्, रुत्, विट् और त्विष् स्त्रीलिङ्ग होते हैं।",
    section = LinganushasanaSection.STRILINGA,
    targetLingas = setOf(Linga.STRI),
    priority = 50,
) {
    private val members = setOf("प्रावृष्", "विप्रुष्", "रुत्", "विट्", "त्विष्")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

