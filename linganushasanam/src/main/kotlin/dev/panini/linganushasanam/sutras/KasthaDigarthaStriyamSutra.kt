package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.core.LingaSemanticClass
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyEquals

/** Liṅgānuśāsanam 72: काष्ठा दिगर्था स्त्रियाम्. */
object KasthaDigarthaStriyamSutra : LinganushasanaSutra(
    number = "72", text = "काष्ठा दिगर्था स्त्रियाम्",
    hindiExplanation = "दिशा के अर्थ में काष्ठा स्त्रीलिङ्ग होती है।",
    section = LinganushasanaSection.PUMLINGA, targetLingas = setOf(Linga.STRI), priority = 40,
) {
    override fun matches(context: LingaRuleContext): Boolean =
        context.pratipadika.phonologicallyEquals("काष्ठा") && LingaSemanticClass.DIRECTION in context.semanticClasses
}
