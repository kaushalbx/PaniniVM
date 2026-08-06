package dev.panini.linganushasanam.adhyaya1

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

/**
 * Liṅgānuśāsanam 1.5: ऊजन्ताः.
 * Stems ending in Ūṅ pratyaya (-ऊ) are feminine.
 */
object UjantahSutra : LinganushasanaSutra(
    number = "1.5",
    text = "ऊजन्ताः",
    hindiExplanation = "ऊङ्-प्रत्ययान्त (ऊ-कारान्त) शब्द स्त्रीलिङ्ग होते हैं (उदा. वधूः, चमूः, पुनर्भूः)।",
    section = LinganushasanaSection.STRILINGA,
    targetLinga = Linga.STRI,
    priority = 20,
) {
    private val UNG_STEMS = setOf("वधू", "चमू", "पुनर्भू", "श्वश्रू", "सरयू", "भ्रू")

    override fun matches(context: LingaRuleContext): Boolean {
        val stem = context.pratipadika
        if (context.pratyaya == "ऊङ्") return true
        if (stem in UNG_STEMS) return true
        return stem.endsWith("ऊ") || stem.endsWith("ू")
    }
}
