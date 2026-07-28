package dev.panini.execution

import dev.panini.actions.numeric.AdditionAction
import dev.panini.actions.numeric.MultiplicationAction
import dev.panini.actions.numeric.NumericDhatuActionGrantha
import dev.panini.actions.numeric.SubtractionAction
import dev.panini.core.Karaka
import dev.panini.sutra.runtime.SutraArthaValue
import dev.panini.sutra.runtime.SutraBlueprintGranthaTextCodec
import dev.panini.sutra.runtime.SutraBlueprintGranthaTextDecoding
import dev.panini.sutra.runtime.SutraBlueprintGranthaTextEncoding
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class NumericDhatuActionGranthaTest {
    @BeforeTest
    fun initializeNumbers() {
        val words = mapOf(8L to "अष्ट", 12L to "द्वादश", 20L to "विंशतिः")
        SankhyaResultRenderer.defaultRenderer = SankhyaResultRenderer(words::get)
    }

    @Test
    fun `migrated action grantha is canonical and lossless`() {
        val grantha = NumericDhatuActionGrantha.blueprint

        assertEquals(3, grantha.sutras.size)
        assertEquals(grantha.sutras.map { it.id }.toSet(), grantha.exports)
        assertTrue(grantha.sutras.all { it.artha.kind == "dhatu-action" })
        assertEquals(
            listOf("ADD", "SUBTRACT", "MULTIPLY"),
            grantha.sutras.map {
                assertIs<SutraArthaValue.Symbol>(it.artha.fields["operator"]).name
            },
        )

        val text = assertIs<SutraBlueprintGranthaTextEncoding.Success>(
            SutraBlueprintGranthaTextCodec.encode(grantha),
        ).text
        val decoded = assertIs<SutraBlueprintGranthaTextDecoding.Success>(
            SutraBlueprintGranthaTextCodec.decode(text),
        ).grantha

        assertEquals(grantha, decoded)
    }

    @Test
    fun `numeric action blueprints drive existing dhatu execution`() {
        val expression = ExecutionExpression.Coordination(
            listOf(
                ExecutionExpression.sankhya(10, "दश"),
                ExecutionExpression.sankhya(2, "द्वि"),
            ),
        )
        val context = ExecutionContext(
            bindings = mapOf(Karaka.KARMAN to expression),
        )

        val addition = assertIs<ExecutionResult.Success>(
            AdditionAction.execute(context, AdditionAction.numericOp()),
        )
        val subtraction = assertIs<ExecutionResult.Success>(
            SubtractionAction.execute(context, SubtractionAction.numericOp()),
        )
        val multiplication = assertIs<ExecutionResult.Success>(
            MultiplicationAction.execute(context, MultiplicationAction.numericOp()),
        )

        assertEquals(12L, assertIs<SanskritValue.Sankhya>(addition.typedValue).value)
        assertEquals(8L, assertIs<SanskritValue.Sankhya>(subtraction.typedValue).value)
        assertEquals(20L, assertIs<SanskritValue.Sankhya>(multiplication.typedValue).value)
        assertTrue(addition.trace.first().contains(AdditionAction.blueprint.id.value))
        assertTrue(subtraction.trace.first().contains(SubtractionAction.blueprint.id.value))
        assertTrue(multiplication.trace.first().contains(MultiplicationAction.blueprint.id.value))
    }
}
