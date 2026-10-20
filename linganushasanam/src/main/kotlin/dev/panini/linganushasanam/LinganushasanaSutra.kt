package dev.panini.linganushasanam

import dev.panini.core.Linga

/**
 * Abstract base class for individual Pāṇinian Liṅgānuśāsana Sūtras.
 *
 * @property number Sūtra number in the Ashtadhyayi.com Liṅgānuśāsanam recension (1–189).
 * @property text Devanagari text of the Sūtra (e.g. "ल्युडाद्यन्तः").
 * @property hindiExplanation Traditional Hindi commentary/explanation.
 * @property section Section/Adhyāya of Liṅgānuśāsanam.
 * @property targetLingas The complete set of [Linga] values prescribed by this Sūtra.
 * @property priority Priority value for evaluation order.
 */
abstract class LinganushasanaSutra(
    val number: String,
    val text: String,
    val hindiExplanation: String,
    val section: LinganushasanaSection,
    val targetLingas: Set<Linga>,
    override val priority: Int = 10,
    val kind: LinganushasanaRuleKind = LinganushasanaRuleKind.VIDHI,
) : LinganushasanamRule {

    init {
        require(targetLingas.isNotEmpty()) { "Sūtra $number must prescribe at least one liṅga." }
    }

    override val ruleId: String get() = number
    override val description: String get() = "$number ($text): $hindiExplanation"

    override fun apply(context: LingaRuleContext): LingaRuleResult {
        val resolvedLinga = context.governingLinga?.takeIf { it in targetLingas } ?: targetLingas.first()
        return LingaRuleResult.Matched(
            linga = resolvedLinga,
            ruleId = number,
            explanation = "Sūtra $number ($text) matched for '${context.pratipadika}'.",
            lingas = targetLingas,
        )
    }
}
