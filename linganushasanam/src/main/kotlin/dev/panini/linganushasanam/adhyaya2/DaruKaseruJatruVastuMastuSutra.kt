package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 58: दारुकसेरुजत्रुवस्तुमस्तूनि नपुंसके । (क्वचित् जतु इति पाठः ।) */
object DaruKaseruJatruVastuMastuSutra : LinganushasanaSutra(
    number = "58",
    text = "दारुकसेरुजत्रुवस्तुमस्तूनि नपुंसके । (क्वचित् जतु इति पाठः ।)",
    hindiExplanation = "दारु, कसेरु, जत्रु, वस्तु और मस्तु नपुंसकलिङ्ग होते हैं; जतु पाठान्तर भी स्वीकृत है।",
    section = LinganushasanaSection.PUMLINGA,
    targetLinga = Linga.NAPUMSAKA,
    priority = 50,
) {
    private val members = setOf("दारु", "कसेरु", "जत्रु", "वस्तु", "मस्तु", "जतु")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

