package dev.sanskrit.ashtadhyayi.adhyaya6.pada4

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.DerivationTerm
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

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
        val noAgamaYet = context.terms.none { it.id == "at-agama" }
        
        return isNgLakara && noAgamaYet
    }

    override fun apply(context: DerivationState): DerivationChange {
        val terms = context.terms
        val dhatuIndex = terms.indexOfFirst { it.kind == TermKind.DHATU }
        val newTerms = if (dhatuIndex >= 0) {
            terms.take(dhatuIndex) + 
            DerivationTerm("at-agama", "अ", TermKind.AGAMA, upadesha = "अट्") + 
            terms.drop(dhatuIndex)
        } else {
            listOf(DerivationTerm("at-agama", "अ", TermKind.AGAMA, upadesha = "अट्")) + terms
        }
        return DerivationChange(
            state = context.copy(terms = newTerms, stage = DerivationStage.IT_PROCESSED),
            explanation = "6.4.71: Prepend अट् augment before the verbal root."
        )
    }
}
