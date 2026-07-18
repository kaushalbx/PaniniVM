package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.Vacana
import dev.panini.derivation.Vibhakti
import dev.panini.shiksha.Linga
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** Masculine ṛ-stem before singular ṅasi/ṅas: form the uḥ ending. */
object RtoNgasyosSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.135",
    text = "ऋतो ङसिङसोः",
    hindiExplanation = "पुंलिङ्ग ऋकारान्त में एकवचन ङसि और ङस् के परे उः रूप होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730135,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2 ||
            context.effectiveContext.rupa.linga != Linga.PUMS ||
            context.effectiveContext.rupa.vacana != Vacana.EKAVACANA ||
            context.effectiveContext.rupa.vibhakti !in setOf(Vibhakti.PANCHAMI, Vibhakti.SASTHI)
        ) return false
        val stem = context.terms[context.terms.size - 2]
        return stem.surface.lastOrNull() in setOf('ऋ', 'ृ') && context.terms.last().upadesha in setOf("ङसि", "ङस्")
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        return DerivationChange(
            state = context.copy(
                terms = context.terms.dropLast(2) + stem.copy(surface = stem.surface.dropLast(1) + "ुः"),
                droppedTerms = context.droppedTerms + affix.copy(surface = ""),
                stage = DerivationStage.FINAL,
            ),
            explanation = "7.3.135: Formed the masculine ṛ-stem singular उः ending before ${affix.upadesha}.",
        )
    }
}
