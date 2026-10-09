package dev.panini.compiler

import dev.panini.execution.ExecutionError
import dev.panini.execution.SanskritValue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CompiledActionResultHistoryTest {
    @Test
    fun `structured history snapshots nested containers on recording and reading`() {
        val runtime = CompiledProgramRuntime()
        val items = mutableListOf<SanskritValue>(SanskritValue.Sankhya(1, "एक"))
        val fields = mutableMapOf<String, SanskritValue>("items" to SanskritValue.Suchi(items))
        val value = SanskritValue.Rupa("record", fields)
        runtime.recordActionFrame("कृ", value, mapOf(dev.panini.core.Karaka.KARMAN to listOf(value)))
        items.clear()
        fields.clear()
        val expected = SanskritValue.Rupa("record", mapOf("items" to SanskritValue.Suchi(listOf(SanskritValue.Sankhya(1, "एक")))))
        assertEquals(expected, runtime.loadActionResult("कृ", 1))
        assertEquals(listOf(expected), runtime.loadActionParticipants("कृ", 1, dev.panini.core.Karaka.KARMAN))
        val read = runtime.loadActionResult("कृ", 1) as SanskritValue.Rupa
        (read.fields as MutableMap).clear()
        assertEquals(expected, runtime.loadOrdinalActionResult("कृ", 1))
    }

    @Test
    fun `cyclic values fail before publishing an action frame`() {
        val runtime = CompiledProgramRuntime()
        val items = mutableListOf<SanskritValue>()
        val value = SanskritValue.Suchi(items)
        items.add(value)
        assertEquals(ExecutionError.INVALID_VALUE, assertFailsWith<CompiledPaniniExecutionException> {
            runtime.recordActionResult("कृ", value)
        }.error)
        assertFailsWith<CompiledPaniniExecutionException> { runtime.loadActionResult("कृ", 1) }
    }
    @Test
    fun `participant frames share action chronology without confusing inputs and result`() {
        val runtime = CompiledProgramRuntime()
        val one = SanskritValue.Sankhya(1, "एक")
        val two = SanskritValue.Sankhya(2, "द्वि")
        val members = mutableListOf<SanskritValue>(one, two)
        val bindings = mutableMapOf<dev.panini.core.Karaka, List<SanskritValue>>(dev.panini.core.Karaka.KARMAN to members)
        runtime.recordActionFrame("युज्", SanskritValue.Sankhya(3, "त्रि"), bindings)
        members.clear()
        bindings.clear()
        runtime.recordActionResult("युज्", SanskritValue.Sankhya(5, "पञ्च"))
        assertEquals(listOf(one, two), runtime.loadOrdinalActionParticipants("युज्", 1, dev.panini.core.Karaka.KARMAN))
        assertEquals(listOf(one, two), runtime.loadActionParticipants("युज्", 2, dev.panini.core.Karaka.KARMAN))
        assertEquals(3L, (runtime.loadOrdinalActionResult("युज्", 1) as SanskritValue.Sankhya).value)
        for (load in listOf<() -> List<SanskritValue>>(
            { runtime.loadActionParticipants("युज्", 1, dev.panini.core.Karaka.KARMAN) },
            { runtime.loadOrdinalActionParticipants("युज्", 1, dev.panini.core.Karaka.KARANA) },
            { runtime.loadOrdinalActionParticipants("युज्", Long.MAX_VALUE, dev.panini.core.Karaka.KARMAN) },
            { CompiledProgramRuntime().loadActionParticipants("युज्", 1, dev.panini.core.Karaka.KARMAN) },
        )) {
            assertEquals(ExecutionError.INVALID_VALUE, assertFailsWith<CompiledPaniniExecutionException> { load() }.error)
        }
    }
    @Test
    fun `action histories keep typed values independent of prints and variables`() {
        val runtime = CompiledProgramRuntime()
        val chosen = SanskritValue.Sankhya(3, "त्रीणि")
        runtime.recordActionResult("चिञ्", chosen)
        runtime.recordActionResult("मुद्रँ", SanskritValue.Shabda("नवन्"))
        runtime.storeValue("LastResult", SanskritValue.Shabda("नवन्"))
        assertEquals(chosen, runtime.loadActionResult("चिञ्", 1))
        assertEquals(mapOf("LastResult" to SanskritValue.Shabda("नवन्")), runtime.snapshot())
        runtime.recordActionResult("चिञ्", SanskritValue.Sankhya(5, "पञ्च"))
        assertEquals(5L, (runtime.loadActionResult("चिञ्", 1) as SanskritValue.Sankhya).value)
        assertEquals(chosen, runtime.loadActionResult("चिञ्", 2))
    }

    @Test
    fun `history survives nested parameter frames but not a separate execution`() {
        val runtime = CompiledProgramRuntime()
        runtime.enterFrame(arrayOf("argument"), arrayOf(SanskritValue.Shabda("outer")))
        runtime.enterFrame(arrayOf("argument"), arrayOf(SanskritValue.Shabda("inner")))
        val value = SanskritValue.Suchi(listOf(SanskritValue.Sankhya(1, "एक")))
        runtime.recordActionResult("ग्रहँ", value)
        runtime.exitFrame()
        runtime.exitFrame()
        assertEquals(value, runtime.loadActionResult("ग्रहँ", 1))
        val missing = assertFailsWith<CompiledPaniniExecutionException> {
            CompiledProgramRuntime().loadActionResult("ग्रहँ", 1)
        }
        assertEquals(ExecutionError.INVALID_VALUE, missing.error)
    }

    @Test
    fun `missing history and invalid recency fail explicitly`() {
        val runtime = CompiledProgramRuntime()
        assertFailsWith<IllegalArgumentException> { runtime.recordActionResult("", SanskritValue.Shabda("x")) }
        assertFailsWith<IllegalArgumentException> { runtime.loadActionResult("चिञ्", 0) }
        assertFailsWith<CompiledPaniniExecutionException> { runtime.loadActionResult("चिञ्", 1) }
        runtime.recordActionResult("चिञ्", SanskritValue.Sankhya(1, "एक"))
        assertFailsWith<CompiledPaniniExecutionException> { runtime.loadActionResult("चिञ्", 2) }
    }

    @Test
    fun `chronological ordinal remains stable when newer results arrive`() {
        val runtime = CompiledProgramRuntime()
        runtime.recordActionResult("चिञ्", SanskritValue.Sankhya(3, "त्रीणि"))
        runtime.recordActionResult("चिञ्", SanskritValue.Sankhya(5, "पञ्च"))
        runtime.recordActionResult("मुद्रँ", SanskritValue.Shabda("नवन्"))
        assertEquals(3L, (runtime.loadOrdinalActionResult("चिञ्", 1) as SanskritValue.Sankhya).value)
        assertEquals(5L, (runtime.loadOrdinalActionResult("चिञ्", 2) as SanskritValue.Sankhya).value)
        assertFailsWith<IllegalArgumentException> { runtime.loadOrdinalActionResult("चिञ्", 0) }
        assertFailsWith<CompiledPaniniExecutionException> { runtime.loadOrdinalActionResult("चिञ्", 3) }
        assertFailsWith<CompiledPaniniExecutionException> { runtime.loadOrdinalActionResult("चिञ्", 4_294_967_297L) }
        assertFailsWith<CompiledPaniniExecutionException> { runtime.loadOrdinalActionResult("चिञ्", Long.MAX_VALUE) }
    }
}
