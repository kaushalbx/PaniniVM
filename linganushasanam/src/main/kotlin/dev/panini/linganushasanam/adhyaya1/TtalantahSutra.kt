package dev.panini.linganushasanam.adhyaya1

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

/**
 * Liṅgānuśāsanam 1.6: तल्-ता-अन्ताः.
 * Abstract nouns ending in Taddhita pratyaya Tal (-तल्, -ता) are feminine.
 */
object TtalantahSutra : LinganushasanaSutra(
    number = "1.6",
    text = "तल्-ता-अन्ताः",
    hindiExplanation = "तल् (ता) प्रत्ययान्त भाववाचक शब्द स्त्रीलिङ्ग होते हैं (उदा. देवता, सुलभता, सुन्दरता, जनहितैषिता)।",
    section = LinganushasanaSection.STRILINGA,
    targetLinga = Linga.STRI,
    priority = 25,
) {
    override fun matches(context: LingaRuleContext): Boolean {
        val stem = context.pratipadika
        if (context.pratyaya in setOf("तल्", "ता")) return true
        return stem.endsWith("ता") || stem.endsWith("ताम्")
    }
}
