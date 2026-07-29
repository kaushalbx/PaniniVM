package dev.panini.derivation.sutra

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.sutra.runtime.SutraAvastha
import dev.panini.sutra.runtime.SutraEffect
import dev.panini.sutra.runtime.SutraId

data class DerivationAvastha(
    val derivation: DerivationState,
    val appliedSutras: List<SutraId> = emptyList(),
    val explanations: List<String> = emptyList(),
) : SutraAvastha

data class ApplyDerivationChange(
    val sutraId: SutraId,
    val change: DerivationChange,
) : SutraEffect<DerivationAvastha>
