package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 97: मानसबिसबुससाहसानि नपुंसके । (मानस स्थाने पनस इति क्वचित्) */
object ManasaBisaBusaSahasaSutra : LinganushasanaSutra(
    number = "97",
    text = "मानसबिसबुससाहसानि नपुंसके । (मानस स्थाने पनस इति क्वचित्)",
    hindiExplanation = "मानस/पनस, बिस, बुस और साहस नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLinga = Linga.NAPUMSAKA,
    priority = 50,
) {
    private val members = setOf("मानस", "पनस", "बिस", "बुस", "साहस")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

