package dev.panini.execution

import dev.panini.core.Karaka
import dev.panini.core.SupAffix
import dev.panini.execution.binding.VyakaranamExecutionAdapter
import kotlin.test.*

class SyncreticRuntimeSlotTest {
    @Test fun `runtime role assignment retains the selected syncretic slot`() {
        val initial = mapOf("वाम" to SanskritValue.Sankhya(1, "host"),
            "दक्षिण" to SanskritValue.Sankhya(2, "host"))
        for ((predicate, role, slot) in listOf(
            Triple("सम", Karaka.KARANA, SupAffix.BHYAM_3),
            Triple("न्यून", Karaka.APADANA, SupAffix.BHYAM_5),
            Triple("अधिक", Karaka.APADANA, SupAffix.BHYAM_5),
        )) {
            val conversation = SambhashanaContext("प्रयोक्ता", "यन्त्रम्")
            val binding = assertIs<ExecutionBindingResult.Bound>(VyakaranamExecutionAdapter.bind(
                SanskritUktiInput(conversation.speaker, conversation.listener,
                    "वाम + सुँ दक्षिण + भ्याम् $predicate + सुँ असँ + लट् + तिप् ।"),
                conversation, environment = ValueEnvironment(initial)))
            val invocation = binding.ukti.invocations.single()
            val resolved = assertIs<OperationResolution.Resolved>(OperationResolver.resolve(invocation, initial))
            val operand = assertIs<ExecutionExpression.TypedOperand>(resolved.value.context.bindings[role])
            assertEquals(slot, operand.sup, predicate)
            assertEquals("दक्षिण", operand.referenceName)
        }
    }
}
