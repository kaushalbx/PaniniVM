package dev.panini.execution

import dev.panini.vyakaranam.ast.*
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.*

class ParsedAnaphorExecutionTest {
    @Test fun `explicit AST scope stops after failure suspension or control signal`() {
        val parser = PaniniParser()
        for (first in listOf("तद् + शस् मुद्र् + णिच् + लोट् + सिप् ।",
            "वार्ता + अम् प्रेष् + णिच् + लोट् + सिप् ।", "वि + स्था + लोट् + सिप् ।")) {
            val declaration = (parser.parse("मान + सुँ ।").body as Invocation).vakya.padas.single() as SubantaPada
            val locals = ProcedureLocalBindings.member(null, "मान", SanskritValue.Sankhya(9L, "नवन्"), declaration)
            val vm = PaniniVM()
            val scope = vm.defaultScope.copy(environment = ValueEnvironment.live(locals.values), procedureBindings = locals)
            val parsed = parser.parse(first)
            val program = Scope(first, "क्षेत्र", body = listOf(parsed.body,
                parser.parse("द्वि + अम् मान + ङे दा + लोट् + सिप् ।").body))
            val result = vm.evalParsed(parsed.copy(body = program), scope = scope)
            assertTrue(result !is ExecutionResult.Success || result.controlSignal != null, result.toString())
            assertEquals(9L, (locals.values.getValue("मान") as SanskritValue.Sankhya).value, first)
        }
    }

    @Test fun `direct loop AST reevaluates the live pronoun cell`() {
        val parser = PaniniParser()
        val declaration = (parser.parse("मान + सुँ ।").body as Invocation).vakya.padas.single() as SubantaPada
        val locals = ProcedureLocalBindings.member(null, "मान", SanskritValue.Sankhya(9L, "नवन्"), declaration)
        val scope = ExecutionScope(environment = ValueEnvironment.live(locals.values), procedureBindings = locals)
        val source = "यावत् तद् + सुँ नवन् + टा सम + सुँ असँ + लट् + तिप् तावत् द्वि + अम् तद् + ङे दा + लोट् + सिप् ।"
        val result = assertIs<ExecutionResult.Success>(PaniniVM(
            executionLimits = ExecutionLimits(maxConditionIterations = 2L)).evalParsed(parser.parse(source), scope = scope))
        assertEquals(1L, result.iterationCount)
        assertEquals(2L, (locals.values.getValue("मान") as SanskritValue.Sankhya).value)
    }

    @Test fun `direct foreach AST resolves the whole before introducing its member`() {
        val parser = PaniniParser()
        val declaration = (parser.parse("मान + सुँ ।").body as Invocation).vakya.padas.single() as SubantaPada
        val list = SanskritValue.Suchi(listOf(SanskritValue.Sankhya(1L, "एक"), SanskritValue.Sankhya(2L, "द्वि")))
        val locals = ProcedureLocalBindings.member(null, "मान", list, declaration)
        val scope = ExecutionScope(environment = ValueEnvironment.live(locals.values), procedureBindings = locals)
        val source = "तद् + ङस् मूल्य + अम् प्रति मूल्य + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val result = PaniniVM().evalParsed(parser.parse(source), scope = scope)
        assertEquals("द्वि", assertIs<ExecutionResult.Success>(result).value)
        assertSame(list, locals.values["मान"])
        assertNull(locals.identity("मूल्य"))
    }

    @Test fun `direct conditional AST resolves only its selected branch`() {
        val parsed = PaniniParser().parse("यदि एक + सुँ एक + टा सम + सुँ असँ + लट् + तिप् तर्हि नवन् + शस् मुद्र् + णिच् + लोट् + सिप् अन्यथा तद् + अम् मुद्र् + णिच् + लोट् + सिप् ।")
        val result = PaniniVM().evalParsed(parsed)
        assertEquals("नवन्", assertIs<ExecutionResult.Success>(result).value)
    }

    @Test fun `direct conditional AST assignment retains the local antecedent`() {
        val parser = PaniniParser()
        val declaration = (parser.parse("मान + सुँ ।").body as Invocation).vakya.padas.single() as SubantaPada
        val locals = ProcedureLocalBindings.member(null, "मान", SanskritValue.Sankhya(9L, "नवन्"), declaration)
        val scope = ExecutionScope(environment = ValueEnvironment.live(locals.values), procedureBindings = locals)
        val source = "यदि तद् + सुँ नवन् + टा सम + सुँ असँ + लट् + तिप् तर्हि द्वि + अम् तद् + ङे दा + लोट् + सिप् अन्यथा एक + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        assertIs<ExecutionResult.Success>(PaniniVM().evalParsed(parser.parse(source), scope = scope))
        assertEquals(2L, (locals.values.getValue("मान") as SanskritValue.Sankhya).value)
    }

    @Test fun `resumed pronoun assignment retains its original lexical cell`() {
        val parser = PaniniParser()
        val declaration = (parser.parse("मान + सुँ ।").body as Invocation).vakya.padas.single() as SubantaPada
        val locals = ProcedureLocalBindings.member(null, "मान", SanskritValue.Sankhya(9L, "नवन्"), declaration)
        val vm = PaniniVM()
        val scope = vm.defaultScope.copy(environment = ValueEnvironment.live(locals.values), procedureBindings = locals)
        val paused = assertIs<ExecutionResult.NeedsApproval>(vm.evalParsed(parser.parse(
            "द्वि + अम् तद् + ङे दा + लोट् + सिप् ततः वार्ता + अम् प्रेष् + णिच् + लोट् + सिप् ।"), scope = scope))
        val pausedAgain = assertIs<ExecutionResult.NeedsApproval>(vm.resume(paused.continuation, scope = scope))
        assertIs<ExecutionResult.Success>(vm.resume(pausedAgain.continuation,
            scope = scope.copy(capabilities = scope.capabilities + paused.requiredEffects)))
        assertEquals(2L, (locals.values.getValue("मान") as SanskritValue.Sankhya).value)
    }

