package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * A call owns the values it introduces, so recursion keeps its own working values; program state (set at
 * top level before the call) is shared, and the call's result returns to its caller.
 */
class ProcedureCallScopeTest {
    private fun printed(source: String): List<String> {
        val results = PaniniVM().evalScript(source)
        assertTrue(results.none { it is ExecutionResult.Failure }, results.filterIsInstance<ExecutionResult.Failure>().toString())
        return results.filterIsInstance<ExecutionResult.Success>().filter { it.outputKind == OutputKind.CONSOLE }.map { it.value }
    }

    private val setsMeasure = "परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।\n" +
        "एक + अम् परिमाण + ङि स्था + णिच् + लोट् + सिप् ॥\n"

    @Test fun `a value first set inside a call stays with that call`() {
        val inside = printed(setsMeasure + "परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।\n" +
            "पञ्चन् + शस् परिमाण + ङि स्था + णिच् + लोट् + सिप् ।\nपरिमाण + अम् मुद्र् + णिच् + लोट् + सिप् ।")
        assertEquals(listOf("पञ्चन्"), inside)
        // The caller never set परिमाण before the call, so the call's एक does not reach it.
        val after = PaniniVM().evalScript(setsMeasure + "परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।\n" +
            "परिमाण + अम् मुद्र् + णिच् + लोट् + सिप् ।")
        val shown = after.filterIsInstance<ExecutionResult.Success>().filter { it.outputKind == OutputKind.CONSOLE }.map { it.value }
        assertTrue("एक" !in shown, shown.toString())
    }

    @Test fun `program state set at top level is changed by the call`() {
        val source = setsMeasure + "पञ्चन् + शस् परिमाण + ङि स्था + णिच् + लोट् + सिप् ।\n" +
            "परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।\nपरिमाण + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        assertEquals(listOf("एक"), printed(source))
    }

    @Test fun `a recursive call does not overwrite its caller's working values`() {
        // Each level sets स्तर to its own input, recurses on one less, then prints its own स्तर.
        val source = "अवरोह + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।\n" +
            "मान + सुँ नाम मान + सुँ सङ्ख्या + सुँ असँ + लट् + तिप् ।\n" +
            "मान + अम् स्तर + ङि स्था + णिच् + लोट् + सिप् ।\n" +
            "यदि शून्य + सुँ मान + ङसिँ न्यून + सुँ असँ + लट् + तिप् " +
            "तर्हि मान + अम् एक + अम् च वि + युज् + णिच् + लोट् + सिप् ततः फल + अम् अवरोह + टा डुकृञ् + उ + लोट् + सिप् ।\n" +
            "स्तर + अम् मुद्र् + णिच् + लोट् + सिप् ॥\n" +
            "त्रि + शस् अवरोह + टा डुकृञ् + उ + लोट् + सिप् ।"
        assertEquals(listOf("शून्यम्", "एकम्", "द्वे", "त्रि"), printed(source))
    }

    @Test fun `the call's result returns to the caller as फल`() {
        val source = "द्विगुण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।\n" +
            "मान + सुँ नाम मान + सुँ सङ्ख्या + सुँ असँ + लट् + तिप् ।\n" +
            "मान + अम् मान + अम् च युज् + णिच् + लोट् + सिप् ततः फल + अम् द्विगुणित + ङि स्था + णिच् + लोट् + सिप् ॥\n" +
            "त्रि + शस् द्विगुण + टा डुकृञ् + उ + लोट् + सिप् ।\nफल + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        assertEquals(listOf("षट्"), printed(source))
    }

    @Test fun `merge sort sorts by recursion`() {
        val source = java.io.File("examples/algorithms/merge_sort.pvm").readText()
        assertEquals(listOf("[एक, द्वि, त्रि, चतुर्, पञ्चन्, अष्टन्]"), printed(source))
    }
}
