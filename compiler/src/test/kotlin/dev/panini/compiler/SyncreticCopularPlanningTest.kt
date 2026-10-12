package dev.panini.compiler

import dev.panini.core.Karaka
import dev.panini.core.SupAffix
import dev.panini.execution.*
import dev.panini.execution.planning.ResolvedLeafPlanner
import kotlin.test.*

class SyncreticCopularPlanningTest {
    @Test fun `recognized predicates select the correct operation and syncretic standard role`() {
        for ((predicate, role, slot) in listOf(
            Triple("सम", Karaka.KARANA, SupAffix.BHYAM_3),
            Triple("न्यून", Karaka.APADANA, SupAffix.BHYAM_5),
            Triple("अधिक", Karaka.APADANA, SupAffix.BHYAM_5),
        )) {
            val plan = assertNotNull(ResolvedLeafPlanner.planAny(
                "वाम + सुँ दक्षिण + भ्याम् $predicate + सुँ असँ + लट् + तिप् ।"), predicate)
            val standard = assertIs<ExecutionExpression.TypedOperand>(plan.resolved.context.bindings[role], predicate)
            assertEquals(slot, standard.sup, predicate)
            assertEquals("दक्षिण", standard.referenceName)
        }
    }

    @Test fun `planned comparison standard selects the ablative slot not first bhyam slot`() {
        val clause = "वाम + सुँ दक्षिण + भ्याम् न्यून + सुँ असँ + लट् + तिप् ।"
        val plan = assertNotNull(ResolvedLeafPlanner.planAny(clause))
        val standard = assertIs<ExecutionExpression.TypedOperand>(plan.resolved.context.bindings[Karaka.APADANA],
            "${plan.resolved.operation.name}: ${plan.resolved.context.bindings}")
        assertEquals(SupAffix.BHYAM_5, standard.sup)
        assertEquals("दक्षिण", standard.referenceName)
        val source = "यदि ${clause.removeSuffix("।")} तर्हि " +
            "एक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् अन्यथा " +
            "शून्य + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ।"
        val generated = BytecodeCompiler.compileAndLoad(source, "SyncreticAblativeStandard")
        for ((left, right) in listOf(1L to 2L, 3L to 2L)) {
            val initial = mapOf("वाम" to SanskritValue.Sankhya(left, "host"),
                "दक्षिण" to SanskritValue.Sankhya(right, "host"))
            val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source,
                scope = ExecutionScope(environment = ValueEnvironment(initial))).last())
            assertEquals(if (left < right) 2L else 1L, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
            assertEquals(result.typedValue,
                (generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>)["LastResult"])
        }
    }
}
