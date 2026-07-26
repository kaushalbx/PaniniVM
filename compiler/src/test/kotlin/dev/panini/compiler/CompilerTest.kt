package dev.panini.compiler

import dev.panini.execution.SanskritValue
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CompilerTest {

    @Test
    fun testCompileAndRunAddition() {
        val script = "हे यन्त्र + सुँ, एक + अम् द्वि + औट् त्रि + शस् च युज् + णिच् + लोट् + सिप् ।"
        val className = "SanskritAdditionTest"
        
        // Use compileAndLoad dynamically in memory
        val clazz = BytecodeCompiler.compileAndLoad(script, className)
        val executeMethod = clazz.getMethod("execute")
        
        // Capture stdout
        val originalOut = System.out
        val outContent = ByteArrayOutputStream()
        System.setOut(PrintStream(outContent))
        
        val resultVariables: Map<*, *>?
        try {
            resultVariables = executeMethod.invoke(null) as? Map<*, *>
        } finally {
            System.setOut(originalOut)
        }

        val output = outContent.toString().trim()
        assertTrue(output.contains("Line 1:"), "Output should contain line trace prefix")
        assertTrue(output.contains("✓ Result: षट्"), "Result should be correctly computed as षट्: $output")
        
        assertNotNull(resultVariables, "Returned variables map should not be null")
        val finalResultVal = resultVariables["योग-1"] as? SanskritValue.Sankhya
        assertNotNull(finalResultVal, "Result value for 'योग-1' should be present and type Sankhya")
        assertEquals(6L, finalResultVal.value, "Sankhya value should be 6")
        assertEquals("षट्", finalResultVal.word, "Sankhya word representation should be षट्")
    }

    @Test
    fun testCompileAndRunMultiClause() {
        // A multi-clause script referencing intermediate result (फल)
        val script = """
            एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।
            फल + औट् द्वि + औट् युज् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        
        val className = "SanskritMultiClauseTest"
        val clazz = BytecodeCompiler.compileAndLoad(script, className)
        val executeMethod = clazz.getMethod("execute")
        
        // Capture stdout
        val originalOut = System.out
        val outContent = ByteArrayOutputStream()
        System.setOut(PrintStream(outContent))
        
        val resultVariables: Map<*, *>?
        try {
            resultVariables = executeMethod.invoke(null) as? Map<*, *>
        } finally {
            System.setOut(originalOut)
        }

        val output = outContent.toString().trim()
        assertTrue(output.contains("Line 1:"), "Output should contain Line 1 trace")
        assertTrue(output.contains("✓ Result: त्रीणि"), "Result of 1 + 2 should be त्रीणि: $output")
        assertTrue(output.contains("Line 2:"), "Output should contain Line 2 trace")
        assertTrue(output.contains("✓ Result: पञ्च"), "Result of 3 + 2 should be पञ्च: $output")

        assertNotNull(resultVariables, "Returned variables map should not be null")
        
        // Check first statement result via its turn ID
        val clause1Val = resultVariables["उक्ति-१/योग-1"] as? SanskritValue.Sankhya
        assertNotNull(clause1Val)
        assertEquals(3L, clause1Val.value)
        assertEquals("त्रीणि", clause1Val.word)
        
        // Check second statement result via its turn ID
        val clause2Val = resultVariables["उक्ति-२/योग-1"] as? SanskritValue.Sankhya
        assertNotNull(clause2Val)
        assertEquals(5L, clause2Val.value)
        assertEquals("पञ्च", clause2Val.word)

        // Check general LastResult entry
        val lastResultVal = resultVariables["LastResult"] as? SanskritValue.Sankhya
        assertNotNull(lastResultVal)
        assertEquals(5L, lastResultVal.value)
    }
}
