package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.core.Linga
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** Neuter s-stem before sup: form the ḥsu locative plural. */
object StoSupiSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.158",
    text = "सतः सुपि",
    hindiExplanation = "नपुंसकलिङ्ग सकारान्त में सप्तमी बहुवचन सुप् के परे ःसु रूप होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730158,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2 || context.effectiveContext.rupa.linga != Linga.NAPUMSAKA ||
            context.effectiveContext.rupa.vibhakti != Vibhakti.SAPTAMI ||
            context.effectiveContext.rupa.vacana != Vacana.BAHUVACANA
        ) return false
        return context.terms[context.terms.size - 2].surface.endsWith("स्") && context.terms.last().upadesha == "सुप्"
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        return DerivationChange(
            state = context.copy(
                terms = context.terms.dropLast(2) + stem.copy(surface = stem.surface.dropLast(2) + "ःसु"),
                droppedTerms = context.droppedTerms + dev.panini.derivation.consumeAffixForDrop(affix, sutra),
                stage = DerivationStage.FINAL,
            ),
            explanation = "7.3.158: Formed the neuter s-stem locative-plural ःसु ending before सुप्.",
        )
    }
}
