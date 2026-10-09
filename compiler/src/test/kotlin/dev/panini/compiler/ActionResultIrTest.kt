package dev.panini.compiler

import dev.panini.execution.SanskritValue
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes.*
import java.lang.reflect.InvocationTargetException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ActionResultIrTest {
    @Test
    fun `parsed karaka history relation lowers to typed participant selection`() {
        val source = CompilerFrontend.lower(
            "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।\n" +
                "द्वि + औट् त्रि + शस् च युज् + णिच् + लोट् + सिप् ।", "HistoryRelationSource")
        for ((qualifier, expected) in listOf("प्रथम + अम्" to listOf(1L, 2L),
            "पूर्व + अम्" to listOf(1L, 2L), "द्वि + तीय + अम्" to listOf(2L, 3L), "" to listOf(2L, 3L))) {
            val invocation = dev.panini.vyakaranam.parser.PaniniParser().parse(
                "युज् + ल्युट् + ङस् $qualifier कर्मन् + अम् मुद्र् + णिच् + लोट् + सिप् ।",
            ).body as dev.panini.vyakaranam.ast.Invocation
            val relation = dev.panini.execution.binding.KarakaReferenceResolver.references(invocation.vakya.padas).single()
            val runtime = runFixture(source.entryPoint + listOf(
                CompilerIrLowering.lowerKarakaHistory(relation), CompilerInstruction.Store("selected")))
            val participants = (runtime.loadValue("selected") as SanskritValue.Gana).elements
            assertEquals(expected, participants.map { (it as SanskritValue.Sankhya).value })
        }
    }
    @Test
    fun `source action frame derives agent and object roles from grammatical bindings`() {
        val program = CompilerFrontend.lower(
            "राम + सुँ एक + अम् द्वि + औट् च युज् + णिच् + लोट् + तिप् ।", "SourceRoleHistory")
        val record = program.entryPoint.filterIsInstance<CompilerInstruction.RecordActionFrame>().single()
        val runtime = runFixture(program.entryPoint)
        assertEquals("राम", runtime.loadOrdinalActionParticipants(record.dhatuUpadesha, 1, dev.panini.core.Karaka.KARTR)
            .single().toDisplayText())
        assertEquals(listOf(1L, 2L), runtime.loadOrdinalActionParticipants(record.dhatuUpadesha, 1, dev.panini.core.Karaka.KARMAN)
            .map { (it as SanskritValue.Sankhya).value })
    }
    @Test
    fun `source lowered arithmetic records its actual object operands`() {
        val program = CompilerFrontend.lower(
            "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।\n" +
                "नवन् + शस् मुद्र् + णिच् + लोट् + सिप् ।", "SourceParticipantHistory")
        val runtime = runFixture(program.entryPoint)
        val key = program.entryPoint.filterIsInstance<CompilerInstruction.RecordActionFrame>().first().dhatuUpadesha
        assertEquals(listOf(1L, 2L), runtime.loadOrdinalActionParticipants(key, 1, dev.panini.core.Karaka.KARMAN)
            .map { (it as SanskritValue.Sankhya).value })
        assertEquals(3L, (runtime.loadOrdinalActionResult(key, 1) as SanskritValue.Sankhya).value)
    }
    @Test
    fun `generated frame recording preserves participant order and result atomically`() {
        val one = SanskritValue.Sankhya(1, "एक")
        val two = SanskritValue.Sankhya(2, "द्वि")
        val three = SanskritValue.Sankhya(3, "त्रि")
        val record = CompilerInstruction.RecordActionFrame("युज्",
            listOf(dev.panini.core.Karaka.KARMAN, dev.panini.core.Karaka.KARMAN))
        val runtime = runFixture(listOf(
            CompilerInstruction.Constant(one), CompilerInstruction.Constant(two),
            CompilerInstruction.Constant(three), record,
            CompilerInstruction.LoadOrdinalActionParticipants("युज्", dev.panini.core.Karaka.KARMAN, 1),
            CompilerInstruction.Store("objects"),
            CompilerInstruction.LoadOrdinalActionResult("युज्", 1), CompilerInstruction.Store("result"),
        ))
        assertEquals(SanskritValue.Gana(listOf(one, two)), runtime.loadValue("objects"))
        assertEquals(three, runtime.loadValue("result"))
        assertFailsWith<IllegalArgumentException> {
            CompilerIrVerifier.verify(listOf(CompilerInstruction.Constant(three), record))
        }
        assertFailsWith<IllegalArgumentException> {
            CompilerInstruction.RecordActionFrame("युज्", listOf(dev.panini.core.Karaka.ANIRDHARITA))
        }
    }
    @Test
    fun `generated participant loads retain typed coordination and chronology`() {
        val runtime = CompiledProgramRuntime()
        val members = listOf(SanskritValue.Sankhya(1, "एक"), SanskritValue.Sankhya(2, "द्वि"))
        runtime.recordActionFrame("युज्", SanskritValue.Sankhya(3, "त्रि"),
            mapOf(dev.panini.core.Karaka.KARMAN to members))
        runtime.recordActionResult("युज्", SanskritValue.Sankhya(5, "पञ्च"))
        runFixture(listOf(
            CompilerInstruction.LoadOrdinalActionParticipants("युज्", dev.panini.core.Karaka.KARMAN, 1),
            CompilerInstruction.Store("first"),
            CompilerInstruction.LoadActionParticipants("युज्", dev.panini.core.Karaka.KARMAN, 2),
            CompilerInstruction.Store("previous"),
        ), runtime)
        assertEquals(SanskritValue.Gana(members), runtime.loadValue("first"))
        assertEquals(runtime.loadValue("first"), runtime.loadValue("previous"))
        val failure = assertFailsWith<InvocationTargetException> {
            runFixture(listOf(CompilerInstruction.LoadOrdinalActionParticipants("युज्", dev.panini.core.Karaka.KARMAN, Long.MAX_VALUE),
                CompilerInstruction.Pop), runtime)
        }
        assertEquals(dev.panini.execution.ExecutionError.INVALID_VALUE, (failure.cause as CompiledPaniniExecutionException).error)
        assertFailsWith<IllegalArgumentException> {
            CompilerInstruction.LoadActionParticipants("युज्", dev.panini.core.Karaka.ANIRDHARITA)
        }
        assertFailsWith<IllegalArgumentException> {
            CompilerInstruction.LoadOrdinalActionParticipants("युज्", dev.panini.core.Karaka.KARMAN, 0)
        }
    }
    @Test
    fun `generated wide ordinal cannot alias the first recorded result`() {
        val runtime = CompiledProgramRuntime()
        runtime.recordActionResult("चिञ्", SanskritValue.Sankhya(3, "त्रीणि"))
        val failure = assertFailsWith<InvocationTargetException> {
            runFixture(listOf(CompilerInstruction.LoadOrdinalActionResult("चिञ्", 4_294_967_297L),
                CompilerInstruction.Store("selected")), runtime)
        }
        assertEquals(dev.panini.execution.ExecutionError.INVALID_VALUE,
            (failure.cause as CompiledPaniniExecutionException).error)
    }
    @Test
    fun `failed source action does not publish a completed result`() {
        val program = CompilerFrontend.lower(
            "एक + अम् द्वि + औट् च वि + युज् + णिच् + लोट् + सिप् ।",
            "FailedRecordedSource",
        )
        val runtime = CompiledProgramRuntime()
        val failure = assertFailsWith<InvocationTargetException> { runFixture(program.entryPoint, runtime) }
        assertEquals(CompiledPaniniExecutionException::class, failure.cause!!::class)
        val key = program.entryPoint.filterIsInstance<CompilerInstruction.RecordActionFrame>().single().dhatuUpadesha
        assertFailsWith<CompiledPaniniExecutionException> { runtime.loadActionResult(key, 1) }
    }

    @Test
    fun `source lowered choices retain numeric history after later prints`() {
        val program = CompilerFrontend.lower(
            "द्वि + ङसिँ द्वि + औट् परि + अन्त + अम् सङ्ख्या + अम् चिञ् + क्त्वा " +
                "मुद्र् + णिच् + लोट् + सिप् ।\nनवन् + शस् मुद्र् + णिच् + लोट् + सिप् ।",
            "RecordedSourceChoice",
        )
        val runtime = runFixture(program.entryPoint)
        assertEquals(2L, (runtime.loadActionResult("चिञ्", 1) as SanskritValue.Sankhya).value)
        assertEquals(SanskritValue.Shabda("नवन्"), runtime.loadActionResult("मुद्रँ", 1))
        assertEquals(SanskritValue.Shabda("नवन्"), runtime.loadValue("LastResult"))
    }

    @Test
    fun `skipped branches do not create action history`() {
        val runtime = runFixture(listOf(
            CompilerInstruction.Constant(SanskritValue.Satya(false)),
            CompilerInstruction.Booleanize,
            CompilerInstruction.Branch("skip"),
            CompilerInstruction.Constant(SanskritValue.Sankhya(1, "एक")),
            CompilerInstruction.RecordActionResult("चिञ्"),
            CompilerInstruction.Label("skip"),
        ))
        assertFailsWith<CompiledPaniniExecutionException> { runtime.loadActionResult("चिञ्", 1) }
    }

    @Test
    fun `history instructions verify their operands and identities`() {
        assertFailsWith<IllegalArgumentException> {
            CompilerIrVerifier.verify(listOf(CompilerInstruction.RecordActionResult("चिञ्")))
        }
        assertFailsWith<IllegalArgumentException> { CompilerInstruction.RecordActionResult("") }
        assertFailsWith<IllegalArgumentException> { CompilerInstruction.LoadActionResult("चिञ्", 0) }
        assertFailsWith<IllegalArgumentException> { CompilerInstruction.LoadOrdinalActionResult("चिञ्", 0) }
        assertFailsWith<IllegalArgumentException> { CompilerInstruction.LoadOrdinalActionResult("", 1) }
        CompilerIrVerifier.verify(listOf(CompilerInstruction.LoadOrdinalActionResult("चिञ्", 1), CompilerInstruction.Pop))
        CompilerIrVerifier.verify(listOf(CompilerInstruction.LoadActionResult("चिञ्"), CompilerInstruction.Pop))
    }

    @Test
    fun `JVM ordinal load retrieves chronological history`() {
        val first = SanskritValue.Sankhya(3, "त्रीणि")
        val runtime = runFixture(listOf(
            CompilerInstruction.Constant(first), CompilerInstruction.RecordActionResult("चिञ्"),
            CompilerInstruction.Constant(SanskritValue.Sankhya(5, "पञ्च")), CompilerInstruction.RecordActionResult("चिञ्"),
            CompilerInstruction.LoadOrdinalActionResult("चिञ्", 1), CompilerInstruction.Store("first"),
        ))
        assertEquals(first, runtime.loadValue("first"))
    }

    @Test
    fun `JVM emission records and reloads typed history independently of LastResult`() {
        val chosen = SanskritValue.Sankhya(3, "त्रीणि")
        val instructions = listOf(
            CompilerInstruction.Constant(chosen), CompilerInstruction.RecordActionResult("चिञ्"),
            CompilerInstruction.Constant(SanskritValue.Shabda("printed")), CompilerInstruction.Store("LastResult"),
            CompilerInstruction.LoadActionResult("चिञ्"), CompilerInstruction.Store("chosen"),
        )
        val runtime = runFixture(instructions)
        assertEquals(chosen, runtime.loadValue("chosen"))
        assertEquals(SanskritValue.Shabda("printed"), runtime.loadValue("LastResult"))
    }

    private fun runFixture(instructions: List<CompilerInstruction>, runtime: CompiledProgramRuntime = CompiledProgramRuntime()): CompiledProgramRuntime {
        val writer = ClassWriter(ClassWriter.COMPUTE_FRAMES or ClassWriter.COMPUTE_MAXS)
        writer.visit(V17, ACC_PUBLIC, "CompiledActionHistoryFixture", null, "java/lang/Object", null)
        val method = writer.visitMethod(ACC_PUBLIC or ACC_STATIC, "run",
            "(Ldev/panini/compiler/CompiledProgramRuntime;)V", null, null)
        method.visitCode()
        var local = 1
        CompilerIrJvmEmitter("CompiledActionHistoryFixture", method) { width -> local.also { local += width } }
            .emit(instructions)
        method.visitInsn(RETURN)
        method.visitMaxs(0, 0)
        method.visitEnd()
        writer.visitEnd()
        val loader = object : ClassLoader(javaClass.classLoader) {
            fun define(bytes: ByteArray): Class<*> = defineClass(null, bytes, 0, bytes.size)
        }
        val fixture = loader.define(writer.toByteArray())
        fixture.getMethod("run", CompiledProgramRuntime::class.java).invoke(null, runtime)
        return runtime
    }
}
