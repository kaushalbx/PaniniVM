package dev.panini.compiler

import dev.panini.execution.*
import kotlin.test.*

class TypedParticipantProcedureTest {
    @Test fun `coordinated typed input example has backend parity`() {
        val source = java.io.File("examples/control_flow/coordinated_typed_inputs.pvm").readText()
        assertEquals("पञ्च", parity(source, "CoordinatedTypedInputExample").toDisplayText())
    }
    @Test fun `explicit conjuncts disambiguate same case typed inputs without reordering operands`() {
        val first = "प्रथम + टाप् + अम् सङ्ख्या + अम्"
        val second = "द्वितीय + टाप् + अम् सङ्ख्या + अम्"
        for ((index, phrases) in listOf(listOf(first, second), listOf(second, first),
            listOf("सङ्ख्या + अम् प्रथम + टाप् + अम्", "सङ्ख्या + अम् द्वितीय + टाप् + अम्")).withIndex()) {
            val source = """
                परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
                ${phrases.joinToString(" च ")} च वि + युज् + णिच् + लोट् + सिप् ॥
                ${if (index == 1) "द्वि + औट् नवन् + शस् च" else "नवन् + शस् द्वि + औट् च"} परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
            """.trimIndent()
            assertEquals(7L,
                assertIs<SanskritValue.Sankhya>(parity(source, "CoordinatedTypedInputs$index")).value)
        }
    }

