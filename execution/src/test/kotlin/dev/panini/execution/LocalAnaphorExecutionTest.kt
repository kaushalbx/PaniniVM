package dev.panini.execution

import kotlin.test.*

class LocalAnaphorExecutionTest {
    @Test fun `conditional compares a resolved grammatical participant`() {
        val results = PaniniVM().evalScript("""
            परीक्षण + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            यदि तद् + सुँ नवन् + टा सम + सुँ असँ + लट् + तिप् तर्हि एक + अम् मुद्र् + णिच् + लोट् + सिप् अन्यथा द्वि + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            नवन् + शस् परीक्षण + टा कृ + लोट् + सिप् ।
        """.trimIndent())
        assertTrue(results.none { it is ExecutionResult.Failure }, results.toString())
        assertEquals("एक", assertIs<ExecutionResult.Success>(results.last()).value)
    }

    @Test fun `loop resolves the current participant after mutation`() {
        val results = PaniniVM().evalScript("""
            परीक्षण + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            यावत् तद् + सुँ नवन् + टा सम + सुँ असँ + लट् + तिप् तावत् द्वि + अम् तद् + ङे दा + लोट् + सिप् ।
            मान + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            नवन् + शस् परीक्षण + टा कृ + लोट् + सिप् ।
        """.trimIndent())
        assertTrue(results.none { it is ExecutionResult.Failure }, results.toString())
        assertEquals(1L, results.filterIsInstance<ExecutionResult.Success>().single { it.loopOutcome != null }.iterationCount)
        assertEquals("द्वि", assertIs<ExecutionResult.Success>(results.last()).value)
    }

    @Test fun `pronoun assignment updates the selected participant cell`() {
        val results = PaniniVM().evalScript("""
            परीक्षण + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            द्वि + अम् तद् + ङे दा + लोट् + सिप् ।
            मान + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            नवन् + शस् परीक्षण + टा कृ + लोट् + सिप् ।
        """.trimIndent())
        assertTrue(results.none { it is ExecutionResult.Failure }, results.toString())
        assertEquals("द्वि", assertIs<ExecutionResult.Success>(results.last()).value)
    }

    @Test fun `tad reads a grammatical procedure participant`() {
        val results = PaniniVM().evalScript("""
            परीक्षण + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            तद् + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            नवन् + शस् परीक्षण + टा कृ + लोट् + सिप् ।
        """.trimIndent())
        assertTrue(results.none { it is ExecutionResult.Failure }, results.toString())
        assertEquals("नवन्", assertIs<ExecutionResult.Success>(results.last()).value)
    }

    @Test fun `missing antecedent fails instead of printing a literal pronoun`() {
        val results = PaniniVM().evalScript("तद् + अम् मुद्र् + णिच् + लोट् + सिप् ।")
        val failure = assertIs<ExecutionResult.Failure>(results.last())
        assertEquals(ExecutionError.INVALID_VALUE, failure.error)
    }

    @Test fun `two compatible participants cannot be resolved by recency`() {
        val results = PaniniVM().evalScript("""
            परीक्षण + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            मूल्य + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            तद् + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            नवन् + शस् द्वि + औट् च परीक्षण + टा कृ + लोट् + सिप् ।
        """.trimIndent())
        val failures = results.filterIsInstance<ExecutionResult.Failure>()
        assertTrue(failures.isNotEmpty(), results.toString())
        assertTrue(failures.any { it.toString().contains("Ambiguous तद् antecedent") }, results.toString())
    }
}
