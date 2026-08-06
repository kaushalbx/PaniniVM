package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

/**
 * Liṅgānuśāsanam 3.1: नपुंसके.
 * Adhikāra sūtra for neuter gender rules.
 */
object NapumsakeSutra : LinganushasanaSutra(
    number = "3.1",
    text = "नपुंसके",
    hindiExplanation = "यहाँ से आगे नपुंसकलिङ्ग का अधिकार चलता है।",
    section = LinganushasanaSection.NAPUMSAKALINGA,
    targetLinga = Linga.NAPUMSAKA,
    priority = 1,
) {
    override fun matches(context: LingaRuleContext): Boolean = false
}
