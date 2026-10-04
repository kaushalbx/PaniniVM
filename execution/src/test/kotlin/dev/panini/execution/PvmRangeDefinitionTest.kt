package dev.panini.execution

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class PvmRangeDefinitionTest {
    @Test
    fun `lotto example uses natural choice and exclusion morphology`() {
        val results = PaniniVM().evalFile(File("examples/arithmetic/lotto_guesser.pvm"))

        assertTrue(results.none { it is ExecutionResult.Failure }, results.toString())
        val successes = results.filterIsInstance<ExecutionResult.Success>()
        val main = successes.mapNotNull { it.typedValue as? SanskritValue.Suchi }.last()
        val bonus = successes.mapNotNull { it.typedValue as? SanskritValue.Sankhya }.last()
        val mainNumbers = main.items.map { assertIs<SanskritValue.Sankhya>(it).value }
        assertEquals(6, mainNumbers.distinct().size)
        assertTrue(bonus.value !in mainNumbers)
        assertTrue((mainNumbers + bonus.value).all { it in 1L..48L })
    }

    @Test
    fun `parses one scoped inclusive range`() {
        val statement = PvmScript.parse("एक + ङसिँ दशन् + शस् परि + अन्त + अम् इति सीमा + सुँ ।").single()
        val definition = assertIs<PvmScriptStatement.RangeDefinition>(statement, statement.toString())

        assertEquals(1L, definition.range.minimum.value)
        assertEquals(10L, definition.range.maximum.value)
    }

    @Test
    fun `range declaration becomes a typed discourse referent`() {
        val statements = PvmScript.parse(
            "एक + ङसिँ दशन् + शस् परि + अन्त + अम् इति सीमा + सुँ ।",
        )
        val discourse = PvmDiscourseContext.from(statements)

        assertEquals(1L, discourse.activeRange?.minimum?.value)
        assertEquals(10L, discourse.activeRange?.maximum?.value)
        assertEquals(discourse.activeRange, discourse.valueEnvironment().values[ACTIVE_RANGE_NAME])
    }

    @Test
    fun `locative upper bound is rejected as a range declaration`() {
        val source = "एक + ङसिँ दश + ङि इति सीमा + सुँ ।"

        assertIs<PvmScriptStatement.Sentence>(PvmScript.parse(source).single())
        assertTrue(PrakriyaScriptValidator.validate(source).any { "पर्यन्त" in it.message })
    }

    @Test
    fun `scoped range drives choice and quoted output but not ordinary printing`() {
        val results = PaniniVM().evalScript(
            """
            एक + ङसिँ दशन् + शस् परि + अन्त + अम् इति सीमा + सुँ ।
            सङ्ख्या + अम् चिञ् + श्नु + लोट् + सिप् ततः रहस्य + ङे दा + लोट् + सिप् ।
            सङ्ख्या + अम् अनुमिनु + लोट् + सिप् इति मुद्र् + णिच् + लोट् + सिप् ।
            समाप्ताः + अम् मुद्र् + णिच् + लोट् + सिप् ।
            """.trimIndent(),
        )
        val successes = results.filterIsInstance<ExecutionResult.Success>()

        assertTrue(assertIs<SanskritValue.Sankhya>(successes.first().typedValue).value in 1L..10L)
        assertEquals(
            listOf("एकतः दशपर्यन्तं सङ्ख्याम् अनुमिनु", "समाप्ताः"),
            successes.filter { it.outputKind == OutputKind.CONSOLE }.map { it.value },
        )
    }

    @Test
    fun `exclusion absolutive removes collection values from range choice`() {
        val results = PaniniVM().evalScript(
            """
            एक + ङसिँ द्वि + शस् परि + अन्त + अम् इति सीमा + सुँ ।
            एक + अम् क्षिप् + णिच् + लोट् + सिप् ततः क्षिप् + घञ् + ङस् फल + अम् क्रम + ङि स्था + णिच् + लोट् + सिप् ।
            क्रम + अम् वृज् + णिच् + क्त्वा चिञ् + श्नु + लोट् + सिप् ।
            """.trimIndent(),
        )

        assertTrue(results.none { it is ExecutionResult.Failure }, results.toString())
        val choice = results.filterIsInstance<ExecutionResult.Success>().last()
        assertEquals(2L, assertIs<SanskritValue.Sankhya>(choice.typedValue).value)
    }
}
