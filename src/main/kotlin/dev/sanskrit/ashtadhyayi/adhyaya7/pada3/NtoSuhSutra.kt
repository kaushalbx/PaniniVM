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

/** Masculine n-stem before su: form the nominative singular ā ending. */
object NtoSuhSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.137",
    text = "नतः सुः",
    hindiExplanation = "पुंलिङ्ग नकारान्त में प्रथमा एकवचन सु के परे आादेश होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730137,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2 ||
            context.effectiveContext.rupa.linga != Linga.PUMS ||
            context.effectiveContext.rupa.vibhakti != Vibhakti.PRATHAMA ||
            context.effectiveContext.rupa.vacana != Vacana.EKAVACANA
        ) return false
        val stem = context.terms[context.terms.size - 2]
        return stem.surface.endsWith("न्") && context.terms.last().upadesha == "सुँ"
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        return DerivationChange(
            state = context.copy(
                terms = context.terms.dropLast(2) + stem.copy(surface = stem.surface.dropLast(2) + "ा"),
                droppedTerms = context.droppedTerms + affix.copy(surface = ""),
                stage = DerivationStage.FINAL,
            ),
            explanation = "7.3.137: Formed the masculine n-stem nominative-singular आ ending before सु.",
        )
    }
}
