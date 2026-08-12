package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ExplicitSamjnaSignatureTest {
    private val vm = PaniniVM()

    @Test
    fun `named call arguments bind by parameter name instead of source order`() {
        val results = vm.evalScript(
            """
            व्यवकलन + ल्युट् + सुँ ।
            वाम + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            दक्षिण + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            सङ्ख्या + सुँ इति परिणाम + सुँ ।
            वाम + अम् दक्षिण + अम् च वि + युज् + णिच् + लोट् + सिप् ॥

            दक्षिण + ङस् द्वि + अम् वाम + ङस् पञ्च + अम् व्यवकलन + ल्युट् + टा कृ + लोट् + सिप् ।
            """.trimIndent(),
        )

        val success = results.filterIsInstance<ExecutionResult.Success>().last()
        assertEquals(3, assertIs<SanskritValue.Sankhya>(success.typedValue).value, results.toString())
    }

    @Test
    fun `named call rejects unknown and duplicate parameter names`() {
        val definition = """
            योजन + ल्युट् + सुँ ।
            वाम + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            दक्षिण + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            सङ्ख्या + सुँ इति परिणाम + सुँ ।
            वाम + अम् दक्षिण + अम् च युज् + णिच् + लोट् + सिप् ॥
        """.trimIndent()
        val unknown = vm.evalScript(
            definition + "\nअज्ञात + ङस् द्वि + अम् दक्षिण + ङस् त्रि + अम् योजन + ल्युट् + टा कृ + लोट् + सिप् ।",
        )
        val duplicate = vm.evalScript(
            definition + "\nवाम + ङस् द्वि + अम् वाम + ङस् त्रि + अम् योजन + ल्युट् + टा कृ + लोट् + सिप् ।",
        )

        assertTrue(assertIs<ExecutionResult.Failure>(unknown.last()).message.contains("Unknown parameters"))
        assertTrue(assertIs<ExecutionResult.Failure>(duplicate.last()).message.contains("Duplicate arguments"))
    }

    @Test
    fun `named typed parameters are bound and declaration sentences are not executed`() {
        val script = """
            योजन + ल्युट् + सुँ ।
            वाम + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            दक्षिण + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            सङ्ख्या + सुँ इति परिणाम + सुँ ।
            वाम + अम् दक्षिण + अम् च युज् + णिच् + लोट् + सिप् ॥

            द्वि + अम् त्रि + अम् च योजन + ल्युट् + टा कृ + लोट् + सिप् ।
        """.trimIndent()

        val results = vm.evalScript(script)
        val successes = results.filterIsInstance<ExecutionResult.Success>()

        assertEquals(1, successes.size, results.toString())
        assertEquals("पञ्च", successes.single().value)
    }

    @Test
    fun `explicit signature rejects the wrong arity`() {
        val results = vm.evalScript(
            """
            योजन + ल्युट् + सुँ ।
            वाम + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            दक्षिण + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            सङ्ख्या + सुँ इति परिणाम + सुँ ।
            वाम + अम् दक्षिण + अम् च युज् + णिच् + लोट् + सिप् ॥

            द्वि + अम् योजन + ल्युट् + टा कृ + लोट् + सिप् ।
            """.trimIndent(),
        )

        val failure = assertIs<ExecutionResult.Failure>(results.last())
        assertTrue(failure.message.contains("expects 2 arguments"), failure.message)
    }

    @Test
    fun `explicit signature rejects an incompatible argument type`() {
        val results = vm.evalScript(
            """
            द्विगुणन + ल्युट् + सुँ ।
            मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            सङ्ख्या + सुँ इति परिणाम + सुँ ।
            मान + अम् द्वि + अम् च गुण् + णिच् + लोट् + सिप् ॥

            राम + अम् द्विगुणन + ल्युट् + टा कृ + लोट् + सिप् ।
            """.trimIndent(),
        )

        val failure = assertIs<ExecutionResult.Failure>(results.last())
        assertTrue(failure.message.contains("मानप्रकार"), failure.message)
    }

    @Test
    fun `signature compiler preserves individual names types and result`() {
        val statements = PvmScript.parse(
            """
            योजन + ल्युट् + सुँ ।
            वाम + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            दक्षिण + सुँ शब्द + सुँ इति मान + सुँ ।
            सूची + सुँ इति परिणाम + सुँ ।
            वाम + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            """.trimIndent(),
        )
        val definition = assertIs<PvmScriptStatement.SamjnaDefinition>(statements.single())
        val signature = SamjnaSignatureCompiler.compile(definition.body)

        assertEquals(
            listOf(
                SamjnaParameter("वाम", SamjnaValueType.SANKHYA),
                SamjnaParameter("दक्षिण", SamjnaValueType.SHABDA),
            ),
            signature.parameters,
        )
        assertEquals(SamjnaValueType.SUCHI, signature.resultType)
    }
}
