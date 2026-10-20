package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.Linga
import dev.panini.core.LingaSemanticClass
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.endsWithVarnas

/** Liṅgānuśāsanam 151: मन् द्व्यच्कोऽकर्त्तरि । */
object ManDvyacAkartariSutra : LinganushasanaSutra(
    number = "151", text = "मन् द्व्यच्कोऽकर्त्तरि ।", hindiExplanation = "मन्-अन्त द्व्यक्षर शब्द, कर्तृवाचक न होने पर, नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.NAPUMSAKA, priority = 10,
) {
    override fun matches(context: LingaRuleContext): Boolean =
        context.pratipadika.endsWithVarnas("मन्") && context.syllableCount == 2 &&
            LingaSemanticClass.ACTION_AGENT !in context.semanticClasses
}
