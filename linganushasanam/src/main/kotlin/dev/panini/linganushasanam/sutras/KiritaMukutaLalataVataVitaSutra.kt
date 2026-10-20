package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 65: किरीटमुकुटललाटवटवीटश‍ृङ्गाटकरटलोष्टानि नपुंसके । */
object KiritaMukutaLalataVataVitaSutra : LinganushasanaSutra(
    number = "65",
    text = "किरीटमुकुटललाटवटवीटशृङ्गाटकराटलोष्टानि नपुंसके च",
    hindiExplanation = "किरीट, मुकुट, ललाट, वट, वीट, शृङ्गाटक, रट और लोष्ट नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLingas = setOf(Linga.NAPUMSAKA),
    priority = 50,
) {
    private val members = setOf("किरीट", "मुकुट", "ललाट", "वट", "वीट", "शृङ्गाटक", "राट", "लोष्ट")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

