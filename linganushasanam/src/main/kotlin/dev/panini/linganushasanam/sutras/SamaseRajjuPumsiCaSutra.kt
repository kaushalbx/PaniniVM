package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyEquals

/** Liṅgānuśāsanam 53: समासे रज्जुः पुंसि च. */
object SamaseRajjuPumsiCaSutra : LinganushasanaSutra(
    number = "53",
    text = "समासे रज्जुः पुंसि च",
    hindiExplanation = "रज्जु समास में स्त्रीलिङ्ग के साथ पुंलिङ्ग में भी प्रयुक्त होती है।",
    section = LinganushasanaSection.PUMLINGA,
    targetLingas = setOf(Linga.STRI, Linga.PUMS),
    priority = 40,
) {

    override fun matches(context: LingaRuleContext): Boolean =
        context.samasaType != null && context.pratipadika.phonologicallyEquals("रज्जु")
}
