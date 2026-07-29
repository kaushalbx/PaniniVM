package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SamjnaDefinitionArtha
import dev.panini.sutra.Samjni
import dev.panini.sutra.SamjnaVidhayakaSutra
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 1.1.60: adarśanaṃ lopaḥ.
 * Disappearance (non-perception) of an element is called 'lopa'.
 */
object AdarsanamLopaSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.60",
    text = "अदर्शनं लोपः",
    segmentedSource = "नञ् - दृश् + ल्युट् + अम् लोप + सुँ ।",
    hindiExplanation = "वर्ण के न दिखने (अदर्शन) को लोप संज्ञा दी जाती है।",
    type = SutraType.SAMJNA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110060,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.DERIVATION,
), DerivationSutra, SamjnaVidhayakaSutra {
    override val artha = SamjnaDefinitionArtha(
        samjni = Samjni.ADARSHANA,
        samjna = dev.panini.shiksha.Samjna.LOPA,
    )

    override fun matches(context: DerivationState): Boolean = false
    override fun apply(context: DerivationState): DerivationChange = error("Interpretive rule.")
}
