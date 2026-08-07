package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SthanivadbhavaDelegationTest {

    private val vm = PaniniVM()

    @Test
    fun testSthanivadbhavaPolymorphicDelegationToParent() {
        val script = """
            # Step 1: Parent domain declaration 'गणित'
            गणित + सुँ इति अधि + कृ + घञ् + सुँ ।

            # Step 2: Child domain declaration 'गाणित' extending 'गणित' (Taddhita AṆ 4.1.83)
            गणित + अण् + सुँ इति अधि + कृ + घञ् + सुँ ।

            # Step 3: Define method 'गुण्' on Parent class 'गणित' (Multiplication: 5 * 2 = 10 -> दश)
            गणित + ङस् गुण् + ल्युट् + सुँ इति संज्ञा + सुँ ।
            पञ्च + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ॥

            # Step 4: Invoke 'गुण्' on Child class 'गाणित' (which has NO explicit override).
            # Sūtra 1.1.56 (स्थानिवदादेशोऽनल्विधौ) delegates execution to Parent 'गणित' -> 10 (दश)
            पञ्च + अम् द्वि + अम् च गाणित + ङस् गुण् + ल्युट् + टा कृ + लोट् + सिप् ।
        """.trimIndent()

        val results = vm.evalScript(script)
        val success = results.filterIsInstance<ExecutionResult.Success>()
        assertTrue(success.isNotEmpty(), "Expected successful Sthānivadbhāva delegation: $results")
        assertEquals("सप्त", success.last().value, "Expected child 'गाणित' to delegate method 'गुण्' to parent 'गणित' via Sūtra 1.1.56 (स्थानिवदादेशोऽनल्विधौ)")
    }
}
