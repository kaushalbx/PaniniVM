package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 62: चिबुकशालूकप्रातिपादिकांशुकोल्मुकानि नपुंसके । */
object CibukaSalukaPratipadikaAmsukaUlmukaSutra : LinganushasanaSutra(
    number = "62",
    text = "चिबुकशालूकप्रातिपादिकांशुकोल्मुकानि नपुंसके ।",
    hindiExplanation = "चिबुक, शालूक, प्रातिपदिक, अंशुक और उल्मुक नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLinga = Linga.NAPUMSAKA,
    priority = 50,
) {
    private val members = setOf("चिबुक", "शालूक", "प्रातिपदिक", "अंशुक", "उल्मुक")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

