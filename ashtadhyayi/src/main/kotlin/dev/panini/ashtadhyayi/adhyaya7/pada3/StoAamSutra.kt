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

/** Neuter s-stem before ām: form the sām genitive plural. */
object StoAamSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.156",
    text = "सत आमि",
    hindiExplanation = "नपुंसकलिङ्ग सकारान्त में षष्ठी बहुवचन आम् के परे साम् रूप होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730156,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2 || context.effectiveContext.rupa.linga != Linga.NAPUMSAKA ||
            context.effectiveContext.rupa.vibhakti != Vibhakti.SASTHI ||
            context.effectiveContext.rupa.vacana != Vacana.BAHUVACANA
        ) return false
        return context.terms[context.terms.size - 2].surface.endsWith("स्") && context.terms.last().upadesha == "आम्"
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        return DerivationChange(
            state = context.copy(
                terms = context.terms.dropLast(2) + stem.copy(surface = stem.surface.dropLast(1) + "ाम्"),
                droppedTerms = context.droppedTerms + affix.copy(surface = ""),
                stage = DerivationStage.FINAL,
            ),
            explanation = "7.3.156: Formed the neuter s-stem genitive-plural साम् ending before आम्.",
        )
    }
}
