package dev.panini.linganushasanam.adhyaya1

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaRuleKind
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

/** Liṅgānuśāsanam 2: स्त्री — begins the feminine section. */
object StriAdhikaraSutra : LinganushasanaSutra(
    number = "2",
    text = "स्त्री ।",
    hindiExplanation = "इस अधिकार के अन्तर्गत आने वाले शब्द स्त्रीलिङ्ग होते हैं।",
    section = LinganushasanaSection.STRILINGA,
    targetLinga = Linga.STRI,
    kind = LinganushasanaRuleKind.ADHIKARA,
) {
    override fun matches(context: LingaRuleContext): Boolean = false
}
