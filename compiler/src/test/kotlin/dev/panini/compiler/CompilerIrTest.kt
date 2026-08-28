package dev.panini.compiler

import dev.panini.execution.ExecutionExpression
import dev.panini.execution.SanskritValue
import dev.panini.execution.renderSankhyaResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class CompilerIrTest {
    @Test
    fun `whole-program IR verifies procedure targets`() {
        val call = CompilerInstruction.InvokeProcedure("samjna_0", 0)
        val program = CompilerProgram(
            className = "GeneratedProgram",
            entryPoint = listOf(
                CompilerInstruction.EnterFrame(emptyList()),
                call,
                CompilerInstruction.ExitFrame,
            ),
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
        val frame = CompilerInstruction.EnterFrame(parameterNames = listOf("मान"))

        assertEquals(listOf("मान"), frame.parameterNames)
        CompilerIrVerifier.verify(
            listOf(CompilerInstruction.Constant(value), frame),
        )
    }

    @Test
    fun `whole-program verifier checks explicit frame shape and procedure arity`() {
        val procedure = CompilerProcedure(
            methodName = "samjna_0",
            instructions = listOf(CompilerInstruction.Return),
            parameterNames = listOf("मान"),
        )
        fun program(frame: CompilerInstruction.EnterFrame, count: Int) = CompilerProgram(
            className = "GeneratedProgram",
            entryPoint = listOf(
                frame,
                CompilerInstruction.InvokeProcedure("samjna_0", count),
                CompilerInstruction.ExitFrame,
            ),
            procedures = listOf(procedure),
        )

        CompilerProgramVerifier.verify(
            program(CompilerInstruction.EnterFrame(listOf("मान")), 1).copy(
                entryPoint = listOf(
                    CompilerInstruction.Constant(SanskritValue.Sankhya(2L, "द्वे")),
                    CompilerInstruction.EnterFrame(listOf("मान")),
                    CompilerInstruction.InvokeProcedure("samjna_0", 1),
                    CompilerInstruction.ExitFrame,
                ),
            ),
        )
        assertFailsWith<IllegalArgumentException> {
            CompilerProgramVerifier.verify(
                program(CompilerInstruction.EnterFrame(emptyList()), 0),
            )
        }
        assertFailsWith<IllegalArgumentException> {
            CompilerProgramVerifier.verify(
                program(CompilerInstruction.EnterFrame(listOf("मान", "मान")), 2),
            )
        }
    }

    @Test
    fun `frame instructions cannot appear without a complete call sequence`() {
        val procedure = CompilerProcedure("samjna_0", listOf(CompilerInstruction.Return))
        assertFailsWith<IllegalArgumentException> {
            CompilerProgramVerifier.verify(
                CompilerProgram(
                    "GeneratedProgram",
                    listOf(CompilerInstruction.InvokeProcedure("samjna_0", 0)),
                    listOf(procedure),
                ),
            )
        }
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
    fun `list construction consumes its elements and produces one value`() {
        val instructions = listOf(
            CompilerInstruction.Constant(SanskritValue.Shabda("प्रथम")),
            CompilerInstruction.Constant(SanskritValue.Shabda("द्वितीय")),
            CompilerInstruction.BuildList(2),
            CompilerInstruction.Store("LastResult"),
        )

        CompilerIrVerifier.verify(instructions)
        assertFailsWith<IllegalArgumentException> {
            CompilerIrVerifier.verify(listOf(CompilerInstruction.BuildList(1)))
        }
    }

    @Test
    fun `record construction and field access have explicit value flow`() {
        val instructions = listOf(
            CompilerInstruction.Constant(SanskritValue.Shabda("विजय")),
            numericConstant(3),
            CompilerInstruction.BuildRecord("परिणाम", listOf("अवस्था", "प्रयत्नसङ्ख्या")),
            CompilerInstruction.LoadField("प्रयत्नसङ्ख्या"),
            CompilerInstruction.Store("LastResult"),
        )

        CompilerIrVerifier.verify(instructions)
        assertFailsWith<IllegalArgumentException> {
            CompilerIrVerifier.verify(
                listOf(
                    CompilerInstruction.Constant(SanskritValue.Shabda("विजय")),
                    CompilerInstruction.Constant(SanskritValue.Shabda("पुनः")),
                    CompilerInstruction.BuildRecord("परिणाम", listOf("अवस्था", "अवस्था")),
                ),
            )
        }
    }

    @Test
    fun `text rendering and truth primitives have typed stack flow`() {
        val two = SanskritValue.Sankhya(2L, "द्वे")
        CompilerIrVerifier.verify(
            listOf(
                CompilerInstruction.Constant(two),
                CompilerInstruction.IsEven,
                CompilerInstruction.Store("समत्वम्"),
                CompilerInstruction.Constant(SanskritValue.Shabda("फलम्")),
                CompilerInstruction.Constant(two),
                CompilerInstruction.RenderText(2),
                CompilerInstruction.Store("LastResult"),
            ),
        )
        assertEquals(SanskritValue.Satya(true), CompilerValueOperations.isEven(two))
        assertEquals(
            "फलम् द्वे",
            CompilerValueOperations.renderText(arrayOf(SanskritValue.Shabda("फलम्"), two)).toDisplayText(),
        )
        assertFailsWith<IllegalArgumentException> {
            CompilerIrVerifier.verify(
                listOf(CompilerInstruction.Constant(SanskritValue.Shabda("द्वे")), CompilerInstruction.IsEven),
            )
        }
        assertFailsWith<IllegalArgumentException> {
            CompilerIrVerifier.verify(listOf(CompilerInstruction.RenderText(1)))
        }
    }

    @Test
    fun `runtime boundary report counts only generic action calls`() {
        val call = CompilerInstruction.Call("धातुः", "विशिष्टक्रिया", "", emptyMap())
        val program = CompilerProgram(
            "GeneratedProgram",
            listOf(call, CompilerInstruction.Store("LastResult")),
            listOf(
                CompilerProcedure(
                    "samjna_0",
                    listOf(call, CompilerInstruction.Store("LastResult"), CompilerInstruction.Return),
                ),
            ),
        )

        assertEquals(mapOf("विशिष्टक्रिया" to 2), CompilerRuntimeBoundaryReport.operations(program))
    }

    @Test
    fun `collection operations preserve verifier value types`() {
        val list = SanskritValue.Suchi(listOf(SanskritValue.Shabda("एक")))
        CompilerIrVerifier.verify(listOf(
            CompilerInstruction.Constant(list),
            CompilerInstruction.Collection(CollectionOperator.REVERSE),
            CompilerInstruction.Duplicate,
            CompilerInstruction.Collection(CollectionOperator.CONCAT),
            CompilerInstruction.Constant(SanskritValue.Sankhya(1, "एकम्")),
            CompilerInstruction.Collection(CollectionOperator.INDEX),
            CompilerInstruction.BuildList(1),
            CompilerInstruction.Collection(CollectionOperator.LENGTH),
            CompilerInstruction.Store("LastResult"),
        ))
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
        assertEquals(1L, (CompilerValueOperations.minimum(one, two) as SanskritValue.Sankhya).value)
        assertEquals(8L, (CompilerValueOperations.power(two, SanskritValue.Sankhya(3L, "त्रीणि")) as SanskritValue.Sankhya).value)
        assertFailsWith<IllegalArgumentException> {
            CompilerValueOperations.power(two, SanskritValue.Sankhya(-1L, "-1"))
        }
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
        )
        val consequent = CompilerInstruction.Call("then", "then", "", emptyMap())
        val alternate = CompilerInstruction.Call("else", "else", "", emptyMap())

        assertEquals(
            listOf(
                condition,
                CompilerInstruction.Duplicate,
                CompilerInstruction.Store("LastResult"),
                CompilerInstruction.Booleanize,
                CompilerInstruction.Branch("test_alternate"),
                consequent,
                CompilerInstruction.Store("LastResult"),
                CompilerInstruction.Jump("test_end"),
                CompilerInstruction.Label("test_alternate"),
                alternate,
                CompilerInstruction.Store("LastResult"),
                CompilerInstruction.Label("test_end"),
            ),
            CompilerIrLowering.lowerConditional(
                condition,
                listOf(consequent, CompilerInstruction.Store("LastResult")),
                listOf(alternate, CompilerInstruction.Store("LastResult")),
                "test",
            ),
        )
    }

    @Test
    fun `conditional without alternate still has a valid false target`() {
        val condition = booleanCall("condition")
        val consequent = valueInstructions("then")

        val instructions = CompilerIrLowering.lowerConditional(
            condition,
            consequent,
            labelPrefix = "single",
        )

        assertEquals(CompilerInstruction.Branch("single_alternate"), instructions[4])
        assertEquals(CompilerInstruction.Label("single_alternate"), instructions[8])
        CompilerIrVerifier.verify(instructions)
    }

    @Test
    fun `nested conditional labels remain distinct and valid`() {
        val inner = CompilerIrLowering.lowerConditional(
            booleanCall("inner-condition"),
            valueInstructions("inner-then"),
            valueInstructions("inner-else"),
            "inner",
        )
        val outer = CompilerIrLowering.lowerConditional(
            booleanCall("outer-condition"),
            inner,
            valueInstructions("outer-else"),
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
        val body = valueInstructions("body")

        val instructions = CompilerIrLowering.lowerRepeat(3, body, "test-repeat")

        assertEquals(
            listOf(
                numericConstant(0),
                CompilerInstruction.StoreLocal("test-repeat_counter"),
                CompilerInstruction.Label("test-repeat_start"),
                CompilerInstruction.LoadLocal("test-repeat_counter"),
                numericConstant(3),
                CompilerInstruction.Compare(ComparisonOperator.LESS_THAN),
                CompilerInstruction.Branch("test-repeat_exit"),
                *body.toTypedArray(),
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
            body = valueInstructions("body"),
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
        val body = valueInstructions("body")

        val instructions = CompilerIrLowering.lowerWhile(
            condition,
            body,
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
                *body.toTypedArray(),
                CompilerInstruction.LoadLocal("test-while_counter"),
                numericConstant(1),
                CompilerInstruction.Arithmetic(ArithmeticOperator.ADD),
                CompilerInstruction.StoreLocal("test-while_counter"),
                CompilerInstruction.ConsumeBreak,
                CompilerInstruction.Branch("test-while_victory", whenTrue = true),
                CompilerInstruction.Jump("test-while_condition"),
                CompilerInstruction.Label("test-while_victory"),
                CompilerInstruction.Constant(SanskritValue.Shabda("विजय")),
                CompilerInstruction.LoadLocal("test-while_counter"),
                CompilerInstruction.Cardinalize,
                CompilerInstruction.Duplicate,
                CompilerInstruction.Store("प्रयत्नसङ्ख्या"),
                CompilerInstruction.BuildRecord("परिणाम", listOf("अवस्था", "प्रयत्नसङ्ख्या")),
                CompilerInstruction.Duplicate,
                CompilerInstruction.Store("परिणाम"),
                CompilerInstruction.Store("LastResult"),
                CompilerInstruction.Jump("test-while_target"),
                CompilerInstruction.Label("test-while_target"),
            ),
            instructions,
        )
    }

    @Test
    fun `bounded while lowers exhaustion outcome and result target`() {
        val exhausted = valueInstructions("exhausted")
        val target = valueCall("target")

        val instructions = CompilerIrLowering.lowerWhile(
            condition = booleanCall("condition"),
            body = valueInstructions("body"),
            maximumIterations = 4L,
            exhausted = exhausted,
            resultTarget = listOf(target, CompilerInstruction.Store("LastResult")),
            namePrefix = "bounded",
        )

        assertTrue(instructions.contains(numericConstant(4)))
        assertTrue(instructions.contains(CompilerInstruction.Compare(ComparisonOperator.LESS_THAN)))
        assertTrue(instructions.contains(CompilerInstruction.Label("bounded_exhausted")))
        assertTrue(
            instructions.contains(
                CompilerInstruction.BuildRecord("परिणाम", listOf("अवस्था", "प्रयत्नसङ्ख्या")),
            ),
        )
        assertTrue(instructions.contains(CompilerInstruction.Store("परिणाम")))
        assertEquals(CompilerInstruction.Store("LastResult"), instructions.last())
        CompilerIrVerifier.verify(instructions)
    }

    @Test
    fun `reported-result while captures each body condition`() {
        val instructions = CompilerIrLowering.lowerWhile(
            condition = null,
            body = valueInstructions("body"),
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
            valueInstructions("inner-body"),
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
    fun `numeric condition lowers operands and comparison without a runtime call`() {
        val greater = requireNotNull(DirectLeafPlanner.planAny(
            "द्वि + अम् एक + अम् च विद् + लोट् + सिप् ।",
        ))
        val less = requireNotNull(DirectLeafPlanner.planAny(
            "एक + अम् द्वि + अम् च नि + विद् + लोट् + सिप् ।",
        ))

        val greaterInstructions = CompilerIrLowering.lowerCondition(greater)
        val lessInstructions = CompilerIrLowering.lowerCondition(less)

        assertEquals(CompilerInstruction.Compare(ComparisonOperator.GREATER_THAN), greaterInstructions.last())
        assertEquals(CompilerInstruction.Compare(ComparisonOperator.LESS_THAN), lessInstructions.last())
        assertTrue(greaterInstructions.none { it is CompilerInstruction.Call })
        assertTrue(lessInstructions.none { it is CompilerInstruction.Call })
    }

    @Test
    fun `boolean and loop target behavior lowers to ordinary value IR`() {
        val condition = requireNotNull(DirectLeafPlanner.planAny(
            "द्वि + अम् एक + अम् च विद् + लोट् + सिप् ।",
        ))
        val target = requireNotNull(DirectLeafPlanner.planAny(
            "चक्रफल + अम् मुद्र् + लोट् + सिप् ।",
        ))

        val conditionInstructions = CompilerIrLowering.lowerCondition(condition)
        assertEquals(CompilerInstruction.Compare(ComparisonOperator.GREATER_THAN), conditionInstructions.last())

        val targetInstructions = CompilerIrLowering.lowerLoopTarget(target)
        assertTrue(targetInstructions.contains(CompilerInstruction.LoadField("अवस्था")))
        assertTrue(targetInstructions.contains(CompilerInstruction.Store("चक्रफल")))
        assertTrue(targetInstructions.contains(CompilerInstruction.Pop))
        assertEquals(CompilerInstruction.Store("LastResult"), targetInstructions.last())
    }


    private fun booleanCall(name: String) = CompilerInstruction.Call(
        dhatuUpadesha = name,
        operationName = name,
        requiredSanadi = "",
        bindings = emptyMap(),
    )

    private fun valueCall(name: String) = CompilerInstruction.Call(
        dhatuUpadesha = name,
        operationName = name,
        requiredSanadi = "",
        bindings = emptyMap(),
    )

    private fun valueInstructions(name: String) = listOf(
        valueCall(name),
        CompilerInstruction.Store("LastResult"),
    )

    private fun numericConstant(value: Long) = CompilerInstruction.Constant(
        SanskritValue.Sankhya(value, renderSankhyaResult(value) ?: value.toString()),
    )
}
