package dev.panini.ashtadhyayi.adhyaya3.pada3

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
 * 3.3.158: समानकर्तृकेषु तुमुन्.
 * Introduces the infinitive suffix 'तुमुन्' (tumun) after a verbal root for purpose when agents are identical.
 */
object SamanakartrkesuTumunSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.3.158",
    text = "समानकर्तृकेषु तुमुन्",
    hindiExplanation = "समान कर्ता वाली धातुओं के उपपद रहते इच्छा आदि अर्थों में तुमुन् प्रत्यय होता है।",
    type = SutraType.UTSARGA,
    chapter = 3,
    pada = 3,
    optional = false,
    kramaValue = 330158,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    stage = dev.panini.sutra.SutraStage.PRATYAYA_SELECTION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage != DerivationStage.INITIAL && context.stage != DerivationStage.PRATYAYA_SELECTED) return false
        val isTumunRequested = context.samjnas.any { it.samjna == Samjna.TUMUN }
        val hasPratyaya = context.terms.any { it.kind == TermKind.PRATYAYA }
        return isTumunRequested && !hasPratyaya
    }

    override fun apply(context: DerivationState): DerivationChange {
        val tumunTerm = DerivationTerm(
            id = "tumun_pratyaya",
            surface = "तुमुँन्",
            kind = TermKind.PRATYAYA,
            upadesha = "तुमुन्",
            createdBySutra = sutra,
            itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA,
        )
        return DerivationChange(
            state = context.copy(
                terms = context.terms + tumunTerm,
                stage = DerivationStage.PRATYAYA_SELECTED,
            ),
            explanation = "3.3.158 introduces infinitive suffix तुमुन्."
        )
    }
}
