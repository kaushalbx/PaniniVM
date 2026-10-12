package dev.panini.compiler

import dev.panini.execution.*
import kotlin.test.*

/** Dynamic participant shape must survive lowering until operation selection. */
class BareParticipantHistoryPlanningTest {
    private fun compiled(source: String, name: String): SanskritValue {
        val generated = BytecodeCompiler.compileAndLoad(source, name)
        return (generated.getMethod("execute").invoke(null) as Map<*, *>)["LastResult"] as SanskritValue
    }

    @Test fun `mixed collection history selects counting rather than placeholder multiplication`() {
        val number = SanskritValue.Sankhya(1L, "एक")
        val initial = mapOf("पूर्व" to SanskritValue.Suchi(listOf(number, number)),
            "उत्तर" to SanskritValue.Suchi(listOf(number, number, number)))
        val source = "पूर्व + अम् उत्तर + अम् च मान + ङे दा + लोट् + सिप् ।\n" +
            "दा + ल्युट् + ङस् कर्मन् + अम् एक + अम् च गण + णिच् + लोट् + सिप् ।"
        val results = PaniniVM().evalScript(source, scope = ExecutionScope(environment = ValueEnvironment(initial)))
        val result = assertIs<ExecutionResult.Success>(results.last(), results.toString())
        assertEquals(3L, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
        val generated = BytecodeCompiler.compileAndLoad(source, "MixedCollectionHistoryCounting")
        val values = generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>
        assertEquals(result.typedValue, values["LastResult"])
        assertEquals(1, CompilerFrontend.lower(source, "MixedCollectionHistoryCountingIr").entryPoint
            .filterIsInstance<CompilerInstruction.CallParticipantGroup>().size)
    }

    @Test fun `bare gan selects multiplication for several remembered numeric objects`() {
        val source = "द्वि + औट् त्रि + शस् च मान + ङे दा + लोट् + सिप् ।\n" +
            "दा + ल्युट् + ङस् कर्मन् + अम् गण + णिच् + लोट् + सिप् ।"
        val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source).last())
        assertEquals(6L, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
        assertEquals(result.typedValue, compiled(source, "BareRememberedMultiplication"))
    }

