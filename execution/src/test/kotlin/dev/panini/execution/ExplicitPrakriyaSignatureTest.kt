package dev.panini.execution

import dev.panini.vyakaranam.ast.depthFirst
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.semanticKey
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.SupPratyaya
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ExplicitPrakriyaSignatureTest {
    private val vm = PaniniVM()

    @Test
    fun `parsed argument identity overrides misleading compatibility text`() {
        val pada = SubantaPada(
            sourceText = "फल + अम्",
            pratipadika = MulaPratipadika(sourceText = "फल", text = "राम"),
            sup = SupPratyaya(sourceText = "अम्", text = "अम्"),
        )
        val resolved = ResolvedPrakriyaArgument(
            parameter = null,
            argument = PrakriyaArgument(
                term = "फल",
                pada = pada,
                origin = PrakriyaArgumentOrigin.WRITTEN,
            ),
            bindingKind = PrakriyaArgumentBindingKind.POSITIONAL,
        )

        assertFalse(resolved.isPriorResult)
        assertEquals("राम", resolved.referenceName)
        val astResolution = PrakriyaArgumentResolution.Success.fromPadas(listOf(pada))
        assertTrue(astResolution.arguments.single().argument.pada === pada)
        assertEquals("राम", astResolution.arguments.single().argument.term)
    }

    @Test
    fun `named call arguments bind by parameter name instead of source order`() {
        val results = vm.evalScript(
            """
            व्यवकलन + ल्युट् + सुँ ।
            वाम + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            दक्षिण + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            सङ्ख्या + सुँ इति परिणाम + सुँ ।
            वाम + अम् दक्षिण + अम् च वि + युज् + णिच् + लोट् + सिप् ॥

            दक्षिण + ङि द्वि + अम् वाम + ङि पञ्च + अम् व्यवकलन + ल्युट् + टा कृ + लोट् + सिप् ।
            """.trimIndent(),
        )

        val success = results.filterIsInstance<ExecutionResult.Success>().last()
        assertEquals(3, assertIs<SanskritValue.Sankhya>(success.typedValue).value, results.toString())
    }

    @Test
    fun `resolved named arguments retain parameter roles and source morphology`() {
        val parsed = PvmScript.parse(
            """
            व्यवकलन + ल्युट् + सुँ ।
            वाम + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            दक्षिण + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            सङ्ख्या + सुँ इति परिणाम + सुँ ।
            वाम + अम् दक्षिण + अम् च वि + युज् + णिच् + लोट् + सिप् ॥

            दक्षिण + ङि द्वि + अम् वाम + ङि पञ्च + अम् व्यवकलन + ल्युट् + टा कृ + लोट् + सिप् ।
            """.trimIndent(),
        )
        val definition = assertIs<PvmScriptStatement.PrakriyaDefinition>(parsed[0])
        val registry = PrakriyaRegistry().apply {
            register(
                Prakriya(
                    definition.nameSegmented,
                    definition.prakriya.nameIdentity,
                    definition.body,
                ),
            )
        }
        val call = assertIs<PvmScriptStatement.Sentence>(parsed[1])
        val invocation = requireNotNull(registry.detectInvocation(requireNotNull(call.ukti)))
        val resolution = assertIs<PrakriyaArgumentResolution.Success>(
            PrakriyaInvocationArgumentResolver.resolve(invocation),
        )

        assertEquals(listOf("वाम", "दक्षिण"), resolution.arguments.map { it.parameter?.nameStem })
        assertEquals(listOf("पञ्च", "द्वि"), resolution.arguments.map { it.argument.term })
        assertTrue(resolution.arguments.all { it.argument.pada != null })
        assertTrue(resolution.arguments.all { it.bindingKind == PrakriyaArgumentBindingKind.NAMED })

        val readable = PvmUktiSadhaka().sadhayaLine(call.text)
        assertTrue("दक्षिणे द्वि" in readable, readable)
        assertTrue("वामे पञ्च" in readable, readable)
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

        assertTrue(assertIs<ExecutionResult.Failure>(unknown.last()).message.contains("अज्ञातमानानि"))
        assertTrue(assertIs<ExecutionResult.Failure>(duplicate.last()).message.contains("पुनरुक्तमानानि"))
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
        assertTrue(failure.message.contains("2 मानानि अपेक्षितानि"), failure.message)
    }

    @Test
    fun `explicit signature rejects an incompatible argument type`() {
        val results = vm.evalScript(
            """
            गण + ल्युट् + सुँ ।
            मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            सङ्ख्या + सुँ इति परिणाम + सुँ ।
            मान + अम् द्वि + अम् च गुण् + णिच् + लोट् + सिप् ॥

            राम + अम् गण + ल्युट् + टा डुकृञ् + उ + लोट् + सिप् ।
            """.trimIndent(),
        )

        val failure = assertIs<ExecutionResult.Failure>(results.last())
        assertTrue(failure.message.contains("मानप्रकार"), failure.message)
        assertTrue(failure.message.contains("सङ्ख्या-प्रकारम् अपेक्षते"), failure.message)
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
        val definition = assertIs<PvmScriptStatement.PrakriyaDefinition>(statements.single())
        val signature = PrakriyaSignatureCompiler.compile(definition.body)

        assertEquals(
            listOf(
                PrakriyaParameter("वाम", PrakriyaValueType.SANKHYA),
                PrakriyaParameter("दक्षिण", PrakriyaValueType.SHABDA),
            ),
            signature.parameters,
        )
        assertEquals(PrakriyaValueType.SUCHI, signature.resultType)
    }

    @Test
    fun `derived parameter names retain their complete morphological identity`() {
        val statements = PvmScript.parse(
            """
            वह् + ल्युट् + सुँ ।
            युज् + ल्युट् + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            सङ्ख्या + सुँ इति परिणाम + सुँ ।
            युज् + ल्युट् + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            """.trimIndent(),
        )
        val definition = assertIs<PvmScriptStatement.PrakriyaDefinition>(statements.single())
        val signature = PrakriyaSignatureCompiler.compile(definition.body)

        assertEquals("युज्+ल्युट्", signature.parameters.single().nameStem)
        val bound = PrakriyaAstArgumentBinder.bind(
            requireNotNull(definition.body.last().program),
            signature.parameters,
            argumentCount = 1,
        )
        val operand = bound.depthFirst().filterIsInstance<dev.panini.vyakaranam.ast.Invocation>()
            .single().vakya.padas.filterIsInstance<dev.panini.vyakaranam.ast.SubantaPada>().single()
        assertEquals(PrakriyaAstArgumentBinder.referenceKey(0), operand.pratipadika.semanticKey())
    }
}
