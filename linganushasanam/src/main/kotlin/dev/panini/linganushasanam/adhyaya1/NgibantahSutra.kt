package dev.panini.linganushasanam.adhyaya1

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

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
