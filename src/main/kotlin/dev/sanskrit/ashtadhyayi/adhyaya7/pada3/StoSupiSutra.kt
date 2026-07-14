package dev.sanskrit.ashtadhyayi.adhyaya7.pada3

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.Vacana
import dev.sanskrit.derivation.Vibhakti
import dev.sanskrit.shiksha.Linga
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

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
                droppedTerms = context.droppedTerms + affix.copy(surface = ""),
                stage = DerivationStage.FINAL,
            ),
            explanation = "7.3.158: Formed the neuter s-stem locative-plural ःसु ending before सुप्.",
        )
    }
}
