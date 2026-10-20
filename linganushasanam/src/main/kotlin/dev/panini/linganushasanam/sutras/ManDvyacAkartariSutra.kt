package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.core.LingaSemanticClass
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.endsWithVarnas

/** Liṅgānuśāsanam 149: मन् द्व्यच्कोऽकर्त्तरि । */
object ManDvyacAkartariSutra : LinganushasanaSutra(
    number = "149", text = "मन्द्व्यच्कोऽकर्तरि", hindiExplanation = "मन्-अन्त द्व्यक्षर शब्द, कर्तृवाचक न होने पर, नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLingas = setOf(Linga.NAPUMSAKA), priority = 10,
) {
    override fun matches(context: LingaRuleContext): Boolean =
        context.pratipadika.endsWithVarnas("मन्") && context.syllableCount == 2 &&
            LingaSemanticClass.ACTION_AGENT !in context.semanticClasses
}
