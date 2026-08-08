package dev.panini.ashtadhyayi.adhyaya3.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 3.3.115 ल्युट् च. Introduces LYUṬ in the inherited action-noun domain. */
object LyutCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.3.115",
    text = "ल्युट् च",
    hindiExplanation = "भावार्थ में धातु से ल्युट् प्रत्यय भी होता है।",
    type = SutraType.UTSARGA,
    chapter = 3,
    pada = 3,
    optional = false,
    kramaValue = 330115,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage != DerivationStage.INITIAL && context.stage != DerivationStage.PRATYAYA_SELECTED) return false
        val isLyutRequested = context.samjnas.any { it.samjna == Samjna.LYUT }
        val hasPratyaya = context.terms.any { it.kind == TermKind.PRATYAYA }
        return isLyutRequested && !hasPratyaya
    }

    override fun apply(context: DerivationState): DerivationChange {
        val lyut = DerivationTerm(
            id = "lyut_pratyaya",
            surface = "अन",
            kind = TermKind.PRATYAYA,
            upadesha = "ल्युट्",
            createdBySutra = sutra,
        )
        return DerivationChange(
            state = context.copy(
                terms = context.terms + lyut,
                stage = DerivationStage.PRATYAYA_SELECTED,
            ),
            explanation = "3.3.115 introduces ल्युट् (अन) as an action-noun suffix.",
        )
    }
}
