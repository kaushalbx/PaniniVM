package dev.panini.linganushasanam

import dev.panini.core.Linga

/**
 * Executable canonical rule backed by the shared declarative matcher catalog.
 * Explicit derivational provenance remains available for semantic distinctions
 * that a surface form alone cannot establish.
 */
internal class CanonicalDeclarativeSutra(
    number: String,
    text: String,
    section: LinganushasanaSection,
    private val prescribedLingas: Set<Linga>,
    kind: LinganushasanaRuleKind,
) : LinganushasanaSutra(
    number = number,
    text = text,
    hindiExplanation = "सूत्र की संरचित शर्तों अथवा प्रमाणित सूत्र-सन्दर्भ के अनुसार लिङ्ग निर्धारित होता है।",
    section = section,
    targetLinga = prescribedLingas.first(),
    priority = CanonicalRuleMatcher.priorityFor(number),
    kind = kind,
) {
    override val targetLingas: Set<Linga> get() = prescribedLingas

    override fun matches(context: LingaRuleContext): Boolean =
        CanonicalRuleMatcher.matches(number, context)

    override fun apply(context: LingaRuleContext): LingaRuleResult {
        if (number == "126") {
            val linga = if (context.pratipadika.phonologicallyEquals("अहोरात्र")) Linga.NAPUMSAKA else Linga.PUMS
            return LingaRuleResult.Matched(
                linga = linga,
                ruleId = number,
                explanation = "Sūtra $number ($text) matched for '${context.pratipadika}'.",
                lingas = setOf(linga),
            )
        }
        return super.apply(context)
    }
}
