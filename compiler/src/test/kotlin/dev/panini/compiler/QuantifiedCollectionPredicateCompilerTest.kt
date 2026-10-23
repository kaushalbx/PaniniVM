package dev.panini.compiler

import dev.panini.execution.SanskritValue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue

class QuantifiedCollectionPredicateCompilerTest {
    private val any = "सूची + ङस् कश्चित् सदस्य + सुँ द्वि + ङसिँ अधिक + सुँ असँ + लट् + तिप् ।"
    private val all = "सूची + ङस् प्रत्येकः सदस्य + सुँ शून्य + ङसिँ अधिक + सुँ असँ + लट् + तिप् ।"
    private val allPartial = "सूची + ङस् प्रत्येकः सदस्य + सुँ एक + ङसिँ अधिक + सुँ असँ + लट् + तिप् ।"

    @Test
    fun `native any and all predicates match typed collection semantics`() {
        val values = mapOf(
            "सूची" to SanskritValue.Suchi(listOf(
                SanskritValue.Sankhya(1, "एक"),
                SanskritValue.Sankhya(2, "द्वि"),
                SanskritValue.Sankhya(3, "त्रि"),
            )),
        )

        assertEquals(true, execute(any, "CompiledAny", values).boolean)
        assertEquals(true, execute(all, "CompiledAll", values).boolean)
        assertEquals(false, execute(allPartial, "CompiledPartialAll", values).boolean)
        assertEquals(false, execute(any, "CompiledNoAny", mapOf(
            "सूची" to SanskritValue.Suchi(listOf(
                SanskritValue.Sankhya(1, "एक"),
                SanskritValue.Sankhya(2, "द्वि"),
            )),
        )).boolean)
        assertTrue(
            CompilerInstruction.Collection(CollectionOperator.QUANTIFIER_SNAPSHOT) in
                CompilerFrontend.lower(any, "DirectAnyIr").entryPoint,
        )
    }

    @Test
    fun `empty collections use existential and universal identities`() {
        val emptyList = mapOf("सूची" to SanskritValue.Suchi(emptyList()))
        val emptyGroup = mapOf("सूची" to SanskritValue.Gana(emptyList()))

        assertEquals(false, execute(any, "EmptyListAny", emptyList).boolean)
        assertEquals(true, execute(all, "EmptyListAll", emptyList).boolean)
        assertEquals(false, execute(any, "EmptyGroupAny", emptyGroup).boolean)
        assertEquals(true, execute(all, "EmptyGroupAll", emptyGroup).boolean)
    }

    @Test
    fun `quantifier rejects scalar collection values`() {
        val generated = BytecodeCompiler.compileAndLoad(any, "ScalarAny")
        assertFailsWith<java.lang.reflect.InvocationTargetException> {
            generated.getMethod("execute", Map::class.java).invoke(
                null,
                mapOf("सूची" to SanskritValue.Sankhya(1, "एक")),
            )
        }
    }

    @Test
    fun `typed member mismatch and non truth predicates are rejected`() {
        val typedMismatch = BytecodeCompiler.compileAndLoad(any, "TypedMismatchAny")
        assertFailsWith<java.lang.reflect.InvocationTargetException> {
            typedMismatch.getMethod("execute", Map::class.java).invoke(
                null,
                mapOf("सूची" to SanskritValue.Gana(listOf(SanskritValue.Shabda("त्रि")))),
            )
        }
        assertFailsWith<CompilerUnsupportedException> {
            CompilerFrontend.lower(
                "सूची + ङस् कश्चित् सदस्य + सुँ सदस्य + अम् गण् + लोट् + सिप् ।",
                "NonTruthAny",
            )
        }
    }

    @Test
    fun `quantified predicate composes with native conditional lowering and restores member scope`() {
        val source = "यदि ${any.removeSuffix(" ।")} तर्हि नव अन्यथा शून्य ।"
        val initial = mapOf(
            "सूची" to SanskritValue.Suchi(listOf(SanskritValue.Sankhya(3, "त्रि"))),
            "सदस्य" to SanskritValue.Sankhya(9, "नव"),
        )
        val generated = BytecodeCompiler.compileAndLoad(source, "ConditionalAny")
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<String, SanskritValue>

        assertEquals(9L, assertIs<SanskritValue.Sankhya>(values.getValue("LastResult")).value)
        assertEquals(initial.getValue("सदस्य"), values.getValue("सदस्य"))
    }

    private fun execute(
        source: String,
        name: String,
        initial: Map<String, SanskritValue>,
    ): SanskritValue.Satya {
        val generated = BytecodeCompiler.compileAndLoad(source, name)
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<String, SanskritValue>
        return assertIs(values.getValue("LastResult"))
    }
}
