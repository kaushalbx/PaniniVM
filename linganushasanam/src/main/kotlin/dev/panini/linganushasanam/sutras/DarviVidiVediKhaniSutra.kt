package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 24: दर्विविदिवेदिखनिशाण्यभ्रिवेणिकृष्योषध्यङ्गुलयः । */
object DarviVidiVediKhaniSutra : LinganushasanaSutra(
    number = "24",
    text = "दर्विविदिवेदिखनिशान्यश्रिवेशिकृष्योषधिकट्यङ्गुलयः",
    hindiExplanation = "सूत्र में पठित दर्वि आदि शब्द स्त्रीलिङ्ग होते हैं।",
    section = LinganushasanaSection.STRILINGA,
    targetLingas = setOf(Linga.STRI),
    priority = 50,
) {
    private val members = setOf("दर्वि", "विदि", "वेदि", "खनि", "शानि", "अश्रि", "वेशि", "कृषि", "ओषधि", "कटि", "अङ्गुलि")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

