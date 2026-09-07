package dev.panini.ashtadhyayi.adhyaya6.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.ItProcessingPhase
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 6.4.71: luṅ-laṅ-lṛṅkṣvaḍ uḍāttaḥ.
 * Prepends the 'aṭ' augment (अ) to the verbal root when followed by a ṅit lakāra (laṅ, luṅ, lṛṅ).
 */
object LunglanglrngksvaduDattaSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.4.71",
    text = "लुङ्लङ्लृङ्क्ष्वडुदात्तः",
    hindiExplanation = "लुङ्, लङ् और लृङ् परे होने पर अङ्ग को अट् का आगम होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 4,
    optional = false,
    kramaValue = 640071,
    role = SutraRole.Vidhi,
    action = SutraAction.AGAMA,
    scope = SutraScope.DERIVATION
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage == DerivationStage.INITIAL || context.stage == DerivationStage.PRATYAYA_SELECTED) return false
        val lastTerm = context.terms.lastOrNull() ?: return false

        val isNgLakara = lastTerm.matchesUpadesha("लङ्") || lastTerm.matchesUpadesha("लृङ्") || lastTerm.matchesUpadesha("लुङ्")
        val noAgamaYet = context.allEffectiveTerms.none { it.id == "at-agama" }

        return isNgLakara && noAgamaYet
    }

    override fun apply(context: DerivationState): DerivationChange {
        val terms = context.terms
        val dhatuIndex = terms.indexOfFirst { it.kind == TermKind.DHATU }
        val target = terms.getOrNull(dhatuIndex) ?: terms.first()
        val augment = DerivationTerm(
            "at-agama", "अट्", TermKind.AGAMA,
            upadesha = "अट्", createdBySutra = sutra,
            itProcessingPhase = ItProcessingPhase.RAW_UPADESHA,
            augmentTargetId = target.id, mergeIntoAugmentTarget = false,
        )
        val newTerms = if (dhatuIndex >= 0) {
            terms.take(dhatuIndex) +
            augment +
            terms.drop(dhatuIndex)
        } else {
            listOf(augment) + terms
        }
        return DerivationChange(
            state = context.copy(terms = newTerms, stage = DerivationStage.IT_PROCESSED),
            explanation = "6.4.71 introduces raw अट् targeted at the beginning of the verbal root."
        )
    }
}
