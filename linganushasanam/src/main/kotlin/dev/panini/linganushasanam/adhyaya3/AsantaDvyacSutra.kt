package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.endsWithVarnas

/** Liṅgānuśāsanam 154: असन्तो द्व्यच्कः । */
object AsantaDvyacSutra : LinganushasanaSutra(
    number = "154", text = "असन्तो द्व्यच्कः ।", hindiExplanation = "अस्-अन्त द्व्यक्षर शब्द नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.NAPUMSAKA, priority = 10,
) {
    override fun matches(context: LingaRuleContext): Boolean =
        context.pratipadika.endsWithVarnas("अस्") && context.syllableCount == 2
}
