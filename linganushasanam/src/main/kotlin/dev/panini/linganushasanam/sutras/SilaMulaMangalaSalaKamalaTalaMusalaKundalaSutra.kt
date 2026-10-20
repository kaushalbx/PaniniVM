package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

object SilaMulaMangalaSalaKamalaTalaMusalaKundalaSutra : LinganushasanaSutra(
    number = "143",
    text = "शीलमूलमङ्गलसालकमलतलमुसलकुण्डलपललमृणालवालनिगलपलालबिडालखिलशूलाः पुंसि च",
    hindiExplanation = "ये नपुंसकलिङ्ग शब्द पुंलिङ्ग में भी प्रयुक्त होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA,
    targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA),
    priority = 50,
) {
    private val members = setOf("शील", "मूल", "मङ्गल", "साल", "कमल", "तल", "मुसल", "कुण्डल", "पलल", "मृणाल", "वाल", "निगल", "पलाल", "बिडाल", "खिल", "शूल")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}
