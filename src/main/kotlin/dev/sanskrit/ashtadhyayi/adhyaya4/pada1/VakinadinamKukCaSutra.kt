package dev.sanskrit.ashtadhyayi.adhyaya4.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.DerivationTerm
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.ganapatha.GanaPatha
import dev.sanskrit.shiksha.SemanticFeature
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 4.1.158: वाकिनादीनां कुक् च. */
object VakinadinamKukCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.1.158", text = "वाकिनादीनां कुक् च",
    hindiExplanation = "उदीच्य अपत्य में वाकिनादि शब्दों से विकल्प से फिञ् और कुक् आगम होता है।",
    type = SutraType.VIBHASHA, chapter = 4, pada = 1, optional = true, kramaValue = 410158,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    private fun bases(context: DerivationState) = context.terms.filter {
        it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(68, it.surface, it.lexicalUses)
    }

    override fun matches(context: DerivationState): Boolean =
        SemanticFeature.APATYA in context.semanticFeatures && SemanticFeature.UDICYA in context.semanticFeatures &&
            bases(context).isNotEmpty() && context.allEffectiveTerms.none { it.upadesha == "फिञ्" }

    override fun apply(context: DerivationState): DerivationChange {
        val replacements = bases(context).associate { term -> term.id to term.copy(surface = term.surface + "क") }
        return DerivationChange(
            context.copy(terms = context.terms.map { replacements[it.id] ?: it })
                .addTerm(DerivationTerm("vakinadi-phiny-suffix", "आयनि", TermKind.PRATYAYA, upadesha = "फिञ्"))
                .copy(stage = DerivationStage.PRATYAYA_SELECTED),
            "4.1.158 introduces कुक् and फिञ् after an eligible वाकिनादि term.",
        )
    }
}
