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
        val noAgamaYet = context.allEffectiveTerms.none { it.id == "at-agama" }
        return pastAugmentEnvironment(context) && noAgamaYet
    }

    override fun apply(context: DerivationState): DerivationChange = introducePastAugment(context, "अट्", sutra)
}
