package dev.panini.execution

import kotlin.test.*

class PrakriyaEffectSemanticsTest {
    @Test fun `procedure owns return effect and validates its value`() {
        for (value in listOf(SanskritValue.Sankhya(1L, "एक"), SanskritValue.Shabda("text"))) {
            val produced = ExecutionResult.Success(value.toDisplayText(), "return", typedValue = value,
                controlSignal = ExecutionControlSignal.RETURN_PROCEDURE)
            assertEquals(produced, produced.consumeLoopBreak())
            val results = PrakriyaExecutor().execute(PrakriyaExecutor.Request(
                PrakriyaInvocation(procedure(PrakriyaValueType.SANKHYA), "", ""),
                ExecutionScope(), PrakriyaRegistry(), null,
                executeBody = { _, _, _ -> listOf(produced) },
            ))
            assertNull(assertIs<ExecutionResult.Success>(results.first()).controlSignal)
            if (value is SanskritValue.Sankhya) assertEquals(value,
                assertIs<ExecutionResult.Success>(results.single()).typedValue)
            else assertIs<ExecutionResult.Failure>(results.last())
        }
    }

    @Test fun `early successful exit still enforces declared result type`() {
        for (value in listOf(SanskritValue.Sankhya(1L, "एक"), SanskritValue.Shabda("text"))) {
            var calls = 0
            val results = PrakriyaExecutor().execute(PrakriyaExecutor.Request(
                PrakriyaInvocation(procedure(PrakriyaValueType.SANKHYA), "", ""),
                ExecutionScope(), PrakriyaRegistry(), null,
                executeBody = { _, _, _ ->
                    calls++
                    listOf(ExecutionResult.Success(value.toDisplayText(), "test", typedValue = value,
                        controlSignal = ExecutionControlSignal.BREAK_LOOP))
                },
            ))
            assertEquals(1, calls)
            if (value is SanskritValue.Sankhya) {
                assertEquals(ExecutionControlSignal.BREAK_LOOP,
                    assertIs<ExecutionResult.Success>(results.single()).controlSignal)
            } else {
                assertEquals(ExecutionError.INVALID_VALUE,
                    assertIs<ExecutionResult.Failure>(results.last()).error)
            }
        }
    }

    private fun procedure(resultType: PrakriyaValueType? = null, resultSchema: String? = null): Prakriya {
        val definition = PvmScript.parse("""
            सिध् + क्त + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् मुद्र् + णिच् + लोट् + सिप् ॥
        """.trimIndent()).filterIsInstance<PvmScriptStatement.PrakriyaDefinition>().single()
        return Prakriya(definition.nameSegmented, definition.prakriya.nameIdentity, definition.body,
            signatureOverride = PrakriyaSignature(resultType = resultType, resultSchema = resultSchema))
    }

    @Test fun `kta name does not suppress repeated procedure execution`() {
        val registry = PrakriyaRegistry()
        val executor = PrakriyaExecutor()
        val invocation = PrakriyaInvocation(procedure(), "", "")
        var calls = 0
        repeat(2) {
            val result = executor.execute(PrakriyaExecutor.Request(invocation, ExecutionScope(), registry, null,
                executeBody = { _, _, _ ->
                    calls++
                    listOf(ExecutionResult.Success("$calls", "test", typedValue = SanskritValue.Sankhya(calls.toLong(), "$calls")))
                }))
            assertEquals((it + 1).toLong(), assertIs<SanskritValue.Sankhya>(assertIs<ExecutionResult.Success>(result.last()).typedValue).value)
        }
        assertEquals(2, calls)
    }

    @Test fun `invalid result is validated again on every call`() {
        val registry = PrakriyaRegistry()
        val executor = PrakriyaExecutor()
        val invocation = PrakriyaInvocation(procedure(PrakriyaValueType.SANKHYA), "", "")
        var calls = 0
        repeat(2) {
            val results = executor.execute(PrakriyaExecutor.Request(invocation, ExecutionScope(), registry, null,
                executeBody = { _, _, _ ->
                    calls++
                    listOf(ExecutionResult.Success("text", "test", typedValue = SanskritValue.Shabda("text")))
                }))
            assertIs<ExecutionResult.Failure>(results.last())
        }
        assertEquals(2, calls)
    }

    @Test fun `declared result requires a value produced by the procedure`() {
        val invocation = PrakriyaInvocation(procedure(PrakriyaValueType.SANKHYA), "", "")
        val results = PrakriyaExecutor().execute(PrakriyaExecutor.Request(
            invocation, ExecutionScope(), PrakriyaRegistry(), null,
            resolveValue = { SanskritValue.Sankhya(1L, "एक") },
            executeBody = { _, _, _ -> emptyList() },
        ))
        val failure = assertIs<ExecutionResult.Failure>(results.single())
        assertEquals(ExecutionError.INVALID_VALUE, failure.error)
        assertTrue(failure.message.contains("produced none"))
    }

    @Test fun `procedure without a declared result may produce no value`() {
        val results = PrakriyaExecutor().execute(PrakriyaExecutor.Request(
            PrakriyaInvocation(procedure(), "", ""), ExecutionScope(), PrakriyaRegistry(), null,
            executeBody = { _, _, _ -> emptyList() },
        ))
        assertTrue(results.isEmpty())
    }

    @Test fun `declared structured result also requires a produced value`() {
        val results = PrakriyaExecutor().execute(PrakriyaExecutor.Request(
            PrakriyaInvocation(procedure(resultSchema = "रूप"), "", ""),
            ExecutionScope(), PrakriyaRegistry(), null,
            executeBody = { _, _, _ -> emptyList() },
        ))
        assertTrue(assertIs<ExecutionResult.Failure>(results.single()).message.contains("produced none"))
    }
}
