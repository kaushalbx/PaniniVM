package dev.panini.ashtadhyayi.adhyaya8.pada2

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

/** 8.2.77: हलि च. Lengthens the vowel of दिव् before the consonantal श्यन् remainder. */
object HaliCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.2.77",
    text = "हलि च",
    hindiExplanation = "हल् परे होने पर दिव् के इकार को दीर्घ होता है।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 2,
    optional = false,
    kramaValue = 820077,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DHATU,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val index = context.terms.indexOfFirst {
            it.kind == TermKind.DHATU && it.matchesUpadesha("दिवुँ") && it.surface == "दिव्"
        }
        return index >= 0 && context.terms.getOrNull(index + 1)?.upadesha == "श्यन्"
    }

    override fun apply(context: DerivationState): DerivationChange {
        val root = context.terms.first {
            it.kind == TermKind.DHATU && it.matchesUpadesha("दिवुँ") && it.surface == "दिव्"
        }
        return DerivationChange(
            state = context.substituteTermSurface(root.id, "दीव्", 'ि', "ी", sutra)
                .copy(stage = DerivationStage.FINAL),
            explanation = "8.2.77 lengthens the vowel of दिव् before the consonantal श्यन् remainder.",
        )
    }
}
