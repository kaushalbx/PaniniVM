package dev.panini.execution

import dev.panini.execution.binding.VyakaranamExecutionAdapter
import kotlin.test.*

class ControlEffectContractTest {
    private fun planning(declared: ExecutionControlSignal?, actual: ExecutionControlSignal?): PlanningResult.Planned {
        val source = "एक + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val bound = assertIs<ExecutionBindingResult.Bound>(VyakaranamExecutionAdapter.bind(
            SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", source), SambhashanaContext("प्रयोक्ता", "यन्त्रम्")))
        val planning = assertIs<PlanningResult.Planned>(ExecutionPlanner.plan(ExecutionProgram(bound.ukti), ValueEnvironment()))
        val plan = planning.plans.single()
        val action = object : DhatuAction("control-contract-fixture", "test") {
            override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult =
                ExecutionResult.Success("एक", operation.name, typedValue = SanskritValue.Sankhya(1, "एक"),
                    controlSignal = actual)
        }
        return planning.copy(plans = listOf(plan.copy(resolved = plan.resolved.copy(
            operation = plan.resolved.operation.copy(action = action, controlSignal = declared)))))
    }

    @Test
    fun `successful action must match its declared control effect`() {
        val signals = listOf(null, ExecutionControlSignal.BREAK_LOOP, ExecutionControlSignal.RETURN_PROCEDURE)
        for (declared in signals) for (actual in signals) {
            val result = ExecutionRuntime.execute(planning(declared, actual), ExecutionScope(
                procedureBindings = ProcedureLocalBindings.create(emptyList(), emptyList())))
            if (declared == actual) {
                assertEquals(actual, assertIs<Phala.Siddha>(result).controlSignal)
            } else {
                val failure = assertIs<ExecutionResult.Failure>(assertIs<Phala.Asiddha>(result).result)
                assertEquals(ExecutionError.ACTION_FAILED, failure.error)
                assertTrue(failure.message.contains("declared control effect"))
            }
        }
    }
}
