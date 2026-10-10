package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.core.SupAffix
import dev.panini.derivation.hasCurrentAffix

/**
 * 7.3.104: osi ca.
 * Substitutes 'e' for the final 'a' of an aṅga before the dual affix 'os'.
 */
object OsiCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.104",
    text = "ओसि च",
    hindiExplanation = "ओस् परे होने पर अकारान्त अङ्ग के अन्त्य अकार का एकार होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730104,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    dependencies = setOf("6.4.1")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage == DerivationStage.INITIAL || context.stage == DerivationStage.PRATYAYA_SELECTED) return false
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        return stem.varnas.lastOrNull() == Svara.A &&
            affix.hasCurrentAffix(SupAffix.OS_6) &&
            affix.varnas == listOf(Svara.O, Vyanjana.SA)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]

        return DerivationChange(
            state = context.replaceTermVarna(stem.id, stem.varnas.lastIndex, listOf(Svara.E), sutra)
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.3.104: Substituted 'e' for final 'a' before 'os'."
        )
    }
}
