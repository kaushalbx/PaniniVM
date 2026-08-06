package dev.panini.linganushasanam.adhyaya5

import dev.panini.core.Linga
import dev.panini.core.SamasaType
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

/**
 * Liṅgānuśāsanam 5.2: स नपुंसकम् (Aṣṭādhyāyī 2.4.17).
 * Avyayībhāva and Dvigu compounds are fixed to Neuter.
 */
object SaNapumsakamSutra : LinganushasanaSutra(
    number = "5.2",
    text = "स नपुंसकम्",
    hindiExplanation = "अव्ययीभाव और द्विगु समास नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.SAMASALINGA,
    targetLinga = Linga.NAPUMSAKA,
    priority = 40,
) {
    override fun matches(context: LingaRuleContext): Boolean {
        return context.samasaType == SamasaType.AVYAYIBHAVA || context.samasaType == SamasaType.DVIGU
    }
}
