package dev.panini.ashtadhyayi.adhyaya7.pada2

import dev.panini.core.Linga
import dev.panini.core.SupAffix
import dev.panini.derivation.hasCurrentAffix
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 7.2.111: ido 'y puṃsi. Replaces the id portion, not the whole idam stem.
 */
object IdoAyamSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.111",
    text = "इदोऽय् पुंसि",
    hindiExplanation = "पुँल्लिङ्ग में सुँ परे होने पर इदम् के इद् भाग को अय् आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 2,
    optional = false,
    kramaValue = 720111,
    role = SutraRole.Apavada,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    nimittaScope = NimittaScope.BOTH,
    dependencies = setOf("6.4.1"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        val isIdam = stem.upadesha == "इदम्" &&
            stem.varnas.take(2) == listOf(Svara.I, Vyanjana.DA) &&
            stem.varnas.lastOrNull() == Vyanjana.MA
        val isMasculine = context.effectiveContext.rupa.linga == Linga.PUMS
        return isIdam && isMasculine && affix.sourceSupAffix == SupAffix.SU && affix.hasCurrentAffix(SupAffix.SU)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]

        return DerivationChange(
            state = context.replaceTermVarnaRange(stem.id, 0, 2,
                listOf(Svara.A, Vyanjana.YA), mapOf(0 to 0, 1 to 1), sutra)
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.2.111: Replaced 'idam' with 'ayam' before nominative singular 'su'."
        )
    }
}
