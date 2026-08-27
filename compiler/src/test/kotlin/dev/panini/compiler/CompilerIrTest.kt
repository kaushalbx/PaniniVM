package dev.panini.compiler

import dev.panini.execution.ExecutionExpression
import dev.panini.execution.SanskritValue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class CompilerIrTest {
    @Test
    fun `whole-program IR verifies procedure targets`() {
        val call = CompilerInstruction.ProcedureCall("samjna_0", emptyList(), emptyList(), emptyList())
        val program = CompilerProgram(
            className = "GeneratedProgram",
            entryPoint = listOf(call),
            procedures = listOf(CompilerProcedure("samjna_0", listOf(CompilerInstruction.Return))),
        )

        CompilerProgramVerifier.verify(program)
        val failure = assertFailsWith<IllegalArgumentException> {
            CompilerProgramVerifier.verify(program.copy(procedures = emptyList()))
        }
        assertTrue(failure.message.orEmpty().contains("Unknown IR procedure"))
    }

    @Test
    fun `whole-program IR rejects duplicate procedures`() {
        val procedure = CompilerProcedure("samjna_0", listOf(CompilerInstruction.Return))
        val failure = assertFailsWith<IllegalArgumentException> {
            CompilerProgramVerifier.verify(
                CompilerProgram("GeneratedProgram", emptyList(), listOf(procedure, procedure)),
            )
        }

        assertTrue(failure.message.orEmpty().contains("Duplicate IR procedure"))
    }

    @Test
    fun `procedure call IR carries frame arguments and structured values`() {
        val value = SanskritValue.Sankhya(2L, "द्वे")
        val call = CompilerInstruction.ProcedureCall(
            methodName = "samjna_0",
            parameterNames = listOf("मान"),
            arguments = listOf("द्वि"),
            argumentValues = listOf(value),
        )

        assertEquals("samjna_0", call.methodName)
        assertEquals(listOf("मान"), call.parameterNames)
        assertEquals(listOf(value), call.argumentValues)
    }

    @Test
    fun `return and break guard are explicit IR instructions`() {
        assertIs<CompilerInstruction.Return>(CompilerInstruction.Return)
        assertIs<CompilerInstruction.ReturnIfBreak>(CompilerInstruction.ReturnIfBreak)
    }

    @Test
    fun `constants loads stores and LastResult have balanced value flow`() {
        val value = SanskritValue.Sankhya(2L, "द्वे")
        val instructions = listOf(
            CompilerInstruction.Constant(value),
            CompilerInstruction.Store("मान"),
            CompilerInstruction.Load("मान"),
            CompilerInstruction.Store("LastResult"),
            CompilerInstruction.LoadLastResult,
            CompilerInstruction.Store("प्रतिलिपि"),
        )

        CompilerIrVerifier.verify(instructions)
    }

    @Test
    fun `IR verifier rejects value stack underflow`() {
        assertFailsWith<IllegalArgumentException> {
            CompilerIrVerifier.verify(listOf(CompilerInstruction.Store("मान")))
        }
    }

    @Test
    fun `comparison consumes two values and produces a branch condition`() {
        val one = SanskritValue.Sankhya(1L, "एकम्")
        val two = SanskritValue.Sankhya(2L, "द्वे")
        val instructions = listOf(
            CompilerInstruction.Constant(one),
            CompilerInstruction.Constant(two),
            CompilerInstruction.Compare(ComparisonOperator.LESS_THAN),
            CompilerInstruction.Branch("false"),
            CompilerInstruction.Jump("end"),
            CompilerInstruction.Label("false"),
            CompilerInstruction.Label("end"),
        )

        CompilerIrVerifier.verify(instructions)
        assertTrue(CompilerValueOperations.lessThan(one, two))
    }

    @Test
    fun `arithmetic consumes numbers and produces a storable value`() {
        val one = SanskritValue.Sankhya(1L, "एकम्")
        val two = SanskritValue.Sankhya(2L, "द्वे")
        val instructions = listOf(
            CompilerInstruction.Constant(one),
            CompilerInstruction.Constant(two),
            CompilerInstruction.Arithmetic(ArithmeticOperator.ADD),
            CompilerInstruction.Store("LastResult"),
        )

        CompilerIrVerifier.verify(instructions)
        assertEquals(3L, (CompilerValueOperations.add(one, two) as SanskritValue.Sankhya).value)
    }

    @Test
    fun `typed verifier rejects non boolean branch and non numeric arithmetic`() {
        assertFailsWith<IllegalArgumentException> {
            CompilerIrVerifier.verify(
                listOf(
                    CompilerInstruction.Constant(SanskritValue.Shabda("न सत्यम्")),
                    CompilerInstruction.Branch("end"),
                    CompilerInstruction.Label("end"),
                ),
            )
        }
        assertFailsWith<IllegalArgumentException> {
            CompilerIrVerifier.verify(
                listOf(
                    CompilerInstruction.Constant(SanskritValue.Shabda("रामः")),
                    CompilerInstruction.Constant(SanskritValue.Sankhya(1L, "एकम्")),
                    CompilerInstruction.Arithmetic(ArithmeticOperator.ADD),
                    CompilerInstruction.Store("LastResult"),
                ),
            )
        }
    }

    @Test
    fun `conditional lowers to branch labels and jump`() {
        val condition = CompilerInstruction.Call(
            dhatuUpadesha = "condition",
            operationName = "condition",
            requiredSanadi = "",
            bindings = emptyMap(),
            resultMode = CallResultMode.BOOLEAN,
        )
        val consequent = CompilerInstruction.Call("then", "then", "", emptyMap())
        val alternate = CompilerInstruction.Call("else", "else", "", emptyMap())

        assertEquals(
            listOf(
                condition,
                CompilerInstruction.Branch("test_alternate"),
                consequent,
                CompilerInstruction.Jump("test_end"),
                CompilerInstruction.Label("test_alternate"),
                alternate,
                CompilerInstruction.Label("test_end"),
            ),
            CompilerIrLowering.lowerConditional(
                condition,
                listOf(consequent),
                listOf(alternate),
                "test",
            ),
        )
    }

    @Test
    fun `conditional without alternate still has a valid false target`() {
        val condition = booleanCall("condition")
        val consequent = valueCall("then")

        val instructions = CompilerIrLowering.lowerConditional(
            condition,
            listOf(consequent),
            labelPrefix = "single",
        )

        assertEquals(CompilerInstruction.Branch("single_alternate"), instructions[1])
        assertEquals(CompilerInstruction.Label("single_alternate"), instructions[4])
        CompilerIrVerifier.verify(instructions)
    }

    @Test
    fun `nested conditional labels remain distinct and valid`() {
        val inner = CompilerIrLowering.lowerConditional(
            booleanCall("inner-condition"),
            listOf(valueCall("inner-then")),
            listOf(valueCall("inner-else")),
            "inner",
        )
        val outer = CompilerIrLowering.lowerConditional(
            booleanCall("outer-condition"),
            inner,
            listOf(valueCall("outer-else")),
            "outer",
        )

        CompilerIrVerifier.verify(outer)
        assertEquals(
            outer.filterIsInstance<CompilerInstruction.Label>().map { it.name }.toSet().size,
            outer.filterIsInstance<CompilerInstruction.Label>().size,
        )
    }

    @Test
    fun `fixed repetition lowers to counter branch and back edge`() {
        val body = valueCall("body")

        val instructions = CompilerIrLowering.lowerRepeat(3, listOf(body), "test-repeat")

        assertEquals(
            listOf(
                numericConstant(0),
                CompilerInstruction.StoreLocal("test-repeat_counter"),
                CompilerInstruction.Label("test-repeat_start"),
                CompilerInstruction.LoadLocal("test-repeat_counter"),
                numericConstant(3),
                CompilerInstruction.Compare(ComparisonOperator.LESS_THAN),
                CompilerInstruction.Branch("test-repeat_exit"),
                body,
                CompilerInstruction.ConsumeBreak,
                CompilerInstruction.Branch("test-repeat_exit", whenTrue = true),
                CompilerInstruction.LoadLocal("test-repeat_counter"),
                numericConstant(1),
                CompilerInstruction.Arithmetic(ArithmeticOperator.ADD),
                CompilerInstruction.StoreLocal("test-repeat_counter"),
                CompilerInstruction.Jump("test-repeat_start"),
                CompilerInstruction.Label("test-repeat_exit"),
            ),
            instructions,
        )
    }

    @Test
    fun `fixed repetition rejects a negative count`() {
        assertFailsWith<IllegalArgumentException> {
            CompilerIrLowering.lowerRepeat(-1, emptyList())
        }
    }

    @Test
    fun `nested fixed repetitions have independent counters and labels`() {
        val inner = CompilerIrLowering.lowerRepeat(
            count = 2,
            body = listOf(valueCall("body")),
            namePrefix = "inner",
        )
        val outer = CompilerIrLowering.lowerRepeat(
            count = 3,
            body = inner,
            namePrefix = "outer",
        )

        CompilerIrVerifier.verify(outer)
        assertEquals(
            setOf("outer_counter", "inner_counter"),
            outer.filterIsInstance<CompilerInstruction.StoreLocal>().map { it.name }.toSet(),
        )
        assertEquals(
            4,
            outer.filterIsInstance<CompilerInstruction.Label>().map { it.name }.toSet().size,
        )
    }

    @Test
    fun `unbounded while lowers condition budget break and back edge`() {
        val condition = booleanCall("condition")
        val body = valueCall("body")

        val instructions = CompilerIrLowering.lowerWhile(
            condition,
            listOf(body),
            namePrefix = "test-while",
        )

        assertEquals(
            listOf(
                numericConstant(0),
                CompilerInstruction.StoreLocal("test-while_counter"),
                CompilerInstruction.Label("test-while_condition"),
                condition,
                CompilerInstruction.Branch("test-while_victory"),
                CompilerInstruction.EnterConditionIteration,
                body,
                CompilerInstruction.LoadLocal("test-while_counter"),
                numericConstant(1),
                CompilerInstruction.Arithmetic(ArithmeticOperator.ADD),
                CompilerInstruction.StoreLocal("test-while_counter"),
                CompilerInstruction.ConsumeBreak,
                CompilerInstruction.Branch("test-while_victory", whenTrue = true),
                CompilerInstruction.Jump("test-while_condition"),
                CompilerInstruction.Label("test-while_victory"),
                CompilerInstruction.LoadLocal("test-while_counter"),
                CompilerInstruction.PublishLoopOutcome("विजय"),
                CompilerInstruction.Jump("test-while_target"),
                CompilerInstruction.Label("test-while_target"),
            ),
            instructions,
        )
    }

    @Test
    fun `bounded while lowers exhaustion outcome and result target`() {
        val exhausted = valueCall("exhausted")
        val target = valueCall("target").copy(resultMode = CallResultMode.LOOP_TARGET)

        val instructions = CompilerIrLowering.lowerWhile(
            condition = booleanCall("condition"),
            body = listOf(valueCall("body")),
            maximumIterations = 4L,
            exhausted = listOf(exhausted),
            resultTarget = listOf(target),
            namePrefix = "bounded",
        )

        assertTrue(instructions.contains(numericConstant(4)))
        assertTrue(instructions.contains(CompilerInstruction.Compare(ComparisonOperator.LESS_THAN)))
        assertTrue(instructions.contains(CompilerInstruction.Label("bounded_exhausted")))
        assertTrue(instructions.contains(CompilerInstruction.PublishLoopOutcome("समाप्ति")))
        assertEquals(target, instructions.last())
        CompilerIrVerifier.verify(instructions)
    }

    @Test
    fun `reported-result while captures each body condition`() {
        val instructions = CompilerIrLowering.lowerWhile(
            condition = null,
            body = listOf(valueCall("body")),
            usesReportedCondition = true,
            negatedReportedCondition = true,
            namePrefix = "reported",
        )

        assertTrue(instructions.contains(CompilerInstruction.InitializeLoopCondition("reported_reported_condition")))
        assertTrue(instructions.contains(CompilerInstruction.ClearReportedCondition))
        assertTrue(instructions.contains(CompilerInstruction.CaptureReportedCondition("reported_reported_condition")))
    }

    @Test
    fun `nested while loops retain independent control state`() {
        val inner = CompilerIrLowering.lowerWhile(
            booleanCall("inner-condition"),
            listOf(valueCall("inner-body")),
            namePrefix = "inner",
        )
        val outer = CompilerIrLowering.lowerWhile(
            booleanCall("outer-condition"),
            inner,
            maximumIterations = 2L,
            namePrefix = "outer",
        )

        CompilerIrVerifier.verify(outer)
        assertEquals(
            setOf("outer_counter", "inner_counter"),
            outer.filterIsInstance<CompilerInstruction.StoreLocal>().map { it.name }.toSet(),
        )
    }

    @Test
    fun `IR verifier rejects unknown loop state`() {
        assertFailsWith<IllegalArgumentException> {
            CompilerIrVerifier.verify(listOf(CompilerInstruction.LoadLocal("missing")))
        }
        assertFailsWith<IllegalArgumentException> {
            CompilerIrVerifier.verify(
                listOf(CompilerInstruction.TestLoopCondition("missing", negated = false)),
            )
        }
    }

    @Test
    fun `IR verifier rejects missing and duplicate labels`() {
        assertFailsWith<IllegalArgumentException> {
            CompilerIrVerifier.verify(listOf(CompilerInstruction.Jump("missing")))
        }
        assertFailsWith<IllegalArgumentException> {
            CompilerIrVerifier.verify(
                listOf(
                    CompilerInstruction.Label("same"),
                    CompilerInstruction.Label("same"),
                ),
            )
        }
    }

    @Test
    fun `numeric leaf lowers to a backend neutral call`() {
        val plan = requireNotNull(DirectLeafPlanner.planAny(
            "एक + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ।",
        ))

        val call = assertIs<CompilerInstruction.Call>(CompilerIrLowering.lowerLeaf(plan))

        assertEquals("युजिँर्", call.dhatuUpadesha)
        assertEquals("सङ्ख्यायोजनम्", call.operationName)
        assertEquals("", call.requiredSanadi)
        assertEquals(CallResultMode.VALUE, call.resultMode)
        assertNull(call.destination)
        val values = call.bindings.values.flatMap { expression ->
            when (expression) {
                is ExecutionExpression.Coordination -> expression.members
                else -> listOf(expression)
            }
        }.mapNotNull { expression ->
            when (expression) {
                is ExecutionExpression.Pada -> expression.value
                is ExecutionExpression.TypedOperand -> expression.value
                else -> null
            }
        }.filterIsInstance<SanskritValue.Sankhya>().map { it.value }
        assertEquals(listOf(1L, 2L), values)
    }

    @Test
    fun `numeric value leaf lowers to arithmetic and stores LastResult`() {
        val plan = requireNotNull(DirectLeafPlanner.planAny(
            "एक + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ।",
        ))

        val instructions = CompilerIrLowering.lowerLeafValues(plan)

        assertIs<CompilerInstruction.Constant>(instructions.first())
        assertEquals(CompilerInstruction.Arithmetic(ArithmeticOperator.ADD), instructions[2])
        assertEquals(CompilerInstruction.Store("LastResult"), instructions.last())
        CompilerIrVerifier.verify(instructions)
    }

    @Test
    fun `boolean and loop target modes survive IR lowering`() {
        val condition = requireNotNull(DirectLeafPlanner.planAny(
            "द्वि + अम् एक + अम् च विद् + लोट् + सिप् ।",
        ))
        val target = requireNotNull(DirectLeafPlanner.planAny(
            "चक्रफल + अम् मुद्र् + लोट् + सिप् ।",
        ))

        assertEquals(
            CallResultMode.BOOLEAN,
            assertIs<CompilerInstruction.Call>(
                CompilerIrLowering.lowerLeaf(condition, CallResultMode.BOOLEAN),
            ).resultMode,
        )
        assertEquals(
            CallResultMode.LOOP_TARGET,
            assertIs<CompilerInstruction.Call>(
                CompilerIrLowering.lowerLeaf(target, CallResultMode.LOOP_TARGET),
            ).resultMode,
        )
    }


    private fun booleanCall(name: String) = CompilerInstruction.Call(
        dhatuUpadesha = name,
        operationName = name,
        requiredSanadi = "",
        bindings = emptyMap(),
        resultMode = CallResultMode.BOOLEAN,
    )

    private fun valueCall(name: String) = CompilerInstruction.Call(
        dhatuUpadesha = name,
        operationName = name,
        requiredSanadi = "",
        bindings = emptyMap(),
    )

    private fun numericConstant(value: Long) = CompilerInstruction.Constant(
        SanskritValue.Sankhya(value, value.toString()),
    )
}
