package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.core.LingaSemanticClass
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

/** Liṅgānuśāsana 36: घञबन्तः — ghañ/ap derivatives in bhāva are masculine. */
object GhanAbantahSutra : LinganushasanaSutra(
    number = "36",
    text = "घञबन्तः",
    hindiExplanation = "भावार्थ में घञ् अथवा अप् प्रत्ययान्त शब्द पुंलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLinga = Linga.PUMS,
    priority = 20,
) {
    override fun matches(context: LingaRuleContext): Boolean =
        context.pratyaya in setOf("घञ्", "अप्") && LingaSemanticClass.BHAVA in context.semanticClasses
}
