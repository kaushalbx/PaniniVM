package dev.panini.ashtadhyayi.adhyaya6.pada4

import dev.panini.core.Lakara
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 6.4.119: घ्वसोरेद्धावभ्यासलोपश्च. */
object GhvasorEddhavabhyasalopashCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.4.119",
    text = "घ्वसोरेद्धावभ्यासलोपश्च",
    hindiExplanation = "धि परे होने पर घु-संज्ञक दा को एकार आदेश होता है और अभ्यास का लोप होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 4,
    optional = false,
    kramaValue = 640119,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DHATU,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.effectiveContext.rupa.lakara != Lakara.LOT) return false
        val dhatu = context.terms.firstOrNull { it.kind == TermKind.DHATU && it.id != "abhyasa" } ?: return false
        val ending = context.terms.lastOrNull { it.kind != TermKind.DHATU }
        val dhiEnvironment = ending?.surface == "धि" ||
            context.allEffectiveTerms.any { it.upadesha == "सिप्" && it.surface.isEmpty() }
        return dhatu.upadesha == "डुदाञ्" && dhatu.surface !in setOf("दे", "देहि") && dhiEnvironment
    }

    override fun apply(context: DerivationState): DerivationChange {
        val dhatu = context.terms.first { it.kind == TermKind.DHATU && it.id != "abhyasa" }
        val ending = context.terms.lastOrNull { it.kind != TermKind.DHATU }
        val endingSurvives = ending?.surface == "धि"
        var state = context.replaceTerm(dhatu.id, dhatu.copy(surface = if (endingSurvives) "दे" else "देहि"))
        if (ending != null) state = state.replaceTerm(ending.id, ending.copy(surface = if (endingSurvives) "हि" else ""))
        if (state.terms.any { it.id == "abhyasa" }) state = state.removeTerm("abhyasa", sutra = sutra)
        return DerivationChange(state, "6.4.119 forms देहि from the ghu root दा before धि.")
    }
}
