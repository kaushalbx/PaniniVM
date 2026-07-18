package dev.sanskrit.ashtadhyayi.adhyaya8.pada2

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.derivation.VarnaSubstitution
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

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
            state = context.replaceTerm(root.id, root.copy(surface = "दीव्"))
                .copy(stage = DerivationStage.FINAL)
                .addSubstitution(VarnaSubstitution(root.id, 'ि', "ी", sutra)),
            explanation = "8.2.77 lengthens the vowel of दिव् before the consonantal श्यन् remainder.",
        )
    }
}
