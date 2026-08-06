package dev.panini.linganushasanam

import dev.panini.core.Linga

/**
 * Abstract base class for individual Pāṇinian Liṅgānuśāsana Sūtras.
 *
 * @property number Canonical Sūtra number in Liṅgānuśāsanam (e.g. "1.1", "3.2", "5.1").
 * @property text Devanagari text of the Sūtra (e.g. "ल्युडाद्यन्तः").
 * @property hindiExplanation Traditional Hindi commentary/explanation.
 * @property section Section/Adhyāya of Liṅgānuśāsanam.
 * @property targetLinga The target [Linga] assigned by this Sūtra.
 * @property priority Priority value for evaluation order.
 */
abstract class LinganushasanaSutra(
    val number: String,
    val text: String,
    val hindiExplanation: String,
    val section: LinganushasanaSection,
    val targetLinga: Linga,
    override val priority: Int = 10,
) : LinganushasanamRule {

    override val ruleId: String get() = number
    override val description: String get() = "$number ($text): $hindiExplanation"

    override fun apply(context: LingaRuleContext): LingaRuleResult {
        return LingaRuleResult.Matched(
            linga = targetLinga,
            ruleId = number,
            explanation = "Sūtra $number ($text) matched for '${context.pratipadika}'.",
        )
    }
}
