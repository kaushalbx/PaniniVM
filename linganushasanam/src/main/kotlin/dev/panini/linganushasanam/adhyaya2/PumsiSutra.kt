package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaRuleKind
import dev.panini.linganushasanam.LinganushasanaSutra

/**
 * Liṅgānuśāsanam 35: पुमान्.
 * Adhikāra sūtra for masculine gender rules.
 */
object PumsiSutra : LinganushasanaSutra(
    number = "35",
    text = "पुमान्",
    hindiExplanation = "यहाँ से आगे पुंलिङ्ग का अधिकार चलता है।",
    section = LinganushasanaSection.PUMLINGA,
    targetLinga = Linga.PUMS,
    priority = 1,
    kind = LinganushasanaRuleKind.ADHIKARA,
) {
    override fun matches(context: LingaRuleContext): Boolean = false
}
