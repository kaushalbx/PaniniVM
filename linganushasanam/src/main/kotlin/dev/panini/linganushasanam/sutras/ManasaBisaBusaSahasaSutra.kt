package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 97: पनसबिसबुससाहसानि नपुंसके । */
object ManasaBisaBusaSahasaSutra : LinganushasanaSutra(
    number = "97",
    text = "पनसबिसबुससाहसानि नपुंसके",
    hindiExplanation = "पनस, बिस, बुस और साहस नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLingas = setOf(Linga.NAPUMSAKA),
    priority = 50,
) {
    private val members = setOf("पनस", "बिस", "बुस", "साहस")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

