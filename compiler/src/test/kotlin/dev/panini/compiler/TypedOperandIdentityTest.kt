package dev.panini.compiler

import dev.panini.core.Karaka
import dev.panini.core.SupAffix
import dev.panini.execution.*
import dev.panini.execution.planning.ResolvedLeafPlanner
import kotlin.test.*

class TypedOperandIdentityTest {
    private fun generic(bindings: Map<Karaka, ExecutionExpression>, initial: Map<String, SanskritValue>,
        name: String, root: String = "गण", operation: String = "सूच्याकारः"): SanskritValue {
        val program = CompilerProgram(name, listOf(CompilerInstruction.Call(root, operation, "", bindings),
            CompilerInstruction.Store("LastResult")))
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader).loadFromBytes(name,
            CompilerProgramJvmEmitter.emit(program))
        return (generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>)["LastResult"] as SanskritValue
    }
    private fun parity(source: String, operand: ExecutionExpression, initial: Map<String, SanskritValue>, name: String): SanskritValue {
        val base = assertNotNull(ResolvedLeafPlanner.planAny(source))
        val bindings = base.resolved.context.bindings + (Karaka.KARMAN to operand)
        val resolved = base.resolved.copy(context = base.resolved.context.copy(bindings = bindings, variables = initial),
            invocation = base.resolved.invocation.copy(bindings = bindings))
        val expected = assertIs<ExecutionResult.Success>(resolved.operation.action.execute(resolved.context, resolved.operation))
        val instructions = CompilerIrLowering.lowerLeafValues(base.copy(resolved = resolved))
        val program = CompilerProgram(name, instructions)
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader).loadFromBytes(name,
            CompilerProgramJvmEmitter.emit(program))
        val value = (generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>)["LastResult"] as SanskritValue
        assertEquals(expected.value, value.toDisplayText())
        return value
    }

    @Test fun `typed numeric references read current bindings and retain missing-reference fallback`() {
        val one = SanskritValue.Sankhya(1L, "एक")
        val operand = ExecutionExpression.Coordination(
            ExecutionExpression.TypedOperand(one, SupAffix.AM, "slot"),
            ExecutionExpression.sankhya(1L, "एक"))
        val source = "एक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ।"
        assertEquals(10L, assertIs<SanskritValue.Sankhya>(parity(source, operand,
            mapOf("slot" to SanskritValue.Sankhya(9L, "नव")), "LiveTypedNumber")).value)
        assertEquals(2L, assertIs<SanskritValue.Sankhya>(parity(source, operand, emptyMap(), "FallbackTypedNumber")).value)
    }

    @Test fun `typed text literal does not become a collection reference from its spelling`() {
        val operand = ExecutionExpression.TypedOperand(SanskritValue.Shabda("slot"), SupAffix.AM)
        val collection = SanskritValue.Suchi(List(3) { SanskritValue.Sankhya(1L, "एक") })
        assertEquals(1L, assertIs<SanskritValue.Sankhya>(parity("सूची + अम् गण + णिच् + लोट् + सिप् ।",
            operand, mapOf("slot" to collection), "LiteralTypedCollectionName")).value)
    }

    @Test fun `typed collection reference counts the current collection rather than its captured value`() {
        val one = SanskritValue.Sankhya(1L, "एक")
        val operand = ExecutionExpression.TypedOperand(SanskritValue.Suchi(listOf(one)), SupAffix.AM, "slot")
        assertEquals(3L, assertIs<SanskritValue.Sankhya>(parity("सूची + अम् गण + णिच् + लोट् + सिप् ।",
            operand, mapOf("slot" to SanskritValue.Suchi(List(3) { one })), "LiveTypedCollection")).value)
    }

    @Test fun `typed display and assignment use live references rather than captured spellings`() {
        val captured = SanskritValue.Shabda("captured")
        val live = SanskritValue.Shabda("current")
        val operand = ExecutionExpression.TypedOperand(captured, SupAffix.AM, "slot")
        for ((index, source) in listOf("राम + अम् मुद्र् + णिच् + लोट् + सिप् ।",
            "राम + अम् मान + ङे दा + लोट् + सिप् ।").withIndex()) {
            assertEquals("current", parity(source, operand, mapOf("slot" to live), "LiveTypedValue$index").toDisplayText())
        }
        val participantInstructions = CompilerIrLowering.lowerParticipantOperands(operand).single()
        assertEquals(listOf(CompilerInstruction.ResolveArgument("slot", captured)), participantInstructions)
    }

    @Test fun `generic emitted calls retain typed referents and literal fallback values`() {
        val one = SanskritValue.Sankhya(1L, "एक")
        val old = SanskritValue.Suchi(listOf(one))
        val current = SanskritValue.Suchi(List(3) { one })
        val operand = ExecutionExpression.TypedOperand(old, SupAffix.AM, "slot")
        assertEquals(3L, assertIs<SanskritValue.Sankhya>(generic(mapOf(Karaka.KARMAN to operand),
            mapOf("slot" to current), "GenericLiveTypedOperand")).value)
        assertEquals(1L, assertIs<SanskritValue.Sankhya>(generic(mapOf(Karaka.KARMAN to operand),
            emptyMap(), "GenericFallbackTypedOperand")).value)
        val literal = ExecutionExpression.TypedOperand(SanskritValue.Shabda("slot"), SupAffix.AM)
        assertEquals(1L, assertIs<SanskritValue.Sankhya>(generic(mapOf(Karaka.KARMAN to literal),
            mapOf("slot" to current), "GenericLiteralTypedOperand")).value)
    }

    @Test fun `generic emitted calls preserve member-selection semantics`() {
        val one = SanskritValue.Sankhya(1L, "एक")
        val bindings = mapOf(Karaka.KARMAN to ExecutionExpression.Pada("सङ्ख्या",
            setOf(dev.panini.shiksha.Samjna.SANKHYA), memberSelection = CollectionMemberSelection.NUMBERS),
            Karaka.SAMBANDHA to ExecutionExpression.Reference("slot"))
        assertEquals(3L, assertIs<SanskritValue.Sankhya>(generic(bindings,
            mapOf("slot" to SanskritValue.Suchi(List(3) { one })), "GenericNumberMemberSum",
            root = "युजिँर्", operation = "सङ्ख्यायोजनम्")).value)
        val decoded = PaniniRuntime.createPadaExpression("मान", one, arrayOf("RUDHI:विशेष"), "ORDINAL")
        assertEquals(setOf(dev.panini.shiksha.Samjna.Rudhi("विशेष")), decoded.samjnas)
        assertEquals(CollectionMemberSelection.ORDINAL, decoded.memberSelection)
    }

    @Test fun `ordinary pada literals cannot be replaced by variables with the same spelling`() {
        val collection = SanskritValue.Suchi(List(3) { SanskritValue.Sankhya(1L, "एक") })
        for ((index, literal) in listOf(ExecutionExpression.Pada("slot", value = SanskritValue.Shabda("slot")),
            ExecutionExpression.Pada("slot")).withIndex()) {
            assertEquals(1L, assertIs<SanskritValue.Sankhya>(generic(mapOf(Karaka.KARMAN to literal),
                mapOf("slot" to collection), "IsolatedPadaLiteral$index")).value)
            val expected = literal.value ?: SanskritValue.of(literal.prakriti, literal.samjnas)
            assertEquals(listOf(CompilerInstruction.Constant(expected)),
                CompilerIrLowering.lowerParticipantOperands(literal).single())
        }
    }
}