    @Test fun `mixed named and pronoun references observe same turn assignments`() {
        for ((target, objectName) in listOf("मान" to "तद्", "तद्" to "मान")) {
            val parser = PaniniParser()
            val declaration = (parser.parse("मान + सुँ ।").body as Invocation).vakya.padas.single() as SubantaPada
            val locals = ProcedureLocalBindings.member(null, "मान", SanskritValue.Sankhya(9L, "नवन्"), declaration)
            val scope = ExecutionScope(environment = ValueEnvironment.live(locals.values), procedureBindings = locals)
            val source = "द्वि + अम् $target + ङे दा + लोट् + सिप् ततः $objectName + अम् मुद्र् + णिच् + लोट् + सिप् ।"
            val result = PaniniVM().evalParsed(parser.parse(source), scope = scope)
            assertEquals("द्वि", assertIs<ExecutionResult.Success>(result).value, source)
            assertEquals(2L, (locals.values.getValue("मान") as SanskritValue.Sankhya).value)
        }
    }

    @Test fun `direct sequence reads its pronoun assignment before publication`() {
        val parser = PaniniParser()
        val declaration = (parser.parse("मान + सुँ ।").body as Invocation).vakya.padas.single() as SubantaPada
        val locals = ProcedureLocalBindings.member(null, "मान", SanskritValue.Sankhya(9L, "नवन्"), declaration)
        val scope = ExecutionScope(environment = ValueEnvironment.live(locals.values), procedureBindings = locals)
        val source = "द्वि + अम् तद् + ङे दा + लोट् + सिप् ततः तद् + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val result = PaniniVM().evalParsed(parser.parse(source), scope = scope)
        assertEquals("द्वि", assertIs<ExecutionResult.Success>(result).value)
        assertEquals(2L, (locals.values.getValue("मान") as SanskritValue.Sankhya).value)
        assertFalse(locals.names.any { it.startsWith("__pvm_anaphor_") })
    }

    @Test fun `raw utterance uses the same local pronoun binding as parsed execution`() {
        val declaration = (PaniniParser().parse("मान + सुँ ।").body as Invocation)
            .vakya.padas.single() as SubantaPada
        val locals = ProcedureLocalBindings.member(null, "मान", SanskritValue.Sankhya(9L, "नवन्"), declaration)
        val scope = ExecutionScope(environment = ValueEnvironment.live(locals.values), procedureBindings = locals)
        val vm = PaniniVM()
        assertIs<ExecutionResult.Success>(vm.eval("द्वि + अम् तद् + ङे दा + लोट् + सिप् ।", scope = scope))
        assertEquals(2L, (locals.values.getValue("मान") as SanskritValue.Sankhya).value)
        assertEquals("द्वि", assertIs<ExecutionResult.Success>(
            vm.eval("तद् + अम् मुद्र् + णिच् + लोट् + सिप् ।", scope = scope)).value)
        assertEquals(ExecutionError.INVALID_VALUE, assertIs<ExecutionResult.Failure>(
            vm.eval("तद् + अम् मुद्र् + णिच् + लोट् + सिप् ।")).error)
        assertEquals(ExecutionError.INVALID_VALUE, assertIs<ExecutionResult.Failure>(vm.eval(" ")).error)
    }

    @Test fun `AST entry point binds pronouns and publishes assignments to their live cell`() {
        val parser = PaniniParser()
        val declaration = (parser.parse("मान + सुँ ।").body as Invocation).vakya.padas.single() as SubantaPada
        val locals = ProcedureLocalBindings.member(null, "मान", SanskritValue.Sankhya(9L, "नवन्"), declaration)
        val scope = ExecutionScope(environment = ValueEnvironment.live(locals.values), procedureBindings = locals)
        val vm = PaniniVM()
        val assigned = vm.evalParsed(parser.parse("द्वि + अम् तद् + ङे दा + लोट् + सिप् ।"), scope = scope)
        assertIs<ExecutionResult.Success>(assigned)
        assertEquals(2L, (locals.values.getValue("मान") as SanskritValue.Sankhya).value)
        val printed = vm.evalParsed(parser.parse("तद् + अम् मुद्र् + णिच् + लोट् + सिप् ।"), scope = scope)
        assertEquals("द्वि", assertIs<ExecutionResult.Success>(printed).value)
        assertFalse(locals.names.any { it.startsWith("__pvm_anaphor_") })
    }

    @Test fun `AST entry point rejects absent antecedents`() {
        val result = PaniniVM().evalParsed(PaniniParser().parse("तद् + अम् मुद्र् + णिच् + लोट् + सिप् ।"))
        val failure = assertIs<ExecutionResult.Failure>(result)
        assertEquals(ExecutionError.INVALID_VALUE, failure.error)
        assertTrue(failure.message.startsWith("MISSING_ANTECEDENT:"), failure.message)
        assertContains(failure.message, "at offsets 0..9")
        assertContains(failure.message, "[तद्+अम्]")
        val diagnostic = assertNotNull(failure.diagnostic)
        assertEquals("MISSING_ANTECEDENT", diagnostic.code)
        assertEquals(DocumentSourceSpan(0, 9), diagnostic.span)
        assertEquals("तद्+अम्", diagnostic.offendingText)
    }
}
