package dev.panini.ashtadhyayi.adhyaya4.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationalEnvironment
import dev.panini.derivation.DerivationalMeaning
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.HasDerivationalEnvironment
import dev.panini.derivation.HasRequestedMeaning
import dev.panini.derivation.TermKind
import dev.panini.ganapatha.GanaPatha
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

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
        HasRequestedMeaning(DerivationalMeaning.APATYA).matches(context) &&
            HasDerivationalEnvironment(DerivationalEnvironment.UDICYA).matches(context) &&
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