    @Test fun `bare and mixed dynamic arithmetic bind results to explicit destinations`() {
        for ((index, extra) in listOf("", "एक + अम् च ").withIndex()) {
            val source = "द्वि + औट् त्रि + शस् च मान + ङे दा + लोट् + सिप् ।\n" +
                "दा + ल्युट् + ङस् कर्मन् + अम् ${extra}परिणाम + ङे गण + णिच् + लोट् + सिप् ।\n" +
                "परिणाम + अम् मुद्र् + णिच् + लोट् + सिप् ।"
            val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source).last())
            assertEquals(result.typedValue, compiled(source, "DynamicHistoryDestination$index"))
            val generated = BytecodeCompiler.compileAndLoad(source, "DynamicHistoryDestinationValues$index")
            val values = generated.getMethod("execute").invoke(null) as Map<*, *>
            assertEquals(6L, assertIs<SanskritValue.Sankhya>(values["परिणाम"]).value)
        }
    }

    @Test fun `dynamic history destination updates procedure slot without overwriting caller`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ सङ्ख्या + अम् जन् + णिच् + लट् + तिप् ।
            मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            दा + ल्युट् + ङस् कर्मन् + अम् मान + ङे गण + णिच् + लोट् + सिप् ॥
            नवन् + शस् मान + ङे दा + लोट् + सिप् ।
            द्वि + औट् त्रि + शस् च आधार + ङे दा + लोट् + सिप् ।
            मान + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source).last())
        assertEquals(6L, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
        val generated = BytecodeCompiler.compileAndLoad(source, "LocalDynamicHistoryDestination")
        val values = generated.getMethod("execute").invoke(null) as Map<*, *>
        assertEquals(result.typedValue, values["LastResult"])
        assertEquals(9L, assertIs<SanskritValue.Sankhya>(values["मान"]).value)
        assertTrue(values.keys.none { it.toString().startsWith("__pvm_parameter_") })
    }

    @Test fun `failed dynamic call does not change its explicit destination`() {
        val prior = SanskritValue.Sankhya(9L, "नव")
        val runtime = CompiledProgramRuntime(mapOf("मान" to prior, "LastResult" to prior))
        assertFailsWith<CompiledPaniniExecutionException> {
            runtime.executeParticipantGroup(SanskritValue.Gana(listOf(SanskritValue.Sankhya(1L, "एक"),
                SanskritValue.Sankhya(0L, "शून्य"))), "हृञ्", "", "", "", "LOT", "",
                mapOf(dev.panini.core.Karaka.SAMPRADANA to ExecutionExpression.Reference("मान")))
        }
        assertEquals(mapOf("मान" to prior, "LastResult" to prior), runtime.snapshot())
        assertFailsWith<CompiledPaniniExecutionException> { runtime.loadActionResult("हृञ्", 1) }
    }

    @Test fun `dynamic destination history captures the original binding before assignment`() {
        val prior = SanskritValue.Sankhya(9L, "नव")
        val runtime = CompiledProgramRuntime(mapOf("मान" to prior))
        runtime.executeParticipantGroup(SanskritValue.Gana(listOf(SanskritValue.Sankhya(2L, "द्वि"),
            SanskritValue.Sankhya(3L, "त्रि"))), "गण", "", "णिच्", "", "LOT", "",
            mapOf(dev.panini.core.Karaka.SAMPRADANA to ExecutionExpression.Reference("मान")))
        assertEquals(6L, assertIs<SanskritValue.Sankhya>(runtime.snapshot()["मान"]).value)
        assertEquals(SanskritValue.Gana(listOf(prior)), runtime.loadActionParticipantGroup("गण", 1,
            dev.panini.core.Karaka.SAMPRADANA))
    }

    @Test fun `multiple history groups preserve order and share dynamic destination binding`() {
        val source = "एक + अम् द्वि + औट् च पूर्व + ङे दा + लोट् + सिप् ।\n" +
            "त्रि + शस् एक + अम् च उत्तर + ङे दा + लोट् + सिप् ।\n" +
            "दा + ल्युट् + ङस् प्रथम + अम् कर्मन् + अम् दा + ल्युट् + ङस् कर्मन् + अम् च " +
            "परिणाम + ङे गण + णिच् + लोट् + सिप् ।\n" +
            "गण + ल्युट् + ङस् कर्मन् + अम् सूची + ङे दा + लोट् + सिप् ।"
        val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source).last())
        val generated = BytecodeCompiler.compileAndLoad(source, "MultipleDynamicHistoryGroups")
        val values = generated.getMethod("execute").invoke(null) as Map<*, *>
        assertEquals(result.typedValue, values["LastResult"])
        assertEquals(6L, assertIs<SanskritValue.Sankhya>(values["परिणाम"]).value)
        assertEquals(listOf(1L, 2L, 3L, 1L), assertIs<SanskritValue.Suchi>(values["सूची"]).items.map {
            assertIs<SanskritValue.Sankhya>(it).value
        })
    }

    @Test fun `bare history call keeps collection participant intact for counting`() {
        val source = "एक + ङस् द्वि + ङस् त्रि + ङस् च सूची + सुँ असँ + लट् + तिप् ।\n" +
            "सूची + अम् मान + ङे दा + लोट् + सिप् ।\n" +
            "दा + ल्युट् + ङस् कर्मन् + अम् गण + णिच् + लोट् + सिप् ।"
        val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source).last())
        assertEquals(3L, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
        assertEquals(result.typedValue, compiled(source, "BareRememberedListCounting"))
    }
    @Test fun `interpreter expands bare remembered objects before selecting addition`() {
        val source = "एक + अम् द्वि + औट् च मान + ङे दा + लोट् + सिप् ।\n" +
            "दा + ल्युट् + ङस् कर्मन् + अम् युज् + णिच् + लोट् + सिप् ।"
        val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source).last())
        assertEquals(3L, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
        assertEquals(result.typedValue, compiled(source, "BareRememberedAddition"))
    }

    @Test fun `one remembered participant can select a nonnumeric counting meaning`() {
        val source = "एक + अम् मान + ङे दा + लोट् + सिप् ।\n" +
            "दा + ल्युट् + ङस् कर्मन् + अम् गण + णिच् + लोट् + सिप् ।"
        val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source).last())
        assertEquals(1L, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
        assertTrue(result.trace.any { it.contains("Selected operation सूच्याकारः") }, result.trace.toString())
        assertEquals(result.typedValue, compiled(source, "BareRememberedCounting"))
    }

    @Test fun `dynamic history call publishes actual participants rather than its result`() {
        val source = "द्वि + औट् त्रि + शस् च मान + ङे दा + लोट् + सिप् ।\n" +
            "दा + ल्युट् + ङस् कर्मन् + अम् गण + णिच् + लोट् + सिप् ।\n" +
            "गण + ल्युट् + ङस् कर्मन् + अम् सूची + ङे दा + लोट् + सिप् ।"
        val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source).last())
        val value = assertIs<SanskritValue.Suchi>(compiled(source, "BareHistoryPublication"))
        assertEquals(result.typedValue, value)
        assertEquals(listOf(2L, 3L), value.items.map { assertIs<SanskritValue.Sankhya>(it).value })
    }

    @Test fun `emitted dynamic call retains resolved agent in action history`() {
        val agent = SanskritValue.Shabda("राम")
        val instructions = listOf(
            CompilerInstruction.Constant(SanskritValue.Gana(listOf(
                SanskritValue.Sankhya(2L, "द्वि"), SanskritValue.Sankhya(3L, "त्रि")))),
            CompilerInstruction.CallParticipantGroup("गण", GrammaticalFeatures(sanadi = setOf("णिच्")),
                otherBindings = mapOf(dev.panini.core.Karaka.KARTR to ExecutionExpression.TypedOperand(
                    SanskritValue.Shabda("captured-agent"), dev.panini.core.SupAffix.SU, "agent"))),
            CompilerInstruction.Store("LastResult"),
            CompilerInstruction.LoadActionParticipants("गण", dev.panini.core.Karaka.KARTR, asGroup = true),
            CompilerInstruction.Store("agent_history"))
        val program = CompilerProgram("DynamicAgentHistory", instructions)
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader).loadFromBytes(
            program.className, CompilerProgramJvmEmitter.emit(program))
        val values = generated.getMethod("execute", Map::class.java).invoke(null, mapOf("agent" to agent)) as Map<*, *>
        assertEquals(SanskritValue.Gana(listOf(agent)), values["agent_history"])
        assertEquals(6L, assertIs<SanskritValue.Sankhya>(values["LastResult"]).value)
    }

    @Test fun `dynamic history call rejects invalid group stack and leaves failed history unpublished`() {
        assertFailsWith<IllegalArgumentException> {
            CompilerIrVerifier.verify(listOf(CompilerInstruction.CallParticipantGroup("युजिँर्",
                GrammaticalFeatures())))
        }
        val runtime = CompiledProgramRuntime()
        assertFailsWith<CompiledPaniniExecutionException> {
            runtime.executeParticipantGroup(SanskritValue.Gana(listOf(SanskritValue.Shabda("राम"))),
                "युजिँर्", "", "णिच्", "", "LOT", "")
        }
        assertTrue(runtime.snapshot().isEmpty())
        assertFailsWith<CompiledPaniniExecutionException> { runtime.loadActionResult("युजिँर्", 1) }
    }
}
