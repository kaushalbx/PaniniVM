package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 58: दारुकशेरुजतुवस्तुमस्तूनि नपुंसके । */
object DaruKaseruJatruVastuMastuSutra : LinganushasanaSutra(
    number = "58",
    text = "दारुकशेरुजतुवस्तुमस्तूनि नपुंसके",
    hindiExplanation = "दारु, कशेरु, जतु, वस्तु और मस्तु नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLingas = setOf(Linga.NAPUMSAKA),
    priority = 50,
) {
    private val members = setOf("दारु", "कशेरु", "जतु", "वस्तु", "मस्तु")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

