package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

/**
 * Liṅgānuśāsanam 2.1: पुंसि.
 * Adhikāra sūtra for masculine gender rules.
 */
object PumsiSutra : LinganushasanaSutra(
    number = "2.1",
    text = "पुंसि",
    hindiExplanation = "यहाँ से आगे पुंलिङ्ग का अधिकार चलता है।",
    section = LinganushasanaSection.PUMLINGA,
    targetLinga = Linga.PUMS,
    priority = 1,
) {
    override fun matches(context: LingaRuleContext): Boolean = false
}
