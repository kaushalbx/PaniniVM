package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NaturalNeighbourIterationTest {
    @Test
    fun `direct singular relative action adds each number to only its next number`() {
        val source = """
            एक + ङस् द्वि + ओस् त्रि + आम् चतुर् + आम् च दत्तसूची + सुँ नाम सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।
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
            एक + ङस् द्वि + ओस् त्रि + आम् चतुर् + आम् च दत्तसूची + सुँ नाम सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।
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
    fun `foreach binds the next member and skips the final boundary`() {
        val source = """
            एक + ङस् द्वि + ओस् त्रि + आम् चतुर् + आम् च दत्तसूची + सुँ नाम सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।
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
            एक + ङस् द्वि + ओस् त्रि + आम् चतुर् + आम् च दत्तसूची + सुँ नाम सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।
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
            एक + ङस् द्वि + ओस् त्रि + आम् च दत्तसूची + सुँ नाम सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।
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
            एक + ङस् द्वि + ओस् त्रि + आम् च दत्तसूची + सुँ नाम सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।
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
