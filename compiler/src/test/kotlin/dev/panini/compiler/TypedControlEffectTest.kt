package dev.panini.compiler

import dev.panini.execution.*
import dev.panini.execution.planning.ResolvedLeafPlanner
import kotlin.test.*

class TypedControlEffectTest {
    @Test
    fun `primitive optimization cannot erase declared control effect`() {
        val plan = assertNotNull(ResolvedLeafPlanner.planAny("एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।"))
        val controlled = plan.copy(resolved = plan.resolved.copy(operation =
            plan.resolved.operation.copy(controlSignal = ExecutionControlSignal.BREAK_LOOP)))
        assertEquals(listOf(CompilerInstruction.RequestBreak), CompilerIrLowering.lowerLeafValues(controlled))
    }

    @Test
    fun `break lowering uses declared effect rather than operation name`() {
        val plan = assertNotNull(ResolvedLeafPlanner.planAny("वि + स्था + लोट् + सिप् ।"))
        assertEquals(ExecutionControlSignal.BREAK_LOOP, plan.resolved.operation.controlSignal)
        val executed = assertIs<ExecutionResult.Success>(plan.resolved.operation.action.execute(
            plan.resolved.context, plan.resolved.operation))
        assertEquals(plan.resolved.operation.controlSignal, executed.controlSignal)
        val renamed = object : DhatuAction("renamed-control", "test") {
            override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult = error("not executed")
        }
        val renamedPlan = plan.copy(resolved = plan.resolved.copy(
            operation = plan.resolved.operation.copy(action = renamed)))
        assertEquals(CompilerInstruction.RequestBreak, CompilerIrLowering.lowerLeaf(renamedPlan))
        val ordinaryPlan = plan.copy(resolved = plan.resolved.copy(
            operation = plan.resolved.operation.copy(controlSignal = null)))
        assertIs<CompilerInstruction.Call>(CompilerIrLowering.lowerLeaf(ordinaryPlan))
    }
}
