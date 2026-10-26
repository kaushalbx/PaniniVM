package dev.panini.execution

import dev.panini.vyakaranam.ast.PrakriyaVisibility
import dev.panini.vyakaranam.ast.PrakriyaPrecedence
import kotlin.test.*

class ProcedureRegistryIdentityTest {
    @Test fun `derived field identities remain distinct in schema declarations`() {
        val schema = assertNotNull(TaddhitaStructEngine.detectResultSchema(
            "मूल्य + सुँ मूल्य + त्व + सुँ च अवस्था + ङस् क्षेत्र + जस् असँ + लट् + झि ।"))
        assertEquals(listOf("मूल्य", "मूल्य + त्व"), schema.fields)
    }

    @Test fun `field assertions cannot turn negated or future clauses into present facts`() {
        val source = "अवस्था + मतुप् + ङस् मूल्य + सुँ द्वि + सुँ असँ + लट् + तिप् ।"
        assertNotNull(TaddhitaStructEngine.detectFieldAssertion(source))
        for ((invalid, message) in listOf(
            source.replace("असँ", "न असँ") to "cannot discard",
            source.replace("असँ", "त्रि + अम् असँ") to "cannot discard",
            source.replace("लट्", "लृट्") to "present copula",
        )) {
            val direct = assertFailsWith<IllegalArgumentException> {
                TaddhitaStructEngine.detectFieldAssertion(invalid)
            }
            assertTrue(direct.message.orEmpty().contains(message))
            val failure = assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(invalid).single())
            val diagnostic = assertNotNull(failure.diagnostic)
            assertTrue(diagnostic.code.startsWith("FIELD_ASSERTION_"))
            assertNotNull(diagnostic.span)
        }
    }

    @Test fun `field declarations require a present copula and consume every participant`() {
        val source = "मूल्य + सुँ फल + सुँ च अवस्था + ङस् क्षेत्र + जस् असँ + लट् + झि ।"
        assertNotNull(TaddhitaStructEngine.detectResultSchema(source))
        val future = source.replace("लट्", "लृट्")
        val extra = source.replace("असँ", "द्वि + अम् असँ")
        for ((invalid, message) in listOf(future to "present copula", extra to "extra participants")) {
            assertTrue(assertFailsWith<IllegalArgumentException> {
                TaddhitaStructEngine.detectResultSchema(invalid)
            }.message.orEmpty().contains(message))
            assertTrue(PrakriyaScriptValidator.validate(invalid).any { it.message.contains(message) })
            val failure = assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(invalid).single())
            val diagnostic = assertNotNull(failure.diagnostic)
            assertTrue(diagnostic.code.startsWith("RESULT_SCHEMA_"))
            assertNotNull(diagnostic.span)
        }
    }

    @Test fun `repeated schema fields are rejected rather than silently collapsed`() {
        val source = "मूल्य + सुँ मूल्य + सुँ फल + सुँ च अवस्था + ङस् क्षेत्र + जस् असँ + लट् + झि ।"
        val message = "Duplicate fields in Panini schema"
        assertTrue(assertFailsWith<IllegalArgumentException> {
            TaddhitaStructSchema("अवस्था", listOf("मूल्य", "मूल्य", "फल"))
        }.message.orEmpty().contains(message))
        assertTrue(PrakriyaScriptValidator.validate(source).any { it.message.contains(message) })
        val failure = assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).single())
        assertEquals("RESULT_SCHEMA_DUPLICATE_FIELDS", assertNotNull(failure.diagnostic).code)
    }

    @Test fun `schema registration preserves the original contract after a conflicting declaration`() {
        val registry = PrakriyaRegistry()
        val schema = TaddhitaStructSchema("अवस्था", listOf("मूल्य", "फल"))
        registry.registerSchema(schema)
        registry.registerSchema(schema.copy())
        assertFailsWith<IllegalArgumentException> {
            registry.registerSchema(TaddhitaStructSchema("अवस्था", listOf("अन्य")))
        }
        assertEquals(schema, registry.resolveSchema("अवस्था"))
        val source = """
            मूल्य + सुँ फल + सुँ च अवस्था + ङस् क्षेत्र + जस् असँ + लट् + झि ।
            अन्य + सुँ प्रयत्न + सुँ च अवस्था + ङस् क्षेत्र + जस् असँ + लट् + झि ।
        """.trimIndent()
        assertTrue(PrakriyaScriptValidator.validate(source).any { it.message.contains("Conflicting Panini schema") })
        assertFailsWith<IllegalArgumentException> { PaniniVM().evalScript(source) }
    }

    @Test fun `inherited domain lookup follows ancestors and rejects cycles without changing existing edges`() {
        val registry = PrakriyaRegistry()
        val procedure = procedure(PrakriyaValueType.SANKHYA)
        registry.register(procedure)
        registry.registerInheritance(InheritanceRelation("मध्य", "गणित"))
        registry.registerInheritance(InheritanceRelation("अन्तिम", "मध्य"))
        val argument = PrakriyaArgument("द्वि", value = SanskritValue.Sankhya(2, "द्वि"),
            origin = PrakriyaArgumentOrigin.WRITTEN)
        assertEquals(procedure, registry.resolveStructuredInvocation("परीक्षण", "अन्तिम", listOf(argument), "")?.kriya)
        assertFailsWith<IllegalArgumentException> { registry.registerInheritance(InheritanceRelation("गणित", "अन्तिम")) }
        assertNull(registry.getParentClass("गणित"))
        assertFailsWith<IllegalArgumentException> { registry.registerInheritance(InheritanceRelation("मध्य", "भाषा")) }
        assertEquals("गणित", registry.getParentClass("मध्य"))
        assertFailsWith<IllegalArgumentException> { registry.registerInheritance(InheritanceRelation("स्व", "स्व")) }
    }

    @Test fun `named singleton slots reject dual and plural reference morphology`() {
        val definition = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            मान + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥
        """.trimIndent()
        for (ending in listOf("ओस्", "सुप्", "आम्")) {
            val source = "$definition\nमान + $ending द्वि + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।"
            val diagnostics = PrakriyaScriptValidator.validate(source)
            assertTrue(diagnostics.any { it.message.contains("slot") && it.message.contains("singular") }, diagnostics.toString())
            val results = PaniniVM().evalScript(source)
            val failure = assertIs<ExecutionResult.Failure>(results.last(), results.toString())
            assertTrue(failure.message.contains("singular"), failure.message)
        }
    }

    @Test fun `prior result follows source order across nominal and action results`() {
        val declaration = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            मान + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥
        """.trimIndent()
        val action = "द्वि + अम् त्रि + अम् च युज् + णिच् + लोट् + सिप् ।"
        val nominal = "यदि एक + सुँ एक + टा सम + सुँ असँ + लट् + तिप् तर्हि नवन् + जस् अन्यथा त्रि + जस् ।"
        for ((body, expected) in listOf("$action\n$nominal" to 10L, "$nominal\n$action" to 6L)) {
            val results = PaniniVM().evalScript("$declaration\n$body\nफल + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।")
            val result = assertIs<ExecutionResult.Success>(results.last(), results.toString())
            assertEquals(expected, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
            val directResults = PaniniVM().evalScript("$body\nफल + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ।")
            val direct = assertIs<ExecutionResult.Success>(directResults.last(), directResults.toString())
            assertEquals(expected, assertIs<SanskritValue.Sankhya>(direct.typedValue).value)
        }
    }

    @Test fun `missing prior result cannot become literal text in a procedure call`() {
        val source = """
            वाचन + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ शब्द + सुँ इति मान + सुँ ।
            मान + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            फल + अम् वाचन + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val results = PaniniVM().evalScript(source)
        val failure = assertIs<ExecutionResult.Failure>(results.last(), results.toString())
        assertEquals(ExecutionError.INVALID_VALUE, failure.error)
        assertTrue(failure.message.contains("No preceding result"), failure.message)
        assertTrue(results.none { it is ExecutionResult.Success && it.outputKind == OutputKind.CONSOLE })
    }

    @Test fun `explicit prior result supplies its typed payload before overload selection`() {
        val registry = PrakriyaRegistry()
        val numeric = procedure(PrakriyaValueType.SANKHYA).copy(domainStem = null)
        registry.register(numeric)
        registry.register(procedure(PrakriyaValueType.SHABDA).copy(domainStem = null))
        val call = PvmScript.parse("फल + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।")
            .filterIsInstance<PvmScriptStatement.Sentence>().single()
        val value = SanskritValue.Sankhya(7, "सप्तन्")
        val invocation = assertNotNull(registry.detectInvocation(assertNotNull(call.ukti), resolvePriorResult = { value }))
        assertEquals(numeric, invocation.kriya)
        assertEquals(value, invocation.arguments.single().value)
    }

    @Test fun `numeric prohibition does not erase extra participants or repeated negation`() {
        for (guard in listOf(
            "न न प्रथम + अम् शून्य + अम् ।",
            "न प्रथम + अम् शून्य + अम् राम + टा ।",
        )) {
            val source = """
                परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
                $guard
                प्रथम + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥
            """.trimIndent()
            val definition = PvmScript.parse(source).filterIsInstance<PvmScriptStatement.PrakriyaDefinition>().single()
            assertNull(NishedhaGuardEvaluator.numericProhibition(
                definition.body.single { it.isNishedha }, emptyList(), 1,
            ), guard)
            val results = PaniniVM().evalScript("$source\nद्वि + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।")
            val failure = assertIs<ExecutionResult.Failure>(results.last(), results.toString())
            assertTrue(failure.message.contains("Unsupported procedure prohibition"), failure.message)
        }
    }

    @Test fun `compatibility payloads follow named arguments when slot order changes`() {
        val registry = PrakriyaRegistry()
        registry.register(procedure(PrakriyaValueType.SANKHYA).copy(domainStem = null,
            signatureOverride = PrakriyaSignature(parameters = listOf(
                PrakriyaParameter("वाम", PrakriyaValueType.SANKHYA),
                PrakriyaParameter("दक्षिण", PrakriyaValueType.SANKHYA),
            ))))
        val call = PvmScript.parse("दक्षिण + ङि निवेश + अम् वाम + ङि सञ्चित + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।")
            .filterIsInstance<PvmScriptStatement.Sentence>().single()
        val invocation = assertNotNull(registry.detectInvocation(assertNotNull(call.ukti))).copy(
            argumentValues = listOf(SanskritValue.Sankhya(2, "द्वि"), SanskritValue.Sankhya(10, "दशन्")),
        )
        val resolved = assertIs<PrakriyaArgumentResolution.Success>(PrakriyaInvocationArgumentResolver.resolve(invocation))
        assertEquals(listOf(10L, 2L), resolved.arguments.map { assertIs<SanskritValue.Sankhya>(it.argument.value).value })
    }

    @Test fun `undeclared named slot is rejected even in a signatureless procedure`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            राम + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            मान + ङि द्वि + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val diagnostics = PrakriyaScriptValidator.validate(source)
        assertTrue(diagnostics.any { it.message.contains("अज्ञातमानानि") }, diagnostics.toString())
        val results = PaniniVM().evalScript(source)
        val failure = assertIs<ExecutionResult.Failure>(results.last(), results.toString())
        assertTrue(failure.message.contains("अज्ञातमानानि"), failure.message)
        assertTrue(results.none { it is ExecutionResult.Success && it.outputKind == OutputKind.CONSOLE })
    }

    @Test fun `named slot mismatch makes an exception inapplicable rather than positional`() {
        val registry = PrakriyaRegistry()
        val numeric = procedure(PrakriyaValueType.SANKHYA).copy(domainStem = null)
        val differentSlot = numeric.copy(
            precedence = PrakriyaPrecedence.APAVADA,
            signatureOverride = PrakriyaSignature(parameters = listOf(
                PrakriyaParameter("अन्य", PrakriyaValueType.SANKHYA),
            )),
        )
        registry.register(numeric)
        registry.register(differentSlot)
        val call = PvmScript.parse("मान + ङि द्वि + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।")
            .filterIsInstance<PvmScriptStatement.Sentence>().single()
        val invocation = assertNotNull(registry.detectInvocation(assertNotNull(call.ukti)))
        assertEquals(numeric, invocation.kriya)
        assertNull(invocation.resolutionError)
        val resolved = assertIs<PrakriyaArgumentResolution.Success>(
            PrakriyaInvocationArgumentResolver.resolve(invocation),
        )
        assertTrue(resolved.named)
        assertEquals("मान", resolved.arguments.single().parameter?.nameStem)
        assertEquals(numeric, registry.detectInvocation(assertNotNull(call.ukti), staticAnalysis = true)?.kriya)

        val incompatibleRegistry = PrakriyaRegistry().also { it.register(differentSlot) }
        val invalid = assertNotNull(incompatibleRegistry.detectInvocation(assertNotNull(call.ukti)))
        assertIs<PrakriyaArgumentResolution.Failure>(PrakriyaInvocationArgumentResolver.resolve(invalid))
    }

    @Test fun `inapplicable higher precedence rule cannot mask a compatible overload`() {
        val registry = PrakriyaRegistry()
        val numeric = procedure(PrakriyaValueType.SANKHYA).copy(domainStem = null)
        val textException = procedure(PrakriyaValueType.SHABDA).copy(
            domainStem = null, precedence = PrakriyaPrecedence.APAVADA,
        )
        registry.register(numeric)
        registry.register(textException)
        val argument = PrakriyaArgument("द्वि", value = SanskritValue.Sankhya(2, "द्वि"),
            origin = PrakriyaArgumentOrigin.WRITTEN)
        assertEquals(numeric, registry.resolveStructuredInvocation("परीक्षण", null,
            listOf(argument), "")?.kriya)
        assertEquals(numeric, registry.resolveStructuredInvocation("परीक्षण", null,
            listOf(argument.copy(value = null)), "",
            analyzedArgumentTypes = listOf(PrakriyaValueType.SANKHYA))?.kriya)
        val call = PvmScript.parse("द्वि + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।")
            .filterIsInstance<PvmScriptStatement.Sentence>().single()
        assertEquals(numeric, registry.detectInvocation(assertNotNull(call.ukti))?.kriya)
        val numericException = numeric.copy(precedence = PrakriyaPrecedence.APAVADA)
        registry.register(numericException)
        assertEquals(numericException, registry.resolveStructuredInvocation("परीक्षण", null,
            listOf(argument), "")?.kriya)
        assertEquals(numericException, registry.detectInvocation(assertNotNull(call.ukti))?.kriya)
    }

    private fun procedure(type: PrakriyaValueType) = Prakriya(
        nameSegmented = "परीक्षण", nameStem = "परीक्षण", domainStem = "गणित",
        body = emptyList(), signatureOverride = PrakriyaSignature(
            parameters = listOf(PrakriyaParameter("मान", type)),
        ),
    )

    @Test fun `enumeration retains distinct overloads while deduplicating domain aliases and registration`() {
        val registry = PrakriyaRegistry()
        val numeric = procedure(PrakriyaValueType.SANKHYA)
        val text = procedure(PrakriyaValueType.SHABDA)
        registry.register(numeric)
        registry.register(text)
        registry.register(numeric.copy())
        assertEquals(listOf(numeric, text), registry.all())
    }

    @Test fun `private last definition does not hide an earlier visible definition`() {
        val registry = PrakriyaRegistry()
        val public = procedure(PrakriyaValueType.SANKHYA)
        val private = procedure(PrakriyaValueType.SHABDA).copy(
            sourceFile = "private.pvm", visibility = PrakriyaVisibility.INTERNAL,
        )
        registry.register(public)
        registry.register(private)
        assertEquals(public, registry.resolve("गणित::परीक्षण", "caller.pvm"))
        assertEquals(private, registry.resolve("गणित::परीक्षण", "private.pvm"))
        assertEquals(public, registry.resolve("परीक्षण"))
    }
}
