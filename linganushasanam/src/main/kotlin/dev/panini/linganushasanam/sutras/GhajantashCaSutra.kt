package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

/** Liṅgānuśāsana 37: घाजन्तश्च; examples include caya and jaya. */
object GhajantashCaSutra : LinganushasanaSutra(
    number = "37",
    text = "घाजन्तश्च",
    hindiExplanation = "घ अथवा अच् प्रत्ययान्त शब्द पुंलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLingas = setOf(Linga.PUMS),
    priority = 20,
) {
    override fun matches(context: LingaRuleContext): Boolean = context.pratyaya in setOf("घ", "अच्")
}
