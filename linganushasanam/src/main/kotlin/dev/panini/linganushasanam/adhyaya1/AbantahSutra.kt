package dev.panini.linganushasanam.adhyaya1

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.endsWithVarnas
import dev.panini.linganushasanam.phonologicallyIn

/**
 * Liṅgānuśāsanam 1.2: आबन्ताः.
 * Stems ending in Āp pratyaya (-टाप्, -डाप्, -चाप्) or feminine -आ are feminine.
 */
object AbantahSutra : LinganushasanaSutra(
    number = "1.2",
    text = "आबन्ताः",
    hindiExplanation = "आबन्त (आ-प्रत्ययान्त) शब्द स्त्रीलिङ्ग होते हैं (उदा. माला, गङ्गा, लता)।",
    section = LinganushasanaSection.STRILINGA,
    targetLinga = Linga.STRI,
    priority = 20,
) {
    private val STRI_STEMS = setOf("गङ्गा", "पूर्णिमा", "प्रज्ञा", "सभा", "शाला", "सेना", "माला", "लता", "विद्या")

    override fun matches(context: LingaRuleContext): Boolean {
        val stem = context.pratipadika
        if (context.pratyaya in setOf("टाप्", "डाप्", "चाप्", "आप्")) return true
        if (stem.phonologicallyIn(STRI_STEMS)) return true
        return stem.endsWithVarnas("आ")
    }
}
