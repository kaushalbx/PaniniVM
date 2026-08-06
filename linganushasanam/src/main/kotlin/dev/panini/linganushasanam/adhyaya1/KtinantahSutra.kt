package dev.panini.linganushasanam.adhyaya1

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

/**
 * Liṅgānuśāsanam 1.4: क्तिन्नन्ताः.
 * Stems ending in Ktin pratyaya (-ति, -क्तिन्) are feminine.
 */
object KtinantahSutra : LinganushasanaSutra(
    number = "1.4",
    text = "क्तिन्नन्ताः",
    hindiExplanation = "क्तिन् प्रत्ययान्त शब्द स्त्रीलिङ्ग होते हैं (उदा. भक्तिः, गतिः, मतिः)।",
    section = LinganushasanaSection.STRILINGA,
    targetLinga = Linga.STRI,
    priority = 20,
) {
    private val STRI_KTIN_STEMS = setOf("भक्ति", "गति", "मति", "शक्ति", "रात्रि", "नीति", "दीप्ति")

    override fun matches(context: LingaRuleContext): Boolean {
        val stem = context.pratipadika
        if (context.pratyaya == "क्तिन्") return true
        if (stem in STRI_KTIN_STEMS) return true
        return stem.endsWith("ति")
    }
}
