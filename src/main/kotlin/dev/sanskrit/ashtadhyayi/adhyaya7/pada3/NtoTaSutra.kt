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

/** Masculine n-stem before ṭā: form the nā instrumental singular. */
object NtoTaSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.142",
    text = "नत टा",
    hindiExplanation = "पुंलिङ्ग नकारान्त में तृतीया एकवचन टा के परे ना रूप होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730142,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2 || context.effectiveContext.rupa.linga != Linga.PUMS ||
            context.effectiveContext.rupa.vibhakti != Vibhakti.TRTIYA ||
            context.effectiveContext.rupa.vacana != Vacana.EKAVACANA
        ) return false
        val stem = context.terms[context.terms.size - 2]
        return stem.surface.endsWith("न्") && context.terms.last().upadesha == "टा"
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        return DerivationChange(
            state = context.copy(
                terms = context.terms.dropLast(2) + stem.copy(surface = stem.surface.dropLast(1) + "ा"),
                droppedTerms = context.droppedTerms + affix.copy(surface = ""),
                stage = DerivationStage.FINAL,
            ),
            explanation = "7.3.142: Formed the masculine n-stem instrumental-singular ना ending before टा.",
        )
    }
}
