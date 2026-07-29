package dev.panini.ashtadhyayi.runtime

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.sutra.ArthavatSutra
import dev.panini.sutra.ContextualSamjnaAssignmentArtha

/** Runtime contract for a sūtra assigning a saṃjñā to matching derivation material. */
interface ContextualSamjnaSutra : ArthavatSutra {
    override val artha: ContextualSamjnaAssignmentArtha

    fun hasSamjnaTarget(state: DerivationState): Boolean

    fun assignSamjna(state: DerivationState): DerivationChange
}
