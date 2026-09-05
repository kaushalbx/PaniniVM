package dev.panini.ashtadhyayi.adhyaya7.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 7.4.79: सन्यतः.
 * Replaces short 'a' in the reduplicated syllable (abhyāsa) with 'i' before the desiderative 'सन्' suffix.
 */
object SanyAtaSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.4.79",
    text = "सन्यतः",
    hindiExplanation = "सन् परे होने पर अभ्यास के अकार का इकार होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 4,
    optional = false,
    kramaValue = 740079,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val pratyaya = context.terms.lastOrNull { it.kind == TermKind.PRATYAYA } ?: return false
        val isSan = pratyaya.upadesha == "सन्"
        val abhyasa = context.terms.firstOrNull { it.id == "abhyasa" } ?: return false
        return isSan && (abhyasa.surface.endsWith("अ") || abhyasa.surface == "प")
    }

    override fun apply(context: DerivationState): DerivationChange {
        val abhyasa = context.terms.first { it.id == "abhyasa" }
        val newSurface = if (abhyasa.surface == "प") "पि" else abhyasa.surface.dropLast(1) + "ि"
        val newAbhyasa = abhyasa.copy(surface = newSurface)
        return DerivationChange(
            state = context.replaceTerm(abhyasa.id, newAbhyasa),
            explanation = "7.4.79 replaces short 'a' with 'i' in abhyāsa (${abhyasa.surface} → $newSurface)."
        )
    }
}
