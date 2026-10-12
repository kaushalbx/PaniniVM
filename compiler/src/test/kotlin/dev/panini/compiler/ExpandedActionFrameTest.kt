package dev.panini.compiler

import dev.panini.core.Karaka
import dev.panini.execution.SanskritValue
import kotlin.test.*

class ExpandedActionFrameTest {
    private fun number(value: Long) = SanskritValue.Sankhya(value, value.toString())

    @Test fun `emitted recording expands one group layer and preserves actual collections in order`() {
        val list = SanskritValue.Suchi(listOf(number(2)))
        val gana = SanskritValue.Gana(listOf(number(3)))
        val group = SanskritValue.Gana(listOf(number(1), list, gana))
        val expected = SanskritValue.Gana(listOf(number(1), list, gana, gana))
        val program = CompilerProgram("ExpandedParticipantHistory", listOf(
            CompilerInstruction.Constant(group),
            CompilerInstruction.Constant(gana),
            CompilerInstruction.Constant(number(7)),
            CompilerInstruction.RecordActionFrame("डुदाञ्", listOf(Karaka.KARMAN, Karaka.KARMAN),
                expandGroups = listOf(true, false)),
            CompilerInstruction.LoadActionParticipants("डुदाञ्", Karaka.KARMAN, asGroup = true),
            CompilerInstruction.Store("latest"),
            CompilerInstruction.LoadOrdinalActionParticipants("डुदाञ्", Karaka.KARMAN, 1L, asGroup = true),
            CompilerInstruction.Store("first"),
        ), emptyList())
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes(program.className, CompilerProgramJvmEmitter.emit(program))
        val results = generated.getMethod("execute").invoke(null) as Map<*, *>
        assertEquals(expected, results["latest"])
        assertEquals(expected, results["first"])
    }

    @Test fun `expanded recording snapshots participants and rejects invalid wrappers atomically`() {
        val runtime = CompiledProgramRuntime()
        val members = mutableListOf<SanskritValue>(number(1))
        runtime.recordExpandedActionFrameValues("डुदाञ्", number(7), arrayOf(Karaka.KARMAN),
            arrayOf(SanskritValue.Gana(members)), "1")
        members[0] = number(9)
        assertEquals(SanskritValue.Gana(listOf(number(1))),
            runtime.loadActionParticipantGroup("डुदाञ्", 1, Karaka.KARMAN))
        assertFailsWith<CompiledPaniniExecutionException> {
            runtime.recordExpandedActionFrameValues("डुदाञ्", number(9), arrayOf(Karaka.KARMAN),
                arrayOf(SanskritValue.Suchi(listOf(number(2)))), "1")
        }
        assertEquals(number(7), runtime.loadActionResult("डुदाञ्", 1))
        assertFailsWith<CompiledPaniniExecutionException> { runtime.loadActionResult("डुदाञ्", 2) }
    }

    @Test fun `expansion flags must align and recording requires its full input stack`() {
        assertFailsWith<IllegalArgumentException> {
            CompilerInstruction.RecordActionFrame("डुदाञ्", listOf(Karaka.KARMAN), listOf(true, false))
        }
        assertFailsWith<IllegalArgumentException> {
            CompilerIrVerifier.verify(listOf(CompilerInstruction.RecordActionFrame("डुदाञ्",
                listOf(Karaka.KARMAN), listOf(true))))
        }
        for (flags in listOf("", "x", "11")) assertFailsWith<IllegalArgumentException> {
            CompiledProgramRuntime().recordExpandedActionFrameValues("डुदाञ्", number(7),
                arrayOf(Karaka.KARMAN), arrayOf(SanskritValue.Gana(listOf(number(1)))), flags)
        }
    }
}
