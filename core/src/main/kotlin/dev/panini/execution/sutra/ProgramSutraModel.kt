package dev.panini.execution.sutra

import dev.panini.execution.DhatuInvocation
import dev.panini.execution.ExecutableUkti
import dev.panini.execution.Phala
import dev.panini.execution.SanskritValue
import dev.panini.execution.ValueEnvironment
import dev.panini.sutra.runtime.SutraAvastha
import dev.panini.sutra.runtime.SutraEffect
import dev.panini.sutra.runtime.SutraId

data class ProgramAvastha(
    val environment: ValueEnvironment,
    val completedSutras: Set<SutraId> = emptySet(),
    val invocationValues: Map<String, SanskritValue> = emptyMap(),
    val localBindings: Map<String, SanskritValue> = emptyMap(),
    val executionTrace: List<String> = emptyList(),
    val lastPhala: Phala? = null,
    val halted: Boolean = false,
) : SutraAvastha

data class InvokeDhatuEffect(
    val invocation: DhatuInvocation,
    val ukti: ExecutableUkti,
) : SutraEffect<ProgramAvastha>

data class RepeatWhileEffect(
    val condition: InvokeDhatuEffect,
    val body: InvokeDhatuEffect,
    val maximumIterations: Int,
) : SutraEffect<ProgramAvastha>
