package dev.panini.compiler

import dev.panini.execution.SanskritValue
import java.lang.reflect.Modifier
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class CompilerImprovementsTest {

    @Test
    fun testStructuredDiagnosticsMorphologyError() {
        // Invalid syntax: missing spacing/plus or invalid morphology
        val script = """
            हे यन्त्र + सुँ, एक + अम् द्वि + औट् त्रि + शस् च युज् + णिच् + लोट् + सिप् ।
            // This is a comment
            Invalid morphology string here + and + bad
        """.trimIndent()

        val exception = assertFailsWith<PaniniCompilationException> {
            BytecodeCompiler.compile(script, "ErrorDemoClass")
        }

        assertEquals(3, exception.lineIndex, "Error should be flagged on line 3")
        assertEquals("Invalid morphology string here + and + bad", exception.sourceLine)
        assertEquals(CompilerErrorKind.MORPHOLOGY_ERROR, exception.errorKind)
    }

    @Test
    fun testStructuredDiagnosticsMissingInput() {
        // 'युज्' addition operation requires KARMAN, but we supply only KARTR (राम + सुँ)
        val script = """
            राम + सुँ युज् + णिच् + लोट् + सिप् ।
        """.trimIndent()

        val exception = assertFailsWith<PaniniCompilationException> {
            BytecodeCompiler.compile(script, "MissingInputDemoClass")
        }

        assertEquals(1, exception.lineIndex, "Error should be flagged on line 1")
        assertEquals(CompilerErrorKind.MISSING_INPUT, exception.errorKind)
    }

    @Test
    fun testConstantPoolingVerification() {
        val script = "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।"
        val className = "SanskritConstantPooledTest"

        val clazz = BytecodeCompiler.compileAndLoad(script, className)

        // Find all fields declared in the compiled class
        val fields = clazz.declaredFields
        val constantFields = fields.filter { field ->
            val modifiers = field.modifiers
            Modifier.isPrivate(modifiers) &&
            Modifier.isStatic(modifiers) &&
            Modifier.isFinal(modifiers) &&
            field.type == SanskritValue::class.java
        }

        // We expect exactly two constant fields: one for 'एक' (1) and one for 'द्वि' (2)
        assertEquals(2, constantFields.size, "Should have exactly 2 static final SanskritValue constant fields")
        
        // Assert field names match our naming format
        constantFields.forEach { field ->
            assertTrue(field.name.startsWith("sanskrit_const_"), "Constant field name should start with sanskrit_const_")
        }
    }
}
