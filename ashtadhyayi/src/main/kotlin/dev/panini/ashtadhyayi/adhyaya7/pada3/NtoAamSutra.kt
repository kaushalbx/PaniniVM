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

/** Masculine n-stem before ām: form the nām genitive plural. */
object NtoAamSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.146",
    text = "नत आमि",
    hindiExplanation = "पुंलिङ्ग नकारान्त में षष्ठी बहुवचन आम् के परे नाम् रूप होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730146,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2 || context.effectiveContext.rupa.linga != Linga.PUMS ||
            context.effectiveContext.rupa.vibhakti != Vibhakti.SASTHI ||
            context.effectiveContext.rupa.vacana != Vacana.BAHUVACANA
        ) return false
        val stem = context.terms[context.terms.size - 2]
        return stem.isNFinal() && context.terms.last().matchesSupAffix(SupAffix.AM_6)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        val shatNumerals = setOf("पञ्चन्", "षट्", "सप्तन्", "अष्टन्", "नवन्", "दशन्")
        val replacement = if (stem.upadesha in shatNumerals) {
            listOf(Svara.AA, Vyanjana.NA, Svara.AA, Vyanjana.MA)
        } else {
            listOf(Vyanjana.NA, Svara.AA, Vyanjana.MA)
        }
        val newSurface = if (stem.upadesha in shatNumerals) {
            stem.replaceFinalAn(replacement)
        } else {
            stem.extendFinalN(listOf(Svara.AA, Vyanjana.MA))
        }
        return DerivationChange(
            state = context.mergeTermsByVarnaSubstitution(
                stem.id, affix.id, newSurface, Vyanjana.NA, replacement, sutra,
            ).copy(stage = DerivationStage.FINAL),
            explanation = "7.3.146: Formed the masculine n-stem genitive-plural नाम् ending before आम्.",
        )
    }
}
