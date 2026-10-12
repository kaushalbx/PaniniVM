package dev.panini.compiler

import dev.panini.core.Karaka
import dev.panini.execution.SanskritValue
import java.lang.reflect.InvocationTargetException
import kotlin.test.*

class ParticipantHistoryFailureTest {
    @Test fun `failed emitted history arithmetic preserves prior results and action frames`() {
        val source = "दा + ल्युट् + ङस् कर्मन् + अम् एक + अम् च मान + ङे हृ + लोट् + सिप् ।"
        val lowered = CompilerFrontend.lower(source, "FailedHistoryArithmetic")
        val program = CompilerProgram(lowered.className, emptyList(), listOf(
            CompilerProcedure("attempt", lowered.entryPoint + CompilerInstruction.Return)))
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader).loadFromBytes(
            program.className, CompilerProgramJvmEmitter.emit(program))
        val prior = SanskritValue.Sankhya(9L, "नव")
        for (invalid in listOf<SanskritValue>(SanskritValue.Sankhya(0L, "शून्य"),
            SanskritValue.Shabda("राम"), SanskritValue.Suchi(emptyList()))) {
            val runtime = CompiledProgramRuntime(mapOf("मान" to prior, "LastResult" to prior))
            val inputs = listOf(SanskritValue.Sankhya(1L, "एक"), invalid)
            runtime.recordActionFrameValues("डुदाञ्", prior, arrayOf(Karaka.KARMAN, Karaka.KARMAN),
                inputs.toTypedArray())
            assertIs<CompiledPaniniExecutionException>(assertFailsWith<InvocationTargetException> {
                generated.getMethod("attempt", CompiledProgramRuntime::class.java).invoke(null, runtime)
            }.cause)
            assertEquals(mapOf("मान" to prior, "LastResult" to prior), runtime.snapshot())
            assertEquals(SanskritValue.Gana(inputs), runtime.loadActionParticipantGroup("डुदाञ्", 1, Karaka.KARMAN))
            assertFailsWith<CompiledPaniniExecutionException> { runtime.loadActionResult("हृञ्", 1) }
        }
    }
}
