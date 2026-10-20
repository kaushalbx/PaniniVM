package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 56: मद्गुमधुसीधुशीधुसानुकमण्डलूनि नपुंसके च । */
object MadguMadhuSidhuSanuKamandaluSutra : LinganushasanaSutra(
    number = "56",
    text = "मद्गुमधुसीधुशीधुसानुकमण्डलूनि नपुंसके च",
    hindiExplanation = "मद्गु, मधु, सीधु, शीधु, सानु और कमण्डलु पुंलिङ्ग तथा नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA),
    priority = 50,
) {
    private val members = setOf("मद्गु", "मधु", "सीधु", "शीधु", "सानु", "कमण्डलु")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

