package dev.panini.execution

import dev.panini.actions.io.ReadAction
import dev.panini.core.Karaka
import dev.panini.execution.external.ExternalCapabilityDispatcher
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertEquals

class ReadActionTest {
    @Test
    fun `unresolved choice fails even when other allowed choices exist`() {
        val dispatcher = ExternalCapabilityDispatcher().apply {
            register(ExecutionEffect.READ_RESOURCE) { _, _ -> error("Missing choice must fail before dispatch") }
        }
        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Pada("वर्ण"),
                Karaka.SAMPRADANA to ExecutionExpression.Coordination(
                    ExecutionExpression.Pada("विकल्प"), ExecutionExpression.Pada("नील"),
                    ExecutionExpression.Reference("missing"),
                ),
            ),
            externalDispatcher = dispatcher,
        )
        assertIs<ExecutionResult.Failure>(ReadAction.execute(context, ReadAction.op()))
    }

    @Test
    fun `choice references resolve as data without changing the choice type`() {
        val dispatcher = ExternalCapabilityDispatcher().apply {
            register(ExecutionEffect.READ_RESOURCE) { payload, _ ->
                val request = InputRequest.decode(payload)
                assertEquals(InputValueType.CHOICE, request?.type)
                assertEquals(listOf("नील"), request?.choices)
                "नील"
            }
        }
        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Pada("वर्ण"),
                Karaka.SAMPRADANA to ExecutionExpression.Coordination(
                    ExecutionExpression.Pada("विकल्प"), ExecutionExpression.Reference("वर्णनाम"),
                ),
            ),
            variables = mapOf("वर्णनाम" to SanskritValue.Shabda("नील")),
            externalDispatcher = dispatcher,
        )
        assertEquals(SanskritValue.Shabda("नील"), assertIs<ExecutionResult.Success>(ReadAction.execute(context, ReadAction.op())).typedValue)
    }

    @Test
    fun `malformed explicit bounds do not silently inherit the active range`() {
        val dispatcher = ExternalCapabilityDispatcher().apply {
            register(ExecutionEffect.READ_RESOURCE) { _, _ -> error("Invalid bounds must fail before input") }
        }
        for (role in listOf(Karaka.APADANA, Karaka.ADHIKARANA)) {
            for (bound in listOf(
                ExecutionExpression.Reference("missing"),
                ExecutionExpression.Pada("शब्द"),
                ExecutionExpression.Coordination(ExecutionExpression.sankhya(1, "एक"), ExecutionExpression.sankhya(2, "द्वि")),
            )) {
                val context = ExecutionContext(
                    bindings = mapOf(
                        Karaka.KARMAN to ExecutionExpression.Pada("निवेश"),
                        Karaka.KARANA to ExecutionExpression.Pada("सङ्ख्या"),
                        role to bound,
                    ),
                    variables = mapOf(ACTIVE_RANGE_NAME to SanskritValue.Range(
                        SanskritValue.Sankhya(1, "एक"), SanskritValue.Sankhya(10, "दश"),
                    )),
                    externalDispatcher = dispatcher,
                )
                assertIs<ExecutionResult.Failure>(ReadAction.execute(context, ReadAction.op()))
            }
        }
    }

    @Test
    fun `type declaration uses nominal identity rather than rendered value`() {
        val dispatcher = ExternalCapabilityDispatcher().apply {
            register(ExecutionEffect.READ_RESOURCE) { payload, _ ->
                assertEquals(InputValueType.NUMBER, InputRequest.decode(payload)?.type)
                "3"
            }
        }
        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Pada("निवेश"),
                Karaka.KARANA to ExecutionExpression.Pada("सङ्ख्या", value = SanskritValue.Shabda("changed")),
            ),
            externalDispatcher = dispatcher,
            sankhyaRenderer = SankhyaResultRenderer { it.toString() },
        )
        assertIs<SanskritValue.Sankhya>(assertIs<ExecutionResult.Success>(ReadAction.execute(context, ReadAction.op())).typedValue)
        val referenced = context.copy(
            bindings = context.bindings + (Karaka.KARANA to ExecutionExpression.Reference("सङ्ख्या")),
            variables = mapOf("सङ्ख्या" to SanskritValue.Shabda("changed again")),
        )
        assertIs<SanskritValue.Sankhya>(assertIs<ExecutionResult.Success>(ReadAction.execute(referenced, ReadAction.op())).typedValue)
    }

    @Test
    fun `conflicting declarations fail before asking the host for input`() {
        val dispatcher = ExternalCapabilityDispatcher().apply {
            register(ExecutionEffect.READ_RESOURCE) { _, _ -> error("Conflicting input must not reach host") }
        }
        for (otherType in listOf("सत्य", "विकल्प", "शब्द")) {
            val context = ExecutionContext(
                bindings = mapOf(
                    Karaka.KARMAN to ExecutionExpression.Pada("निवेश"),
                    Karaka.KARANA to ExecutionExpression.Pada("सङ्ख्या"),
                    Karaka.SAMPRADANA to ExecutionExpression.Pada(otherType),
                ),
                externalDispatcher = dispatcher,
            )
            assertIs<ExecutionResult.Failure>(ReadAction.execute(context, ReadAction.op()))
        }
    }

    @Test
    fun `numeric looking text and choices retain their declared semantic type`() {
        val dispatcher = ExternalCapabilityDispatcher().apply {
            register(ExecutionEffect.READ_RESOURCE) { _, _ -> "००७" }
        }
        val textContext = ExecutionContext(
            bindings = mapOf(Karaka.KARMAN to ExecutionExpression.Pada("नाम")),
            externalDispatcher = dispatcher,
            sankhyaRenderer = SankhyaResultRenderer { error("Text must not invoke number rendering") },
        )
        val text = assertIs<ExecutionResult.Success>(ReadAction.execute(textContext, ReadAction.op()))
        assertEquals(SanskritValue.Shabda("००७"), text.typedValue)
        val choiceContext = textContext.copy(bindings = textContext.bindings + (
            Karaka.SAMPRADANA to ExecutionExpression.Coordination(
                ExecutionExpression.Pada("विकल्प"), ExecutionExpression.Pada("००७"),
            )
        ))
        val choice = assertIs<ExecutionResult.Success>(ReadAction.execute(choiceContext, ReadAction.op()))
        assertEquals(SanskritValue.Shabda("००७"), choice.typedValue)
    }

    @Test
    fun `numeric range is enforced even when host skips validation`() {
        val dispatcher = ExternalCapabilityDispatcher().apply {
            register(ExecutionEffect.READ_RESOURCE) { _, _ -> "11" }
        }
        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Pada("निवेश"),
                Karaka.KARANA to ExecutionExpression.Pada("सङ्ख्या"),
            ),
            variables = mapOf(ACTIVE_RANGE_NAME to SanskritValue.Range(
                SanskritValue.Sankhya(1, "एक"), SanskritValue.Sankhya(10, "दश"),
            )),
            externalDispatcher = dispatcher,
            sankhyaRenderer = SankhyaResultRenderer { it.toString() },
        )
        assertIs<ExecutionResult.Failure>(ReadAction.execute(context, ReadAction.op()))
        dispatcher.register(ExecutionEffect.READ_RESOURCE) { _, _ -> "१०" }
        assertIs<ExecutionResult.Success>(ReadAction.execute(context, ReadAction.op()))
    }

    @Test
    fun `reversed explicit bounds produce a language failure instead of an exception`() {
        val context = ExecutionContext(bindings = mapOf(
            Karaka.KARMAN to ExecutionExpression.Pada("निवेश"),
            Karaka.KARANA to ExecutionExpression.Pada("सङ्ख्या"),
            Karaka.APADANA to ExecutionExpression.sankhya(10, "दश"),
            Karaka.ADHIKARANA to ExecutionExpression.sankhya(1, "एक"),
        ))
        assertIs<ExecutionResult.Failure>(ReadAction.execute(context, ReadAction.op()))
    }

    @Test
    fun `active numeric range does not add bounds to text input`() {
        val operation = ReadAction.op {
            requires(Karaka.KARMAN)
            optional(Karaka.APADANA, Karaka.ADHIKARANA)
        }
        val context = ExecutionContext(
            bindings = mapOf(Karaka.KARMAN to ExecutionExpression.Pada("निवेश")),
            variables = mapOf(
                ACTIVE_RANGE_NAME to SanskritValue.Range(
                    SanskritValue.Sankhya(1, "एक"),
                    SanskritValue.Sankhya(100, "शत"),
                ),
            ),
        )

        assertIs<ExecutionResult.Success>(ReadAction.execute(context, operation))
    }
}
