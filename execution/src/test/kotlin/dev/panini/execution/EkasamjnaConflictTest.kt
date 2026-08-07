package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EkasamjnaConflictTest {

    private val vm = PaniniVM()

    @Test
    fun testNityaHeaderScriptParsing() {
        val statements = PvmScript.parse("गणित + ङस् गुण् + ल्युट् + सुँ इति नि + त्य + सुँ इति संज्ञा + सुँ ।")
        assertTrue(statements.isNotEmpty(), "Expected non-empty script statements for Nitya header line")
        val defn = statements.first() as PvmScriptStatement.SamjnaDefinition
        assertTrue(defn.isNitya, "Expected isNitya to be true for Nitya header definition")
    }

    @Test
    fun testEkasamjnaResolutionHierarchy() {
        val script = """
            # Domain declaration
            गणित + सुँ इति अधि + कृ + घञ् + सुँ ।

            # 1. Utsarga (Default rule - Priority 1): 5 + 5 = 10 (दश)
            गणित + ङस् गुण् + ल्युट् + सुँ इति संज्ञा + सुँ ।
            पञ्च + अम् पञ्च + अम् च युज् + णिच् + लोट् + सिप् ॥

            # 2. Nitya rule (Mandatory rule - Priority 2): 10 + 10 = 20 (विंशति)
            गणित + ङस् गुण् + ल्युट् + सुँ इति नि + त्य + सुँ इति संज्ञा + सुँ ।
            दश + अम् दश + अम् च युज् + णिच् + लोट् + सिप् ॥

            # 3. Antaranga rule (Internal rule - Priority 3): 10 + 20 = 30 (त्रिंशत्)
            गणित + ङस् गुण् + ल्युट् + सुँ इति अन्तर् + अङ्ग + सुँ इति संज्ञा + सुँ ।
            दश + अम् विंशति + अम् च युज् + णिच् + लोट् + सिप् ॥

            # 4. Apavada rule (Exception rule - Priority 4): 10 + 30 = 40 (चत्वारिंशत्)
            गणित + ङस् गुण् + ल्युट् + सुँ इति अप + वद् + घञ् + सुँ इति संज्ञा + सुँ ।
            दश + अम् त्रिंशत् + अम् च युज् + णिच् + लोट् + सिप् ॥

            # Test invocation: Sūtra 1.4.1 Ekasaṁjñā conflict resolution selects Apavāda (Level 4) -> चत्वारिंशत्
            पञ्च + अम् द्वि + अम् च गणित + ङस् गुण् + ल्युट् + टा कृ + लोट् + सिप् ।
        """.trimIndent()

        val results = vm.evalScript(script)
        val success = results.filterIsInstance<ExecutionResult.Success>()
        assertTrue(success.isNotEmpty(), "Expected successful execution: $results")
        assertEquals("चत्वारिंशत्", success.last().value, "Expected Apavāda (Level 4 - चत्वारिंशत्) via Sūtra 1.4.1 Ekasaṁjñā resolution")
    }

    @Test
    fun testAntarangaOverridesNityaAndUtsarga() {
        val script = """
            गणित + सुँ इति अधि + कृ + घञ् + सुँ ।

            # Utsarga (Level 1)
            गणित + ङस् गुण् + ल्युट् + सुँ इति संज्ञा + सुँ ।
            पञ्च + अम् पञ्च + अम् च युज् + णिच् + लोट् + सिप् ॥

            # Nitya (Level 2)
            गणित + ङस् गुण् + ल्युट् + सुँ इति नि + त्य + सुँ इति संज्ञा + सुँ ।
            दश + अम् दश + अम् च युज् + णिच् + लोट् + सिप् ॥

            # Antaranga (Level 3)
            गणित + ङस् गुण् + ल्युट् + सुँ इति अन्तर् + अङ्ग + सुँ इति संज्ञा + सुँ ।
            दश + अम् विंशति + अम् च युज् + णिच् + लोट् + सिप् ॥

            # Invocation without Apavāda: Antaranga (Level 3) wins -> त्रिंशत्
            पञ्च + अम् द्वि + अम् च गणित + ङस् गुण् + ल्युट् + टा कृ + लोट् + सिप् ।
        """.trimIndent()

        val results = vm.evalScript(script)
        val success = results.filterIsInstance<ExecutionResult.Success>()
        assertTrue(success.isNotEmpty(), "Expected successful execution: $results")
        assertEquals("त्रिंशत्", success.last().value, "Expected Antaraṅga (Level 3 - त्रिंशत्) via Sūtra 1.4.1 Ekasaṁjñā resolution")
    }

    @Test
    fun testNityaOverridesUtsarga() {
        val script = """
            गणित + सुँ इति अधि + कृ + घञ् + सुँ ।

            # Utsarga (Level 1)
            गणित + ङस् गुण् + ल्युट् + सुँ इति संज्ञा + सुँ ।
            पञ्च + अम् पञ्च + अम् च युज् + णिच् + लोट् + सिप् ॥

            # Nitya (Level 2)
            गणित + ङस् गुण् + ल्युट् + सुँ इति नि + त्य + सुँ इति संज्ञा + सुँ ।
            दश + अम् दश + अम् च युज् + णिच् + लोट् + सिप् ॥

            # Invocation without Apavāda or Antaraṅga: Nitya (Level 2) wins -> विंशति
            पञ्च + अम् द्वि + अम् च गणित + ङस् गुण् + ल्युट् + टा कृ + लोट् + सिप् ।
        """.trimIndent()

        val results = vm.evalScript(script)
        val success = results.filterIsInstance<ExecutionResult.Success>()
        assertTrue(success.isNotEmpty(), "Expected successful execution: $results")
        assertEquals("विंशतिः", success.last().value, "Expected Nitya (Level 2 - विंशतिः) via Sūtra 1.4.1 Ekasaṁjñā resolution")
    }
}
