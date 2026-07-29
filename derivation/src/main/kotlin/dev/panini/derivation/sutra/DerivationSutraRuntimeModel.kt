package dev.panini.derivation.sutra

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.shiksha.Samjna
import dev.panini.sutra.InterpretivePrinciple
import dev.panini.sutra.Samjni
import dev.panini.sutra.runtime.SutraAvastha
import dev.panini.sutra.runtime.SutraEffect
import dev.panini.sutra.runtime.SutraId

data class DerivationAvastha(
    val derivation: DerivationState,
    val samjnaDefinitions: Set<SamjnaDefinition> = emptySet(),
    val interpretivePrinciples: Set<InterpretivePrincipleDefinition> = emptySet(),
    val appliedSutras: List<SutraId> = emptyList(),
    val explanations: List<String> = emptyList(),
) : SutraAvastha

data class SamjnaDefinition(
    val samjni: Samjni,
    val samjna: Samjna,
    val definingSutra: SutraId,
)

data class InterpretivePrincipleDefinition(
    val principle: InterpretivePrinciple,
    val definingSutra: SutraId,
)

data class ApplyDerivationChange(
    val sutraId: SutraId,
    val change: DerivationChange,
) : SutraEffect<DerivationAvastha>

data class DefineSamjna(
    val definition: SamjnaDefinition,
) : SutraEffect<DerivationAvastha>

data class DefineInterpretivePrinciple(
    val definition: InterpretivePrincipleDefinition,
) : SutraEffect<DerivationAvastha>
