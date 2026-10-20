package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.core.LingaSemanticClass
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyEquals

/** Liṅgānuśāsanam 55: वसु चार्थवाचि. */
object VasuArthavaciSutra : LinganushasanaSutra(
    number = "55",
    text = "वसु चार्थवाचि ।",
    hindiExplanation = "धन अथवा अर्थ के वाचक वसु शब्द का नपुंसकलिङ्ग विधान होता है।",
    section = LinganushasanaSection.PUMLINGA,
    targetLinga = Linga.NAPUMSAKA,
    priority = 40,
) {
    override fun matches(context: LingaRuleContext): Boolean =
        context.pratipadika.phonologicallyEquals("वसु") &&
            LingaSemanticClass.ARTHA_WEALTH in context.semanticClasses
}
