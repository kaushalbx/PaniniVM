package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

/**
 * Liṅgānuśāsanam 2.2: घञन्ताः.
 * Stems ending in Ghañ pratyaya (-घञ्) are Masculine.
 */
object GhajantahSutra : LinganushasanaSutra(
    number = "2.2",
    text = "घञन्ताः",
    hindiExplanation = "घञ् प्रत्ययान्त शब्द पुंलिङ्ग होते हैं (उदा. पाकः, भावः, रागः, कामः, योगः)।",
    section = LinganushasanaSection.PUMLINGA,
    targetLinga = Linga.PUMS,
    priority = 20,
) {
    private val GHAJ_STEMS = setOf("पाक", "भाव", "राग", "काम", "योग", "त्याग", "भोग", "लाभ")

    override fun matches(context: LingaRuleContext): Boolean {
        val stem = context.pratipadika
        if (context.pratyaya == "घञ्") return true
        return stem in GHAJ_STEMS
    }
}
