package dev.panini.compiler

import dev.panini.execution.ExecutionResult
import dev.panini.execution.PaniniVM
import dev.panini.execution.SanskritValue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.test.assertFailsWith

class CollectionReductionTest {
    private val declaration =
        "एक + ङस् द्वि + ओस् त्रि + आम् चतुर् + आम् च सूची + सुँ असँ + लट् + तिप् ।"

    @Test fun `first member reduction supports reusable arithmetic and conditional reducers`() {
        val reducers = listOf(
            "सञ्चित + अम् सदस्य + अम् च युज् + णिच् + लोट् + सिप्" to 10L,
            "सञ्चित + अम् सदस्य + अम् च गण + णिच् + लोट् + सिप्" to 24L,
            ("यदि सदस्य + सुँ सञ्चित + ङसिँ अधिक + सुँ असँ + लट् + तिप् " +
                "तर्हि सदस्य + सुँ अन्यथा सञ्चित + सुँ") to 4L,
        )
        reducers.forEachIndexed { index, (body, expected) ->
            val source = "$declaration\nसूची + ङस् प्रथम + अम् आरभ्य सञ्चित + अम् सदस्य + अम् प्रति $body ।"
            val interpreted = PaniniVM().evalScript(source)
            assertTrue(interpreted.all { it is ExecutionResult.Success }, interpreted.toString())
            assertEquals(expected, assertIs<SanskritValue.Sankhya>(
                assertIs<ExecutionResult.Success>(interpreted.last()).typedValue).value)
            val values = compiled(source, "CollectionReduction$index")
            assertEquals(expected, assertIs<SanskritValue.Sankhya>(values.getValue("LastResult")).value)
            assertFalse("सञ्चित" in values)
            assertFalse("सदस्य" in values)
        }
    }

    @Test fun `one member reduction returns that member without invoking its body`() {
        val source = "नवन् + ङस् सूची + सुँ असँ + लट् + तिप् ।\n" +
            "सूची + ङस् प्रथम + अम् आरभ्य सञ्चित + अम् सदस्य + अम् प्रति " +
            "सञ्चित + अम् सदस्य + अम् च युज् + णिच् + लोट् + सिप् ।"
        assertEquals(9L, assertIs<SanskritValue.Sankhya>(
            assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source).last()).typedValue).value)
        assertEquals(9L, assertIs<SanskritValue.Sankhya>(
            compiled(source, "SingleMemberReduction").getValue("LastResult")).value)
    }

    @Test fun `first member reduction rejects an empty list in both backends`() {
        val source = "सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।\n" +
            "सूची + ङस् प्रथम + अम् आरभ्य सञ्चित + अम् सदस्य + अम् प्रति " +
            "सञ्चित + अम् सदस्य + अम् च युज् + णिच् + लोट् + सिप् ।"
        val interpreted = PaniniVM().evalScript(source)
        assertIs<ExecutionResult.Failure>(interpreted.last())
        val generated = BytecodeCompiler.compileAndLoad(source, "EmptyCollectionReduction")
        assertFailsWith<java.lang.reflect.InvocationTargetException> {
            generated.getMethod("execute").invoke(null)
        }
    }

    private fun compiled(source: String, name: String): Map<String, SanskritValue> {
        val generated = BytecodeCompiler.compileAndLoad(source, name)
        @Suppress("UNCHECKED_CAST")
        return generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
    }
}
