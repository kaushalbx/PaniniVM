package dev.panini.compiler

import dev.panini.execution.*
import java.lang.reflect.InvocationTargetException
import kotlin.test.*

class ParticipantHistoryAssignmentTest {
    private val addition = "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।"

    private fun parity(source: String, name: String, initial: Map<String, SanskritValue> = emptyMap()): Map<*, *> {
        val results = PaniniVM().evalScript(source, scope = ExecutionScope(environment = ValueEnvironment(initial)))
        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        val generated = BytecodeCompiler.compileAndLoad(source, name)
        val values = generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>
        assertEquals(assertIs<ExecutionResult.Success>(results.last()).value,
            assertIs<SanskritValue>(values["LastResult"]).toDisplayText())
        assertTrue(values.keys.none { it.toString().startsWith("__pvm_history_group_") })
        return values
    }

    private fun numbers(value: Any?): List<Long> = assertIs<SanskritValue.Suchi>(value).items.map {
        assertIs<SanskritValue.Sankhya>(it).value
    }

    @Test fun `remembered operand example prints twelve in both backends`() {
        val source = java.io.File("examples/control_flow/remembered_operands.pvm").readText()
        assertEquals("द्वादश", assertIs<SanskritValue>(
            parity(source, "RememberedOperandsExample")["LastResult"]).toDisplayText())
    }

    @Test fun `numeric fold renders the final result rather than host input spellings`() {
        val source = "पूर्व + अम् उत्तर + अम् च मान + ङे दा + लोट् + सिप् ।\n" +
            "दा + ल्युट् + ङस् कर्मन् + अम् शून्य + अम् च युज् + णिच् + लोट् + सिप् ।"
        val values = parity(source, "FinalNumericFoldRendering", mapOf(
            "पूर्व" to SanskritValue.Sankhya(-1L, "negative-one"),
            "उत्तर" to SanskritValue.Sankhya(2L, "two")))
        assertEquals(1L, assertIs<SanskritValue.Sankhya>(values["LastResult"]).value)
    }

    @Test fun `numeric fold converts zero divisors and overflow into execution failures`() {
        for ((operator, operands) in listOf(
            ArithmeticOperator.DIVIDE to listOf(1L, 0L),
            ArithmeticOperator.REMAINDER to listOf(1L, 0L),
            ArithmeticOperator.ADD to listOf(Long.MAX_VALUE, 1L),
            ArithmeticOperator.MULTIPLY to listOf(Long.MAX_VALUE, 2L)
        )) {
            val failure = assertFailsWith<CompiledPaniniExecutionException> {
                CompilerValueOperations.foldNumericOperands(
                    arrayOf(SanskritValue.Gana(operands.map { SanskritValue.Sankhya(it, it.toString()) })),
                    "1", operator.name, 2)
            }
            assertTrue(failure.message!!.contains("overflow or division by zero"))
        }
    }

    @Test fun `remembered arithmetic inputs expand before numeric folding and remain in history`() {
        val source = "$addition\n" +
            "युज् + ल्युट् + ङस् कर्मन् + अम् नवन् + शस् च युज् + णिच् + लोट् + सिप् ।\n" +
            "युज् + ल्युट् + ङस् कर्मन् + अम् सूची + ङे दा + लोट् + सिप् ।"
        assertEquals(listOf(1L, 2L, 9L), numbers(parity(source, "RememberedNumericFold")["सूची"]))
        val fold = CompilerFrontend.lower(source, "RememberedNumericFoldIr").entryPoint
            .filterIsInstance<CompilerInstruction.FoldNumericOperands>().single()
        assertEquals(ArithmeticOperator.ADD, fold.operator)
        assertEquals(listOf(true, false), fold.expandGroups)
    }

    @Test fun `numeric folding does not flatten a collection participant`() {
        for (flags in listOf(emptyList(), listOf(true))) {
            assertFailsWith<IllegalArgumentException> {
                CompilerIrVerifier.verify(listOf(CompilerInstruction.FoldNumericOperands(ArithmeticOperator.ADD, flags)))
            }
        }
        assertFailsWith<CompiledPaniniExecutionException> {
            CompilerValueOperations.foldNumericOperands(arrayOf(SanskritValue.Gana(emptyList())), "1", "ADD")
        }
        val collection = SanskritValue.Suchi(listOf(SanskritValue.Sankhya(1L, "एक")))
        for ((value, group) in listOf(collection to false, SanskritValue.Sankhya(1L, "एक") to true,
            SanskritValue.Shabda("राम") to false)) {
            assertFailsWith<IllegalArgumentException> {
                CompilerIrVerifier.verify(listOf(CompilerInstruction.Constant(value),
                    CompilerInstruction.FoldNumericOperands(ArithmeticOperator.ADD, listOf(group)),
                    CompilerInstruction.Store("LastResult")))
            }
        }
        assertFailsWith<CompiledPaniniExecutionException> {
            CompilerValueOperations.foldNumericOperands(
                arrayOf(SanskritValue.Gana(listOf(collection))), "1", ArithmeticOperator.ADD.name)
        }
        assertEquals(3L, assertIs<SanskritValue.Sankhya>(CompilerValueOperations.foldNumericOperands(
            arrayOf(SanskritValue.Gana(listOf(SanskritValue.Sankhya(1L, "एक"),
                SanskritValue.Sankhya(2L, "द्वि")))), "1", ArithmeticOperator.ADD.name)).value)
    }

