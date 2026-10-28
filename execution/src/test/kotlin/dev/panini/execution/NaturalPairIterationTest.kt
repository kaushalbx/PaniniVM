package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class NaturalNeighbourIterationTest {
    private fun relativeScript(action: String) = """
        एक + सुँ द्वि + औ त्रि + जस् च + इति दत्तसूची + सुँ नाम सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।
        दत्तसूची + ङस् प्रत्येक + टाप् + अम् सङ्ख्या + अम्
        तद् + ङस् + लुक् - उत्तर + टाप् + भिस्
        $action ।
    """.trimIndent()

    @Test
    fun `relative numeric operands reuse the existing multiplication action`() {
        fun values(action: String) = PaniniVM().evalScript(relativeScript(action)).also { results ->
            assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        }.filterIsInstance<ExecutionResult.Success>().drop(1)
            .map { assertIs<SanskritValue.Sankhya>(it.typedValue).value }

        assertEquals(listOf(2L, 3L, 6L), values("गण + णिच् + लोट् + सिप्"))
    }

    @Test
    fun `relative word operands reuse the existing counting action`() {
        val source = """
            राम + ङस् सीता + ङस् च दत्तसूची + सुँ नाम शब्द + आम् सूची + सुँ असँ + लट् + तिप् ।
            दत्तसूची + ङस् प्रत्येक + अम् शब्द + अम्
            तद् + ङस् + लुक् - उत्तर + भिस् शब्द + भिस्
            गण + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val results = PaniniVM().evalScript(source)

        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        assertEquals(2L, assertIs<SanskritValue.Sankhya>(
            results.filterIsInstance<ExecutionResult.Success>().last().typedValue).value)
    }

    @Test
    fun `direct singular relative action adds each number to only its next number`() {
        val source = """
            एक + सुँ द्वि + औ त्रि + जस् चतुर् + जस् च + इति दत्तसूची + सुँ नाम सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।
            दत्तसूची + ङस् प्रत्येक + टाप् + अम् सङ्ख्या + अम्
            तद् + ङस् + लुक् - उत्तर + टाप् + टा सङ्ख्या + टा
            युज् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val results = PaniniVM().evalScript(source)

        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        assertEquals(listOf(3L, 5L, 7L), results.filterIsInstance<ExecutionResult.Success>()
            .drop(1).map { (it.typedValue as SanskritValue.Sankhya).value })
    }

    @Test
    fun `direct singular relative action adds each number to only its previous number`() {
        val source = """
            एक + सुँ द्वि + औ त्रि + जस् चतुर् + जस् च + इति दत्तसूची + सुँ नाम सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।
            दत्तसूची + ङस् प्रत्येक + टाप् + अम् सङ्ख्या + अम्
            तद् + ङस् + लुक् - पूर्व + टाप् + टा सङ्ख्या + टा
            युज् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val results = PaniniVM().evalScript(source)

        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        assertEquals(listOf(3L, 5L, 7L), results.filterIsInstance<ExecutionResult.Success>()
            .drop(1).map { (it.typedValue as SanskritValue.Sankhya).value })
    }

    @Test
    fun `omitted repeated noun and finite result continuation execute naturally`() {
        val source = """
            एक + सुँ द्वि + औ त्रि + जस् च + इति दत्तसूची + सुँ नाम सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।
            दत्तसूची + ङस् प्रत्येक + टाप् + अम् सङ्ख्या + अम्
            तद् + ङस् + लुक् - उत्तर + टाप् + भिस्
            युज् + णिच् + लोट् + सिप् ततः मुद्र् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val results = PaniniVM().evalScript(source)

        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        assertEquals(listOf("त्रीणि", "चत्वारि", "पञ्च"), results.filterIsInstance<ExecutionResult.Success>()
            .filter { it.outputKind == OutputKind.CONSOLE }.map { it.value })
    }

    @Test
    fun `relative action reports a non-list source precisely`() {
        val source = """
            एक + अम् दत्तसूची + ङि स्था + णिच् + लोट् + सिप् ।
            दत्तसूची + ङस् प्रत्येक + टाप् + अम् सङ्ख्या + अम्
            तद् + ङस् + लुक् - उत्तर + टाप् + टा
            युज् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val failure = PaniniVM().evalScript(source).last() as ExecutionResult.Failure

        assertTrue(failure.message.contains("requires a list"), failure.message)
    }

    @Test
    fun `foreach binds the next member and skips the final boundary`() {
        val source = """
            एक + सुँ द्वि + औ त्रि + जस् चतुर् + जस् च + इति दत्तसूची + सुँ नाम सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।
            दत्तसूची + ङस् प्रत्येक + अम् सदस्य + अम्
            तद् + ङस् क्रम + अम् तद् + ङस् उत्तर + अम् च
            ग्रह् + क्त्वा
            उत्तर + अम् मुद्र् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val results = PaniniVM().evalScript(source)

        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        assertEquals(listOf("द्वि", "त्रि", "चतुर्"), results.filterIsInstance<ExecutionResult.Success>()
            .filter { it.outputKind == OutputKind.CONSOLE }.map { it.value })
    }

    @Test
    fun `foreach binds the previous member and skips the initial boundary`() {
        val source = """
            एक + सुँ द्वि + औ त्रि + जस् चतुर् + जस् च + इति दत्तसूची + सुँ नाम सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।
            दत्तसूची + ङस् प्रत्येक + अम् सदस्य + अम्
            तद् + ङस् क्रम + अम् तद् + ङस् पूर्व + अम् च
            ग्रह् + क्त्वा
            पूर्व + अम् मुद्र् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val results = PaniniVM().evalScript(source)

        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        assertEquals(listOf("एक", "द्वि", "त्रि"), results.filterIsInstance<ExecutionResult.Success>()
            .filter { it.outputKind == OutputKind.CONSOLE }.map { it.value })
    }

    @Test
    fun `foreach binds all following members including an empty final suffix`() {
        val source = """
            एक + सुँ द्वि + औ त्रि + जस् च + इति दत्तसूची + सुँ नाम सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।
            दत्तसूची + ङस् प्रत्येक + अम् सदस्य + अम्
            तद् + ङस् क्रम + अम् तद् + ङस् उत्तर + शस् च
            ग्रह् + क्त्वा
            उत्तर + शस् मुद्र् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val results = PaniniVM().evalScript(source)

        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        assertEquals(listOf("[द्वि, त्रि]", "[त्रि]", "[]"), results.filterIsInstance<ExecutionResult.Success>()
            .filter { it.outputKind == OutputKind.CONSOLE }.map { it.value })
    }

    @Test
    fun `foreach binds all preceding members including an empty initial prefix`() {
        val source = """
            एक + सुँ द्वि + औ त्रि + जस् च + इति दत्तसूची + सुँ नाम सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।
            दत्तसूची + ङस् प्रत्येक + अम् सदस्य + अम्
            तद् + ङस् क्रम + अम् तद् + ङस् पूर्व + शस् च
            ग्रह् + क्त्वा
            पूर्व + शस् मुद्र् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val results = PaniniVM().evalScript(source)

        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        assertEquals(listOf("[]", "[एक]", "[एक, द्वि]"), results.filterIsInstance<ExecutionResult.Success>()
            .filter { it.outputKind == OutputKind.CONSOLE }.map { it.value })
    }
}
