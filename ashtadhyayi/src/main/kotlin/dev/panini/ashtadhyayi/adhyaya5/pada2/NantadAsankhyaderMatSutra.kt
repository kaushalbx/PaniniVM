package dev.panini.ashtadhyayi.adhyaya5.pada2

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 5.2.49: नान्तादसंख्यादेर्मट् */
object NantadAsankhyaderMatSutra : Sutra<DerivationState, DerivationChange>(
    number = "5.2.49",
    text = "नान्तादसंख्यादेर्मट्",
    hindiExplanation = "न्-अन्त सङ्ख्या प्रातिपदिक से पूरण अर्थ में मट् आगम होता है।",
    type = SutraType.UTSARGA,
    chapter = 5,
    pada = 2,
    optional = false,
    kramaValue = 520049,
    role = SutraRole.Vidhi,
    action = SutraAction.AGAMA,
    scope = SutraScope.DERIVATION,
    stage = dev.panini.sutra.SutraStage.PRATYAYA_SELECTION,
), DerivationSutra {

    override fun matches(context: DerivationState): Boolean {
        val lastTerm = context.terms.lastOrNull() ?: return false
        val hasPuranaRequest = context.samjnas.any { it.samjna == Samjna.PURANA }
        val isAlreadyApplied = context.terms.any { it.surface == "म" || it.upadesha == "मट्" }
        return hasPuranaRequest && !isAlreadyApplied && lastTerm.surface in setOf(
            "पञ्चन्", "सप्तन्", "अष्टन्", "नवन्", "दशन्"
        )
    }

    override fun apply(context: DerivationState): DerivationChange {
        val matTerm = DerivationTerm(
            id = "purana_mat",
            surface = "म",
            kind = TermKind.AGAMA,
            upadesha = "मट्",
            createdBySutra = sutra,
        )
        return DerivationChange(
            state = context.copy(terms = context.terms + matTerm),
            explanation = "$text: added augment मट् (म)"
        )
    }
}