    @Test fun `unselected remembered arithmetic branch does not require an action frame`() {
        val source = "यदि एक + सुँ एक + टा सम + सुँ असँ + लट् + तिप् तर्हि " +
            "नव + अम् मुद्र् + णिच् + लोट् + सिप् अन्यथा " +
            "युज् + ल्युट् + ङस् कर्मन् + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ।"
        assertEquals("नव", assertIs<SanskritValue>(parity(source, "LazyRememberedArithmetic")["LastResult"]).toDisplayText())
    }

    @Test fun `remembered numeric operands produce arithmetic rather than a packed collection`() {
        for ((index, verb) in listOf("युज् + णिच्", "गण + णिच्").withIndex()) {
            val source = "$addition\nयुज् + ल्युट् + ङस् कर्मन् + अम् नवन् + शस् च $verb + लोट् + सिप् ।"
            val values = parity(source, "RememberedArithmetic$index")
            assertEquals(if (index == 0) 12L else 18L,
                assertIs<SanskritValue.Sankhya>(values["LastResult"]).value)
        }
    }

    @Test fun `multiplication validates operand count after expanding remembered participants`() {
        val instructions = listOf(CompilerInstruction.Constant(SanskritValue.Gana(
            listOf(SanskritValue.Sankhya(1L, "एक")))),
            CompilerInstruction.FoldNumericOperands(ArithmeticOperator.MULTIPLY, listOf(true), minimumOperands = 2),
            CompilerInstruction.Store("LastResult"))
        CompilerIrVerifier.verify(instructions)
        val program = CompilerProgram("SingleRememberedMultiplicand", instructions)
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader).loadFromBytes(
            program.className, CompilerProgramJvmEmitter.emit(program))
        assertIs<CompiledPaniniExecutionException>(assertFailsWith<InvocationTargetException> {
            generated.getMethod("execute").invoke(null)
        }.cause)
        assertFailsWith<IllegalArgumentException> {
            CompilerIrVerifier.verify(listOf(CompilerInstruction.Constant(SanskritValue.Sankhya(1L, "एक")),
                CompilerInstruction.FoldNumericOperands(ArithmeticOperator.ADD, listOf(false), minimumOperands = 0)))
        }
    }

    @Test fun `noncommutative folds preserve remembered and written operand order`() {
        val setup = "नवन् + शस् द्वि + औट् च मान + ङे दा + लोट् + सिप् ।\n"
        for ((index, verb) in listOf("वि + युज् + णिच्", "हृ").withIndex()) {
            for ((order, phrase) in listOf(
                "दा + ल्युट् + ङस् कर्मन् + अम् एक + अम् च",
                "एक + अम् दा + ल्युट् + ङस् कर्मन् + अम् च"
            ).withIndex()) {
                val source = setup + "$phrase $verb + लोट् + सिप् ।"
                if (index == 0 && order == 1) {
                    assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last())
                    val generated = BytecodeCompiler.compileAndLoad(source, "NegativeRememberedFold")
                    assertIs<CompiledPaniniExecutionException>(assertFailsWith<InvocationTargetException> {
                        generated.getMethod("execute").invoke(null)
                    }.cause)
                    continue
                }
                val expected = if (index == 0) { if (order == 0) 6L else -10L }
                    else { if (order == 0) 4L else 0L }
                assertEquals(expected, assertIs<SanskritValue.Sankhya>(
                    parity(source, "OrderedRememberedFold${index}_$order")["LastResult"]).value)
            }
        }
    }

    @Test fun `host collection payloads remain single participants after source reassignment`() {
        val inner = SanskritValue.Suchi(listOf(SanskritValue.Sankhya(1L, "एक")))
        for ((index, value) in listOf<SanskritValue>(SanskritValue.Gana(listOf(inner)),
            SanskritValue.Suchi(listOf(inner))).withIndex()) {
            val source = "वस्तु + अम् आधार + ङे दा + लोट् + सिप् ।\n" +
                "नवन् + शस् वस्तु + ङे दा + लोट् + सिप् ।\n" +
                "दा + ल्युट् + ङस् प्रथम + अम् कर्मन् + अम् प्रतिलिपि + ङे दा + लोट् + सिप् ।\n" +
                "प्रतिलिपि + अम् मुद्र् + णिच् + लोट् + सिप् ।"
            assertEquals(value, parity(source, "HostParticipantPayload$index", mapOf("वस्तु" to value))["प्रतिलिपि"])
        }
    }

    @Test fun `history assignment reads old destination before replacing it`() {
        val source = "नवन् + शस् मान + ङे दा + लोट् + सिप् ।\n$addition\n" +
            "युज् + ल्युट् + ङस् कर्मन् + अम् मान + अम् च मान + ङे दा + लोट् + सिप् ।\n" +
            "मान + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        assertEquals(listOf(1L, 2L, 9L), numbers(parity(source, "SelfParticipantAssignment")["मान"]))
    }

    @Test fun `history assignment updates local list parameter without replacing caller name`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ सूची + अम् जन् + णिच् + लट् + तिप् ।
            मान + सुँ सूची + सुँ इति मान + सुँ ।
            युज् + ल्युट् + ङस् कर्मन् + अम् मान + ङे दा + लोट् + सिप् ॥
            एक + ङस् एक + ङस् च सूची + सुँ असँ + लट् + तिप् ।
            नवन् + शस् मान + ङे दा + लोट् + सिप् ।
            एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।
            सूची + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ततः मुद्र् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val values = parity(source, "LocalParticipantAssignment")
        assertEquals(9L, assertIs<SanskritValue.Sankhya>(values["मान"]).value)
        assertEquals("[एक, द्वि]", assertIs<SanskritValue>(values["LastResult"]).toDisplayText())
        assertTrue(values.keys.none { it.toString().startsWith("__pvm_parameter_") })
    }

    @Test fun `assigned participant history keeps original action inputs rather than packed result`() {
        val source = "$addition\n" +
            "युज् + ल्युट् + ङस् कर्मन् + अम् सूची + ङे दा + लोट् + सिप् ।\n" +
            "दा + ल्युट् + ङस् कर्मन् + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        assertEquals(listOf(1L, 2L), numbers(parity(source, "AssignedParticipantHistory")["सूची"]))
    }

    @Test fun `single collection participant remains the collection not its members or nested list`() {
        val source = "एक + सुँ द्वि + औ च + इति सूची + सुँ असँ + लट् + तिप् ।\n" +
            "सूची + अम् मूल्य + ङे दा + लोट् + सिप् ।\n" +
            "दा + ल्युट् + ङस् कर्मन् + अम् प्रतिलिपि + ङे दा + लोट् + सिप् ।\n" +
            "प्रतिलिपि + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val values = parity(source, "SingleCollectionParticipantAssignment")
        assertEquals(values["मूल्य"], values["प्रतिलिपि"])
        assertEquals(listOf(1L, 2L), numbers(values["प्रतिलिपि"]))
    }

    @Test fun `mixed history and ordinary operands preserve written order`() {
        val source = "$addition\n" +
            "नवन् + शस् युज् + ल्युट् + ङस् कर्मन् + अम् एक + अम् च मान + ङे दा + लोट् + सिप् ।\n" +
            "मान + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        assertEquals(listOf(9L, 1L, 2L, 1L), numbers(parity(source, "MixedHistoryAssignment")["मान"]))
    }

    @Test fun `ordered history assignment selects first rather than latest action`() {
        val source = "$addition\nनवन् + शस् एक + अम् च युज् + णिच् + लोट् + सिप् ।\n" +
            "युज् + ल्युट् + ङस् प्रथम + अम् कर्मन् + अम् पूर्व + ङे दा + लोट् + सिप् ।\n" +
            "युज् + ल्युट् + ङस् कर्मन् + अम् नवीन + ङे दा + लोट् + सिप् ।\n" +
            "पूर्व + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val values = parity(source, "OrderedHistoryAssignment")
        assertEquals(listOf(1L, 2L), numbers(values["पूर्व"]))
        assertEquals(listOf(9L, 1L), numbers(values["नवीन"]))
    }

    @Test fun `missing participant history fails rather than assigning a literal`() {
        val source = "युज् + ल्युट् + ङस् कर्मन् + अम् मान + ङे दा + लोट् + सिप् ।"
        assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last())
        val generated = BytecodeCompiler.compileAndLoad(source, "MissingParticipantAssignment")
        assertIs<CompiledPaniniExecutionException>(assertFailsWith<InvocationTargetException> {
            generated.getMethod("execute").invoke(null)
        }.cause)
    }

    @Test fun `unselected assignment branch does not evaluate missing history`() {
        val source = "यदि एक + सुँ एक + टा सम + सुँ असँ + लट् + तिप् " +
            "तर्हि एक + अम् मुद्र् + णिच् + लोट् + सिप् अन्यथा " +
            "युज् + ल्युट् + ङस् कर्मन् + अम् मान + ङे दा + लोट् + सिप् ।"
        val values = parity(source, "LazyParticipantAssignment")
        assertFalse(values.containsKey("मान"))
    }
}
