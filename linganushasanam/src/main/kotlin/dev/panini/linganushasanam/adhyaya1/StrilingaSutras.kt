package dev.panini.linganushasanam.adhyaya1

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

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
        if (stem in STRI_STEMS) return true
        return stem.endsWith("आ") || stem.endsWith("ा")
    }
}

/**
 * Liṅgānuśāsanam 1.3: ङीबन्ताः.
 * Stems ending in Ṅīp/Ṅīṣ/Ṅīn pratyayas (-ई) are feminine.
 */
object NgibantahSutra : LinganushasanaSutra(
    number = "1.3",
    text = "ङीबन्ताः",
    hindiExplanation = "ङीप्, ङीष् आदि ई-प्रत्ययान्त शब्द स्त्रीलिङ्ग होते हैं (उदा. कुमारी, नदी, नवमी)।",
    section = LinganushasanaSection.STRILINGA,
    targetLinga = Linga.STRI,
    priority = 20,
) {
    override fun matches(context: LingaRuleContext): Boolean {
        val stem = context.pratipadika
        if (context.pratyaya in setOf("ङीप्", "ङीष्", "ङीन्")) return true
        return stem.endsWith("ई") || stem.endsWith("ी")
    }
}

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
