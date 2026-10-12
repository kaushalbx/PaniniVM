package dev.panini.compiler

import dev.panini.execution.SanskritValue
import kotlin.test.*

class RecordReferenceScopeTest {
    @Test fun `nested record references obey local frames and restore outer bindings`() {
        fun record(number: Long) = SanskritValue.Rupa("record",
            mapOf("field" to SanskritValue.Sankhya(number, "host")))
        val global = record(1)
        val local = record(2)
        val inner = record(3)
        val runtime = CompiledProgramRuntime(mapOf("मान" to global))
        val reference = SanskritValue.Shabda("मान")
        assertEquals(global, runtime.resolveRecordReference(reference))
        runtime.enterFrame(arrayOf("मान"), arrayOf(local))
        assertEquals(local, runtime.resolveRecordReference(reference))
        runtime.enterBindingFrame(arrayOf("मान"), arrayOf(inner))
        assertEquals(inner, runtime.resolveRecordReference(reference))
        runtime.exitFrame()
        assertEquals(local, runtime.resolveRecordReference(reference))
        runtime.exitFrame()
        assertEquals(global, runtime.resolveRecordReference(reference))
        runtime.enterFrame(arrayOf("मान"), arrayOf(SanskritValue.Shabda("not a record")))
        assertFailsWith<IllegalArgumentException> { runtime.resolveRecordReference(reference) }
        runtime.exitFrame()
        assertEquals(global, runtime.resolveRecordReference(reference))
    }
}
