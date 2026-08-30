package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
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

/**
 * 3.1.48: णिश्ऱिद्रुस्रुभ्यः कर्तरि चङ्.
 * Prescribes 'चङ्' (caṅ) vicaraṇa affix in Luṅ (Aorist) after causative णि-ending roots.
 */
object NisriSruvbhyahCanSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.48",
    text = "णिश्ऱिद्रुस्रुभ्यः कर्तरि चङ्",
    hindiExplanation = "लुङ् में कर्ता अर्थ में णिजन्त धातुओं से चङ् प्रत्यय होता है।",
    type = SutraType.UTSARGA,
    chapter = 3,
    pada = 1,
    optional = false,
    kramaValue = 310048,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val isCanRequested = context.samjnas.any { it.samjna == Samjna.CAN }
        val hasCan = context.terms.any { it.upadesha == "चङ्" }
        return isCanRequested && !hasCan
    }

    override fun apply(context: DerivationState): DerivationChange {
        val canTerm = DerivationTerm(
            id = "can_pratyaya",
            surface = "चङ्",
            kind = TermKind.PRATYAYA,
            upadesha = "चङ्",
            createdBySutra = sutra,
            itProcessingPending = true,
        )
        return DerivationChange(
            state = context.copy(
                terms = context.terms + canTerm,
                stage = DerivationStage.PRATYAYA_SELECTED,
            ),
            explanation = "3.1.48 introduces causative Aorist vicaraṇa affix चङ् (अ)."
        )
    }
}
