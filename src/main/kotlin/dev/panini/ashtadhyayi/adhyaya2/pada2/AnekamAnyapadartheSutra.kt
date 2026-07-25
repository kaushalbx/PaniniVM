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
 * Sūtra 2.2.24: अनेकमन्यपदार्थे.
 * Prescribes Bahuvrīhi compound formation when the principal meaning is outside the compound (anyapadārtha).
 */
object AnekamAnyapadartheSutra : Sutra<DerivationState, DerivationChange>(
    number = "2.2.24",
    text = "अनेकमन्यपदार्थे",
    hindiExplanation = "अन्य पद का अर्थ प्रधान होने पर प्रथमान्त समर्थ सुबन्तों का बहुव्रीहि समास होता है (उदा. पीताम्बरः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220024,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
), DerivationSutra {

    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val nominals = context.terms.filter { it.kind == TermKind.PRATIPADIKA }
        return nominals.size >= 2 && context.allEffectiveTerms.none { it.id == "samasa_bahuvrihi" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val nominals = context.terms.filter { it.kind == TermKind.PRATIPADIKA }
        val first = nominals[0]
        val second = nominals[1]
        val compoundSurface = first.surface + second.surface
        val compoundTerm = DerivationTerm("samasa_bahuvrihi", compoundSurface, TermKind.PRATIPADIKA, upadesha = compoundSurface)

        val newState = context.replaceTerm(first.id, compoundTerm).removeTerm(second.id, sutra)
        return DerivationChange(
            state = newState.withSamjnas(setOf(SamjnaAssignment(compoundTerm.id, Samjna.PRATIPADIKA))),
            explanation = "2.2.24 forms Bahuvrīhi compound '${compoundSurface}'."
        )
    }
}