    @Test fun `a single final connective does not invent phrase boundaries`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            प्रथम + टाप् + अम् सङ्ख्या + अम् द्वितीय + टाप् + अम् सङ्ख्या + अम् च युज् + णिच् + लोट् + सिप् ॥
            नवन् + शस् द्वि + औट् च परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last())
        assertFailsWith<IllegalArgumentException> { CompilerFrontend.lower(source, "AmbiguousUnmarkedConjuncts") }
    }

    @Test fun `readable output retains explicit multiword conjunct boundaries`() {
        val source = "प्रथम + टाप् + अम् सङ्ख्या + अम् च द्वितीय + टाप् + अम् सङ्ख्या + अम् च युज् + णिच् + लोट् + सिप् ।"
        assertEquals("प्रथमां सङ्ख्यां च द्वितीयां सङ्ख्यां च योजय ।", PvmUktiSadhaka().sadhayaLine(source))
    }
    private val mixedDefinition = """
        परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
        प्रथम + टाप् + अम् सङ्ख्या + अम् मुद्र् + णिच् + लोट् + सिप् ।
        फल + सुँ प्रथम + सुँ शब्द + सुँ असँ + लट् + तिप् ॥
    """.trimIndent()

    private fun parity(source: String, name: String, initial: Map<String, SanskritValue> = emptyMap()): SanskritValue {
        val interpreted = PaniniVM().evalScript(source, scope = ExecutionScope(environment = ValueEnvironment(initial)))
        assertTrue(interpreted.all { it is ExecutionResult.Success }, interpreted.toString())
        val result = assertIs<ExecutionResult.Success>(interpreted.last()).typedValue!!
        val generated = BytecodeCompiler.compileAndLoad(source, name)
        assertEquals(result, (generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>)["LastResult"])
        return result
    }

    @Test fun `first number and first word infer separate slots without declarations`() {
        for ((index, arguments) in listOf("एक + अम् राम + अम् च", "राम + अम् एक + अम् च").withIndex()) {
            val source = "$mixedDefinition\n$arguments परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।"
            assertIs<SanskritValue.Shabda>(parity(source, "TypedMixed$index"))
            assertTrue(PrakriyaScriptValidator.validate(source).none { it.severity == PrakriyaDiagnosticSeverity.ERROR })
        }
    }

    @Test fun `dynamic numeric looking words retain word type in any call order`() {
        val initial = mapOf("वस्तु" to SanskritValue.Shabda("2"), "मान" to SanskritValue.Sankhya(7L, "सप्त"))
        for ((index, arguments) in listOf("वस्तु + अम् मान + अम् च", "मान + अम् वस्तु + अम् च").withIndex()) {
            val source = "$mixedDefinition\n$arguments परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।"
            assertEquals(initial["वस्तु"], parity(source, "DynamicTypedMixed$index", initial))
        }
    }

    @Test fun `postposed ordinal adjectives preserve the same procedure meaning`() {
        val definition = mixedDefinition
            .replace("प्रथम + टाप् + अम् सङ्ख्या + अम्", "सङ्ख्या + अम् प्रथम + टाप् + अम्")
            .replace("प्रथम + सुँ शब्द + सुँ", "शब्द + सुँ प्रथम + सुँ")
        assertEquals(SanskritValue.Shabda("राम"), parity(
            "$definition\nराम + अम् एक + अम् च परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।", "PostposedTypedParticipants"))
    }

    @Test fun `typed participant morphology supplies a live local pronoun antecedent`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            प्रथम + टाप् + अम् सङ्ख्या + अम् मुद्र् + णिच् + लोट् + सिप् ।
            द्वि + औट् तद् + ङे दा + लोट् + सिप् ।
            फल + सुँ तद् + सुँ असँ + लट् + तिप् ॥
            सप्तन् + शस् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        assertEquals(2L, assertIs<SanskritValue.Sankhya>(parity(source, "TypedPronounIdentity")).value)
    }

    @Test fun `second number reserves first numeric slot and preserves order within type`() {
        val definition = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            फल + सुँ द्वितीय + टाप् + सुँ सङ्ख्या + सुँ असँ + लट् + तिप् ॥
        """.trimIndent()
        val source = "$definition\nद्वि + औट् षष् + शस् च परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।"
        assertEquals(6L, assertIs<SanskritValue.Sankhya>(parity(source, "SecondTypedNumber")).value)
        val signature = PaniniModuleAnalyzer.analyze(PaniniModuleDescriptor("typed", listOf(PaniniModuleSource("main.pvm", source))))
            .procedures.single().signature
        assertEquals(listOf(1L, 2L), signature.parameters.map { it.participantOrdinal })
    }

    @Test fun `productive feminine ordinal has interpreter and JVM parity`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            फल + सुँ द्वि + तीय + टाप् + सुँ सङ्ख्या + सुँ असँ + लट् + तिप् ॥
            द्वि + औट् षष् + शस् च परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        assertEquals(6L, assertIs<SanskritValue.Sankhya>(parity(source, "ProductiveFeminineTypedSlot")).value)
    }

    @Test fun `one list argument retains its payload rather than becoming many inputs`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            प्रथम + टाप् + अम् सङ्ख्या + अम् मुद्र् + णिच् + लोट् + सिप् ।
            फल + सुँ प्रथम + टाप् + सुँ सूची + सुँ असँ + लट् + तिप् ॥
            वस्तु + अम् मान + अम् च परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        for (size in listOf(0, 3)) {
            val list = SanskritValue.Suchi(List(size) { SanskritValue.Shabda("item$it") })
            val initial = mapOf("वस्तु" to list, "मान" to SanskritValue.Sankhya(1L, "एक"))
            assertEquals(list, parity(source, "TypedList$size", initial))
        }
    }

    @Test fun `missing excess and wrong type counts fail before the body`() {
        for ((index, arguments) in listOf("एक + अम्", "एक + अम् राम + अम् द्वि + औट् च",
            "एक + अम् द्वि + औट् च").withIndex()) {
            val source = "$mixedDefinition\n$arguments परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।"
            assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last())
            assertFailsWith<IllegalArgumentException> { CompilerFrontend.lower(source, "InvalidTypedCount$index") }
        }
    }

    @Test fun `dynamic type count failures are checked before entering the body`() {
        val source = "$mixedDefinition\nवस्तु + अम् मान + अम् च परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।"
        val initial = mapOf("वस्तु" to SanskritValue.Sankhya(2L, "द्वि"),
            "मान" to SanskritValue.Sankhya(7L, "सप्त"))
        val interpreted = PaniniVM().evalScript(source, scope = ExecutionScope(environment = ValueEnvironment(initial)))
        assertTrue(interpreted.none { it is ExecutionResult.Success }, interpreted.toString())
        assertIs<ExecutionResult.Failure>(interpreted.last())
        val generated = BytecodeCompiler.compileAndLoad(source, "DynamicInvalidTypedCount")
        val failure = assertFailsWith<java.lang.reflect.InvocationTargetException> {
            generated.getMethod("execute", Map::class.java).invoke(null, initial)
        }
        assertIs<CompiledPaniniExecutionException>(failure.cause)
    }

    @Test fun `typed participant metadata survives export codec`() {
        val source = "$mixedDefinition\nराम + अम् एक + अम् च परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।"
        val module = BytecodeCompiler.compileModule(PaniniModuleDescriptor("typed", listOf(PaniniModuleSource("main.pvm", source))), "TypedMetadata")
        val encoded = PaniniModuleMetadataCodec.encode(module.metadata)
        val restored = PaniniModuleMetadataCodec.decode(encoded)
        assertEquals(module.metadata, restored)
        assertEquals(listOf(1L, 1L), restored.procedures.single().parameters.map { it.participantOrdinal })
    }

    @Test fun `exported typed procedures bind correctly without their source`() {
        val library = BytecodeCompiler.compileModule(PaniniModuleDescriptor("typed_library",
            listOf(PaniniModuleSource("library.pvm", mixedDefinition, false))), "TypedLibrary")
        val metadata = PaniniModuleMetadataCodec.decode(PaniniModuleMetadataCodec.encode(library.metadata))
        val application = BytecodeCompiler.compileModule(PaniniModuleDescriptor("typed_application",
            listOf(PaniniModuleSource("main.pvm", "राम + अम् एक + अम् च परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।")),
            dependencies = listOf(metadata)), "TypedApplication")
        val loader = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
        loader.loadFromBytes(library.metadata.className, library.bytecode)
        val generated = loader.loadFromBytes(application.metadata.className, application.bytecode)
        val value = (generated.getMethod("execute").invoke(null) as Map<*, *>)["LastResult"]
        assertEquals(SanskritValue.Shabda("राम"), value)
    }

    @Test fun `recursive typed inputs belong to their own invocation`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            यदि प्रथम + टाप् + सुँ सङ्ख्या + सुँ शून्य + टा सम + सुँ असँ + लट् + तिप्
            तर्हि फल + सुँ शून्य + सुँ असँ + लट् + तिप्
            अन्यथा प्रथम + टाप् + अम् सङ्ख्या + अम् एक + अम् च वि + युज् + णिच् + लोट् + सिप्
            ततः परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ॥
            त्रि + शस् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        assertEquals(0L, assertIs<SanskritValue.Sankhya>(parity(source, "RecursiveTypedParticipant")).value)
    }

    @Test fun `ambiguous or disagreeing input phrases cannot infer a signature`() {
        for (phrase in listOf("प्रथम + अम् सङ्ख्या + अम्",
            "प्रथम + टाप् + अम् सङ्ख्या + अम् सूची + अम्")) {
            val source = """
                परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
                $phrase मुद्र् + णिच् + लोट् + सिप् ॥
                एक + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
            """.trimIndent()
            assertFailsWith<IllegalArgumentException> { CompilerFrontend.lower(source, "InvalidTypedAttachment") }
            assertTrue(PrakriyaScriptValidator.validate(source).any {
                it.severity == PrakriyaDiagnosticSeverity.ERROR && "attachment" in it.message
            })
            assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last())
        }
    }
}
