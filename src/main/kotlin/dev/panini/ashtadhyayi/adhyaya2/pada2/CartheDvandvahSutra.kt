package dev.panini.ashtadhyayi.adhyaya2.pada2

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.SamjnaAssignment
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 2.2.29: चार्थे द्वन्द्वः.
 * Prescribes Dvandva compound formation between multiple subanta terms connected in 'ca' (and) sense.
 */
object CartheDvandvahSutra : Sutra<DerivationState, DerivationChange>(
    number = "2.2.29",
    text = "चार्थे द्वन्द्वः",
    hindiExplanation = "'च' (और) के अर्थ में स्थित अनेक समर्थ सुबन्तों का द्वन्द्व समास होता है (उदा. रामश्च कृष्णश्च = रामकृष्णौ)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220029,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
), DerivationSutra {

    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val nominals = context.terms.filter { it.kind == TermKind.PRATIPADIKA }
        return nominals.size >= 2 && context.allEffectiveTerms.none { it.id == "samasa_dvandva" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val nominals = context.terms.filter { it.kind == TermKind.PRATIPADIKA }
        val first = nominals[0]
        val second = nominals[1]
        val compoundSurface = first.surface + second.surface
        val compoundTerm = DerivationTerm("samasa_dvandva", compoundSurface, TermKind.PRATIPADIKA, upadesha = compoundSurface)

        val newState = context.replaceTerm(first.id, compoundTerm).removeTerm(second.id, sutra)
        return DerivationChange(
            state = newState.withSamjnas(setOf(SamjnaAssignment(compoundTerm.id, Samjna.PRATIPADIKA))),
            explanation = "2.2.29 forms Dvandva compound '${compoundSurface}'."
        )
    }
}
