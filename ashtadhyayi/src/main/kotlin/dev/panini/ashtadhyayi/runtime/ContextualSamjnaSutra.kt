package dev.panini.ashtadhyayi.runtime

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState

/** Runtime contract for a sūtra assigning a saṃjñā to matching derivation material. */
interface ContextualSamjnaSutra {
    fun hasSamjnaTarget(state: DerivationState): Boolean

    fun assignSamjna(state: DerivationState): DerivationChange
}
