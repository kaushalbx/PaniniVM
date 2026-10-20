package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.core.LingaSemanticClass
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyEquals

/** Liṅgānuśāsanam 15: नाभिरक्षत्रिये. */
object NabhiAksatriyeSutra : LinganushasanaSutra(
    number = "15",
    text = "नाभिरक्षत्रिये",
    hindiExplanation = "क्षत्रिय से भिन्न अर्थ में नाभि स्त्रीलिङ्ग होता है।",
    section = LinganushasanaSection.STRILINGA,
    targetLingas = setOf(Linga.STRI),
    priority = 40,
) {
    override fun matches(context: LingaRuleContext): Boolean =
        context.pratipadika.phonologicallyEquals("नाभि") &&
            LingaSemanticClass.NON_KSHATRIYA in context.semanticClasses
}
