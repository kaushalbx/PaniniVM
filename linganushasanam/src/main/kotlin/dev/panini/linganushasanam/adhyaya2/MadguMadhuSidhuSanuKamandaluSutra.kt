package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 56: मद्गुमधुशीधुसानुकमण्डलूनि नपुंसके च । */
object MadguMadhuSidhuSanuKamandaluSutra : LinganushasanaSutra(
    number = "56",
    text = "मद्गुमधुशीधुसानुकमण्डलूनि नपुंसके च ।",
    hindiExplanation = "मद्गु, मधु, शीधु, सानु और कमण्डलु पुंलिङ्ग तथा नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLinga = Linga.PUMS,
    priority = 50,
) {
    override val targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA)
    private val members = setOf("मद्गु", "मधु", "शीधु", "सानु", "कमण्डलु")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

