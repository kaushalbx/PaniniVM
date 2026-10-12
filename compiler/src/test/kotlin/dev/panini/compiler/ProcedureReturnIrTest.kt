package dev.panini.compiler

import dev.panini.execution.SanskritValue
import java.lang.reflect.InvocationTargetException
import kotlin.test.*

class ProcedureReturnIrTest {
    private fun execute(value: SanskritValue): Map<*, *> {
        val program = CompilerProgram("OwnedProcedureReturn", listOf(
            CompilerInstruction.EnterFrame(emptyList()),
            CompilerInstruction.InvokeProcedure("outer", 0, CompilerValueKind.NUMBER),
            CompilerInstruction.ExitFrame,
        ), listOf(
            CompilerProcedure("inner", listOf(
                // A lexical binding frame must not become the return owner.
                CompilerInstruction.EnterFrame(emptyList(), scopedBinding = true),
                CompilerInstruction.Constant(value),
                CompilerInstruction.RequestProcedureReturn,
                CompilerInstruction.ConsumeBreak,
                CompilerInstruction.Branch("not_loop_break"),
                CompilerInstruction.Constant(SanskritValue.Sankhya(99L, "99")),
                CompilerInstruction.RequestProcedureReturn,
                CompilerInstruction.Label("not_loop_break"),
                CompilerInstruction.ExitFrame,
                CompilerInstruction.IsProcedureReturnRequested,
                CompilerInstruction.Branch("contract_exit", whenTrue = true),
                CompilerInstruction.Constant(SanskritValue.Sankhya(99L, "99")),
                CompilerInstruction.Store("LastResult"),
                CompilerInstruction.Label("contract_exit"),
                CompilerInstruction.Return,
            ), returnKind = CompilerValueKind.NUMBER),
            CompilerProcedure("outer", listOf(
                CompilerInstruction.EnterFrame(emptyList()),
                CompilerInstruction.InvokeProcedure("inner", 0, CompilerValueKind.NUMBER),
                CompilerInstruction.ExitFrame,
                CompilerInstruction.Load("LastResult"),
                CompilerInstruction.Store("inner_result"),
                CompilerInstruction.IsProcedureExitRequested,
                CompilerInstruction.Branch("outer_exit", whenTrue = true),
                CompilerInstruction.Constant(SanskritValue.Sankhya(2L, "द्वे")),
                CompilerInstruction.Store("LastResult"),
                CompilerInstruction.Label("outer_exit"),
                CompilerInstruction.Return,
            ), returnKind = CompilerValueKind.NUMBER),
        ))
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes(program.className, CompilerProgramJvmEmitter.emit(program))
        return generated.getMethod("execute").invoke(null) as Map<*, *>
    }

    @Test fun `emitted return crosses binding scope but not procedure boundary`() {
        val value = SanskritValue.Sankhya(1L, "एक")
        val results = execute(value)
        assertEquals(value, results["inner_result"])
        assertEquals(2L, assertIs<SanskritValue.Sankhya>(results["LastResult"]).value)
    }

    @Test fun `emitted return still enforces numeric contract`() {
        assertIs<CompiledPaniniExecutionException>(assertFailsWith<InvocationTargetException> {
            execute(SanskritValue.Shabda("invalid numeric return"))
        }.cause)
    }

    @Test fun `return value stack underflow is rejected before emission`() {
        assertFailsWith<IllegalArgumentException> {
            CompilerIrVerifier.verify(listOf(CompilerInstruction.RequestProcedureReturn))
        }
    }

    @Test fun `verifier tracks returned type rather than preceding result type`() {
        fun instructions(previous: SanskritValue, returned: SanskritValue) = listOf(
            CompilerInstruction.Constant(previous), CompilerInstruction.Store("LastResult"),
            CompilerInstruction.Constant(returned), CompilerInstruction.RequestProcedureReturn,
            CompilerInstruction.Load("LastResult"),
            CompilerInstruction.Constant(SanskritValue.Sankhya(1L, "एक")),
            CompilerInstruction.Arithmetic(ArithmeticOperator.ADD),
            CompilerInstruction.Store("checked"),
        )
        val number = SanskritValue.Sankhya(1L, "एक")
        val text = SanskritValue.Shabda("text")
        CompilerIrVerifier.verify(instructions(text, number))
        assertFailsWith<IllegalArgumentException> { CompilerIrVerifier.verify(instructions(number, text)) }
    }
}
