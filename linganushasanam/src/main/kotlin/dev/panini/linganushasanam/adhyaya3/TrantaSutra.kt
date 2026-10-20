package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.endsWithVarnas

/** Liṅgānuśāsanam 156: त्रान्तः । */
object TrantaSutra : LinganushasanaSutra(
    number = "156", text = "त्रान्तः ।", hindiExplanation = "त्र-अन्त शब्द नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.NAPUMSAKA, priority = 10,
) {
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.endsWithVarnas("त्र")
}
