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

/** Masculine n-stem before os: form the noḥ genitive/locative dual. */
object NtoOsiSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.145",
    text = "नत ओसि",
    hindiExplanation = "पुंलिङ्ग नकारान्त में षष्ठी-सप्तमी द्विवचन ओस् के परे नोः रूप होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730145,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2 || context.effectiveContext.rupa.linga != Linga.PUMS ||
            context.effectiveContext.rupa.vacana != Vacana.DVIVACANA ||
            context.effectiveContext.rupa.vibhakti !in setOf(Vibhakti.SASTHI, Vibhakti.SAPTAMI)
        ) return false
        val stem = context.terms[context.terms.size - 2]
        return stem.surface.endsWith("न्") && context.terms.last().upadesha == "ओस्"
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        return DerivationChange(
            state = context.copy(
                terms = context.terms.dropLast(2) + stem.copy(surface = stem.surface.dropLast(1) + "ोः"),
                droppedTerms = context.droppedTerms + affix.copy(surface = ""),
                stage = DerivationStage.FINAL,
            ),
            explanation = "7.3.145: Formed the masculine n-stem dual नोः ending before ओस्.",
        )
    }
}
