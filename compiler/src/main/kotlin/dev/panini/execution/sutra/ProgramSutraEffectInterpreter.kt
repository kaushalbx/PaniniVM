package dev.panini.execution.sutra

import dev.panini.execution.ExecutableUkti
import dev.panini.execution.ExecutionPlanner
import dev.panini.execution.ExecutionProgram
import dev.panini.execution.ExecutionRuntime
import dev.panini.execution.ExecutionScope
import dev.panini.execution.Phala
import dev.panini.execution.PlanningResult
import dev.panini.execution.SanskritValue
import dev.panini.execution.ValueEnvironment
import dev.panini.sutra.runtime.SutraEffect
import dev.panini.sutra.runtime.SutraEffectApplication
import dev.panini.sutra.runtime.SutraEffectInterpreter
import dev.panini.sutra.runtime.SutraId

/**
 * Transitional interpreter: runtime sūtras are authoritative for ordering,
 * while the proven operation resolver, authority policy, and action runtime
 * still perform each native dhātu invocation.
 */
class ProgramSutraEffectInterpreter(
    private val scope: ExecutionScope,
) : SutraEffectInterpreter<ProgramAvastha> {
    override fun apply(
        effect: SutraEffect<ProgramAvastha>,
        state: ProgramAvastha,
    ): SutraEffectApplication<ProgramAvastha> =
        when (effect) {
            is InvokeDhatuEffect -> applyInvocation(effect, state)
            is RepeatWhileEffect -> applyRepetition(effect, state)
            else -> SutraEffectApplication.Failed(
                "Unsupported program sūtra effect: ${effect::class.simpleName}",
            )
        }

    private fun applyInvocation(
        effect: InvokeDhatuEffect,
        state: ProgramAvastha,
    ): SutraEffectApplication<ProgramAvastha> {
        val invocation = effect.invocation
        val singleUkti: ExecutableUkti = effect.ukti.copy(
            invocations = listOf(invocation),
            dependencies = emptySet(),
        )
        return when (val planning = ExecutionPlanner.plan(ExecutionProgram(singleUkti), state.environment)) {
            is PlanningResult.Failed ->
                SutraEffectApplication.Applied(
                    state.copy(
                        lastPhala = Phala.Asiddha(planning.result, emptyList()),
                        halted = true,
                    ),
                    "Planning failed for ${invocation.id}.",
                )
            is PlanningResult.Planned -> {
                val phala = ExecutionRuntime.execute(planning, scope, state.environment)
                val next = when (phala) {
                    is Phala.Siddha -> {
                        val produced = phala.typedValues + phala.localBindings
                        state.copy(
                            environment = state.environment.mergedWith(ValueEnvironment(produced)),
                            completedSutras = state.completedSutras + SutraId(invocation.id),
                            invocationValues = state.invocationValues + phala.typedValues,
                            localBindings = state.localBindings + phala.localBindings,
                            executionTrace = state.executionTrace + phala.trace,
                            lastPhala = phala,
                        )
                    }
                    is Phala.Avagata -> state.copy(
                        completedSutras = state.completedSutras + SutraId(invocation.id),
                        executionTrace = state.executionTrace + phala.trace,
                        lastPhala = phala,
                    )
                    else -> state.copy(lastPhala = phala, halted = true)
                }
                SutraEffectApplication.Applied(
                    next,
                    "Processed dhātu invocation ${invocation.id} through the existing execution runtime.",
                )
            }
        }
    }

    private fun applyRepetition(
        effect: RepeatWhileEffect,
        initialState: ProgramAvastha,
    ): SutraEffectApplication<ProgramAvastha> {
        var state = initialState
        var iterations = 0
        var lastBodyPhala: Phala? = null
        while (true) {
            val condition = state.invocationValues[effect.condition.invocation.id]
            if (condition !is SanskritValue.Satya) {
                return SutraEffectApplication.Failed(
                    "Conditional duration requires ${effect.condition.invocation.id} to yield satya.",
                )
            }
            if (!condition.boolean) break
            if (iterations >= effect.maximumIterations) {
                return SutraEffectApplication.Failed(
                    "Conditional duration exceeded its limit of ${effect.maximumIterations} iterations.",
                )
            }
            val bodyApplication = applyInvocation(effect.body, state)
            if (bodyApplication is SutraEffectApplication.Failed) return bodyApplication
            state = (bodyApplication as SutraEffectApplication.Applied).state
            if (state.halted) return bodyApplication
            lastBodyPhala = state.lastPhala

            val conditionApplication = applyInvocation(effect.condition, state)
            if (conditionApplication is SutraEffectApplication.Failed) return conditionApplication
            state = (conditionApplication as SutraEffectApplication.Applied).state
            if (state.halted) return conditionApplication
            iterations++
        }
        return SutraEffectApplication.Applied(
            state.copy(
                completedSutras = state.completedSutras + SutraId(effect.body.invocation.id),
                lastPhala = lastBodyPhala ?: state.lastPhala,
            ),
            "Repeated ${effect.body.invocation.id} $iterations time(s) while " +
                "${effect.condition.invocation.id} yielded satya.",
        )
    }
}
