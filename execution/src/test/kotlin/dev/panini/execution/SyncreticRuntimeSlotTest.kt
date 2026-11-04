package dev.panini.execution

import dev.panini.core.Karaka
import dev.panini.core.SupAffix
import dev.panini.execution.binding.VyakaranamExecutionAdapter
import kotlin.test.*

class SyncreticRuntimeSlotTest {
    @Test fun `runtime role assignment retains the selected syncretic slot`() {
        val initial = mapOf("वाम" to SanskritValue.Sankhya(1, "host"),
            "दक्षिण" to SanskritValue.Sankhya(2, "host"))
        for ((predicate, slot) in listOf(
            "सम" to SupAffix.BHYAM_3,
            "न्यून" to SupAffix.BHYAM_5,
            "अधिक" to SupAffix.BHYAM_5,
        )) {
            val conversation = SambhashanaContext("प्रयोक्ता", "यन्त्रम्")
            val binding = assertIs<ExecutionBindingResult.Bound>(VyakaranamExecutionAdapter.bind(
                SanskritUktiInput(conversation.speaker, conversation.listener,
                    "वाम + सुँ दक्षिण + भ्याम् $predicate + सुँ असँ + लट् + तिप् ।"),
                conversation, environment = ValueEnvironment(initial)))
            val invocation = binding.ukti.invocations.single()
            val resolved = assertIs<OperationResolution.Resolved>(OperationResolver.resolve(invocation, initial))
            val operand = assertIs<ExecutionExpression.TypedOperand>(resolved.value.context.nominalBindings[
                NominalOperandRole.COMPARISON_STANDARD])
            assertEquals(slot, operand.sup, predicate)
            assertEquals("दक्षिण", operand.referenceName)
        }
    }
}
