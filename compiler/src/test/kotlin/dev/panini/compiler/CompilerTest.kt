package dev.panini.compiler

import dev.panini.execution.SanskritValue
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@org.junit.jupiter.api.parallel.Execution(org.junit.jupiter.api.parallel.ExecutionMode.SAME_THREAD)
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

    @Test
    fun testCompileAndRunLoop() {
        val script = "पञ्च + अम् सङ्ख्यायोजनम् + टा वृत् + णिच् + लोट् + सिप् ।"
        val className = "SanskritLoopIntegrationTest"

        val clazz = BytecodeCompiler.compileAndLoad(script, className)
        val executeMethod = clazz.getMethod("execute")

        // Setup mock renderer for loop index addition
        dev.panini.execution.SankhyaResultRenderer.defaultRenderer = dev.panini.execution.SankhyaResultRenderer { value ->
            when (value) {
                15L -> "पञ्चदश"
                else -> value.toString()
            }
        }

        val resultVariables = executeMethod.invoke(null) as? Map<*, *>
        assertNotNull(resultVariables)

        val finalResultVal = resultVariables["योग-1"] as? SanskritValue.Sankhya
        assertNotNull(finalResultVal)
        assertEquals(15L, finalResultVal.value)
        assertEquals("पञ्चदश", finalResultVal.word)
    }

    @Test
    fun testCompileAndRunListMap() {
        val script = "एक + अम् द्वि + औट् त्रि + शस् च वर्धन + टा सम् + यु + णिच् + लोट् + सिप् ।"
        val className = "SanskritListMapIntegrationTest"

        val clazz = BytecodeCompiler.compileAndLoad(script, className)
        val executeMethod = clazz.getMethod("execute")

        // Setup mock renderer
        dev.panini.execution.SankhyaResultRenderer.defaultRenderer = dev.panini.execution.SankhyaResultRenderer { value ->
            when (value) {
                1L -> "एक"
                2L -> "द्वि"
                3L -> "त्रि"
                4L -> "चतुर्"
                6L -> "षट्"
                else -> value.toString()
            }
        }

        val resultVariables = executeMethod.invoke(null) as? Map<*, *>
        assertNotNull(resultVariables)

        val finalResultVal = resultVariables["योग-1"] as? SanskritValue.Suchi
        assertNotNull(finalResultVal)
        assertEquals(3, finalResultVal.items.size)
        assertEquals(2L, (finalResultVal.items[0] as SanskritValue.Sankhya).value)
        assertEquals(4L, (finalResultVal.items[1] as SanskritValue.Sankhya).value)
        assertEquals(6L, (finalResultVal.items[2] as SanskritValue.Sankhya).value)
    }

    @Test
    fun testCompileAndRunListFilter() {
        val script = "एक + अम् द्वि + औट् त्रि + शस् चतुर् + शस् च युग्मत्व + टा वि + वृज् + णिच् + लोट् + सिप् ।"
        val className = "SanskritListFilterIntegrationTest"

        val clazz = BytecodeCompiler.compileAndLoad(script, className)
        val executeMethod = clazz.getMethod("execute")

        // Setup mock renderer
        dev.panini.execution.SankhyaResultRenderer.defaultRenderer = dev.panini.execution.SankhyaResultRenderer { value ->
            when (value) {
                1L -> "एक"
                2L -> "द्वि"
                3L -> "त्रि"
                4L -> "चतुर्"
                else -> value.toString()
            }
        }

        val resultVariables = executeMethod.invoke(null) as? Map<*, *>
        assertNotNull(resultVariables)

        val finalResultVal = resultVariables["योग-1"] as? SanskritValue.Suchi
        assertNotNull(finalResultVal)
        assertEquals(2, finalResultVal.items.size)
        assertEquals(2L, (finalResultVal.items[0] as SanskritValue.Sankhya).value)
        assertEquals(4L, (finalResultVal.items[1] as SanskritValue.Sankhya).value)
    }

    @Test
    fun testCompileAndRunListConcat() {
        val script = "एक + अम् द्वि + औट् च त्रि + ङे चतुर् + भ्याम् च सृज् + णिच् + लोट् + सिप् ।"
        val className = "SanskritListConcatIntegrationTest"

        val clazz = BytecodeCompiler.compileAndLoad(script, className)
        val executeMethod = clazz.getMethod("execute")

        // Setup mock renderer
        dev.panini.execution.SankhyaResultRenderer.defaultRenderer = dev.panini.execution.SankhyaResultRenderer { value ->
            when (value) {
                1L -> "एक"
                2L -> "द्वि"
                3L -> "त्रि"
                4L -> "चतुर्"
                else -> value.toString()
            }
        }

        val resultVariables = executeMethod.invoke(null) as? Map<*, *>
        assertNotNull(resultVariables)

        val finalResultVal = resultVariables["योग-1"] as? SanskritValue.Suchi
        assertNotNull(finalResultVal)
        assertEquals(4, finalResultVal.items.size)
        assertEquals(1L, (finalResultVal.items[0] as SanskritValue.Sankhya).value)
        assertEquals(2L, (finalResultVal.items[1] as SanskritValue.Sankhya).value)
        assertEquals(3L, (finalResultVal.items[2] as SanskritValue.Sankhya).value)
        assertEquals(4L, (finalResultVal.items[3] as SanskritValue.Sankhya).value)
    }

    @Test
    fun testCompileAndRunListIndex() {
        val script = "दश + अम् विंशति + औट् त्रिंशत् + शस् च द्वि + टा स्था + लोट् + सिप् ।"
        val className = "SanskritListIndexIntegrationTest"
        val clazz = BytecodeCompiler.compileAndLoad(script, className)
        val executeMethod = clazz.getMethod("execute")

        val resultVariables = executeMethod.invoke(null) as? Map<*, *>
        assertNotNull(resultVariables)

        val finalResultVal = resultVariables["योग-1"] as? SanskritValue.Sankhya
        assertNotNull(finalResultVal)
        assertEquals(20L, finalResultVal.value)
    }

    @Test
    fun testCompileAndRunListSlice() {
        val script = "दश + अम् विंशति + औट् त्रिंशत् + शस् चत्वारिंशत् + शस् च द्वि + टा त्रि + ङे भज् + लोट् + सिप् ।"
        val className = "SanskritListSliceIntegrationTest"
        val clazz = BytecodeCompiler.compileAndLoad(script, className)
        val executeMethod = clazz.getMethod("execute")

        val resultVariables = executeMethod.invoke(null) as? Map<*, *>
        assertNotNull(resultVariables)

        val finalResultVal = resultVariables["योग-1"] as? SanskritValue.Suchi
        assertNotNull(finalResultVal)
        assertEquals(2, finalResultVal.items.size)
        assertEquals(20L, (finalResultVal.items[0] as SanskritValue.Sankhya).value)
        assertEquals(30L, (finalResultVal.items[1] as SanskritValue.Sankhya).value)
    }

    @Test
    fun testCompileAndRunListReverse() {
        val script = "एक + अम् द्वि + औट् च प्रति + वृत् + लोट् + सिप् ।"
        val className = "SanskritListReverseIntegrationTest"
        val clazz = BytecodeCompiler.compileAndLoad(script, className)
        val executeMethod = clazz.getMethod("execute")

        val resultVariables = executeMethod.invoke(null) as? Map<*, *>
        assertNotNull(resultVariables)

        val finalResultVal = resultVariables["योग-1"] as? SanskritValue.Suchi
        assertNotNull(finalResultVal)
        assertEquals(2, finalResultVal.items.size)
        assertEquals(2L, (finalResultVal.items[0] as SanskritValue.Sankhya).value)
        assertEquals(1L, (finalResultVal.items[1] as SanskritValue.Sankhya).value)
    }

    @Test
    fun testCompileAndRunListFold() {
        val script = "द्वि + अम् त्रि + औट् च सङ्ख्यागुणन + टा पञ्च + ङे सम् + क्षिप् + लोट् + सिप् ।"
        val className = "SanskritListFoldIntegrationTest"
        val clazz = BytecodeCompiler.compileAndLoad(script, className)
        val executeMethod = clazz.getMethod("execute")

        val resultVariables = executeMethod.invoke(null) as? Map<*, *>
        assertNotNull(resultVariables)

        val finalResultVal = resultVariables["योग-1"] as? SanskritValue.Sankhya
        assertNotNull(finalResultVal)
        assertEquals(30L, finalResultVal.value)
    }

    @Test
    fun testCompileAndRunIfBranch() {
        val script = "सत्य + ङसिँ सङ्ख्यायोजन + टा सङ्ख्यावियोग + ङे पञ्च + अम् दश + अम् च ज्ञा + लोट् + सिप् ।"
        val className = "SanskritIfBranchIntegrationTest"
        val clazz = BytecodeCompiler.compileAndLoad(script, className)
        val executeMethod = clazz.getMethod("execute")

        val resultVariables = executeMethod.invoke(null) as? Map<*, *>
        assertNotNull(resultVariables)

        val finalResultVal = resultVariables["योग-1"] as? SanskritValue.Sankhya
        assertNotNull(finalResultVal)
        assertEquals(15L, finalResultVal.value)
    }

    @Test
    fun testCompileAndRunWhileLoop() {
        val script = "युग्मत्व + अम् सङ्ख्यावियोग + टा पञ्च + ङे द्वि + सुँ वृत् + लोट् + सिप् ।"
        val className = "SanskritWhileLoopIntegrationTest"
        val clazz = BytecodeCompiler.compileAndLoad(script, className)
        val executeMethod = clazz.getMethod("execute")

        val resultVariables = executeMethod.invoke(null) as? Map<*, *>
        assertNotNull(resultVariables)

        val finalResultVal = resultVariables["योग-1"] as? SanskritValue.Sankhya
        assertNotNull(finalResultVal)
        assertEquals(5L, finalResultVal.value)
    }

    @Test
    fun testCompileAndRunListFlatten() {
        val script = "एक + अम् द्वि + औट् च दा + लोट् + सिप् ।\nफल + अम् त्रि + शस् च तन् + णिच् + लोट् + सिप् ।"
        val className = "SanskritListFlattenIntegrationTest"
        val clazz = BytecodeCompiler.compileAndLoad(script, className)
        val executeMethod = clazz.getMethod("execute")

        val resultVariables = executeMethod.invoke(null) as? Map<*, *>
        assertNotNull(resultVariables)

        val finalResultVal = resultVariables["उक्ति-२/योग-1"] as? SanskritValue.Suchi
        assertNotNull(finalResultVal)
        assertEquals(3, finalResultVal.items.size)
        assertEquals(1L, (finalResultVal.items[0] as SanskritValue.Sankhya).value)
        assertEquals(2L, (finalResultVal.items[1] as SanskritValue.Sankhya).value)
        assertEquals(3L, (finalResultVal.items[2] as SanskritValue.Sankhya).value)
    }

    @Test
    fun testCompileAndRunListContains() {
        val script = "एक + अम् द्वि + औट् च द्वि + टा अस् + लोट् + सिप् ।"
        val className = "SanskritListContainsIntegrationTest"
        val clazz = BytecodeCompiler.compileAndLoad(script, className)
        val executeMethod = clazz.getMethod("execute")

        val resultVariables = executeMethod.invoke(null) as? Map<*, *>
        assertNotNull(resultVariables)

        val finalResultVal = resultVariables["योग-1"] as? SanskritValue.Satya
        assertNotNull(finalResultVal)
        assertEquals(true, finalResultVal.boolean)
    }

    @Test
    fun testCompileAndRunForEach() {
        val script = "एक + अम् द्वि + औट् च सङ्ख्यायोजन + टा दश + ङे अनु + वृत् + लोट् + सिप् ।"
        val className = "SanskritForEachIntegrationTest"
        val clazz = BytecodeCompiler.compileAndLoad(script, className)
        val executeMethod = clazz.getMethod("execute")

        val resultVariables = executeMethod.invoke(null) as? Map<*, *>
        assertNotNull(resultVariables)

        val finalResultVal = resultVariables["योग-1"] as? SanskritValue.Sankhya
        assertNotNull(finalResultVal)
        assertEquals(13L, finalResultVal.value)
    }

    @Test
    fun testCompileAndRunFibonacci() {
        val script = """
            नव + अम् सृज् + टा वृत् + लोट् + सिप् ।
            प्रथमफल + अम् प्रेष् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val className = "SanskritFibonacciIntegrationTest"
        val clazz = BytecodeCompiler.compileAndLoad(script, className)
        val executeMethod = clazz.getMethod("execute")

        val resultVariables = executeMethod.invoke(null) as? Map<*, *>
        assertNotNull(resultVariables)

        val loopResult = resultVariables["उक्ति-१/योग-1"] as? SanskritValue.Suchi
        assertNotNull(loopResult, "Loop result should be a list")
        assertEquals(10, loopResult.items.size)
        assertEquals(1L, (loopResult.items[0] as SanskritValue.Sankhya).value)
        assertEquals(1L, (loopResult.items[1] as SanskritValue.Sankhya).value)
        assertEquals(2L, (loopResult.items[2] as SanskritValue.Sankhya).value)
        assertEquals(3L, (loopResult.items[3] as SanskritValue.Sankhya).value)
        assertEquals(5L, (loopResult.items[4] as SanskritValue.Sankhya).value)
        assertEquals(8L, (loopResult.items[5] as SanskritValue.Sankhya).value)
        assertEquals(13L, (loopResult.items[6] as SanskritValue.Sankhya).value)
        assertEquals(21L, (loopResult.items[7] as SanskritValue.Sankhya).value)
        assertEquals(34L, (loopResult.items[8] as SanskritValue.Sankhya).value)
        assertEquals(55L, (loopResult.items[9] as SanskritValue.Sankhya).value)

        val printResult = resultVariables["उक्ति-२/योग-1"] as? SanskritValue.Shabda
        assertNotNull(printResult)
        assertTrue(printResult.text.contains("Simulated dispatch"))
        assertTrue(printResult.text.contains("पञ्चपञ्चाशत्"))
    }
}
