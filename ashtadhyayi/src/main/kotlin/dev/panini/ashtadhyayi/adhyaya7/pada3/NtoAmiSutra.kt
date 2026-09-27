package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.core.Linga
import dev.panini.core.SupAffix
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.matchesSupAffix
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** Masculine n-stem before am: form the strong ānam accusative singular. */
object NtoAmiSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.140",
    text = "नतोऽमि",
    hindiExplanation = "पुंलिङ्ग नकारान्त में द्वितीया एकवचन अम् के परे आनम् रूप होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730140,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2 || context.effectiveContext.rupa.linga != Linga.PUMS ||
            context.effectiveContext.rupa.vibhakti != Vibhakti.DVITIYA ||
            context.effectiveContext.rupa.vacana != Vacana.EKAVACANA
        ) return false
        val stem = context.terms[context.terms.size - 2]
        return stem.isNFinal() && context.terms.last().matchesSupAffix(SupAffix.AM)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        return DerivationChange(
            state = context.mergeTermsByVarnaSubstitution(
                stem.id, affix.id,
                stem.replaceFinalAn(listOf(Svara.AA, Vyanjana.NA, Svara.A, Vyanjana.MA)),
                Vyanjana.NA, listOf(Svara.AA, Vyanjana.NA, Svara.A, Vyanjana.MA), sutra,
            ).copy(stage = DerivationStage.FINAL),
            explanation = "7.3.140: Formed the masculine n-stem accusative-singular आनम् ending before अम्.",
        )
    }
}
