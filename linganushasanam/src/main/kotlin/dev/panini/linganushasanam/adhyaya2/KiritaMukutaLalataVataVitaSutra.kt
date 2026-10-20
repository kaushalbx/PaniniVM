package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 65: किरीटमुकुटललाटवटवीटश‍ृङ्गाटकरटलोष्टानि नपुंसके । */
object KiritaMukutaLalataVataVitaSutra : LinganushasanaSutra(
    number = "65",
    text = "किरीटमुकुटललाटवटवीटश‍ृङ्गाटकरटलोष्टानि नपुंसके ।",
    hindiExplanation = "किरीट, मुकुट, ललाट, वट, वीट, शृङ्गाटक, रट और लोष्ट नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLinga = Linga.NAPUMSAKA,
    priority = 50,
) {
    private val members = setOf("किरीट", "मुकुट", "ललाट", "वट", "वीट", "शृङ्गाटक", "रट", "लोष्ट")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

