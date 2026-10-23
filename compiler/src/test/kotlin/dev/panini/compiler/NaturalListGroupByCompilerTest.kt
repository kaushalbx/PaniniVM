package dev.panini.compiler

import dev.panini.execution.SanskritValue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class NaturalListGroupByCompilerTest {
    @Test
    fun `compiled vi bhaj group by matches stable typed grouping`() {
        val generated = BytecodeCompiler.compileAndLoad(
            "सूची + अम् यु + टा वि + भजँ + लोट् + सिप् ।",
            "CompiledNaturalGroupBy",
        )
        val source = SanskritValue.Suchi(
            listOf(1L, 2L, 3L, 4L, 2L).map { SanskritValue.Sankhya(it, it.toString()) },
        )
        val sentinel = SanskritValue.Shabda("बाह्य")
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute", Map::class.java)
            .invoke(null, mapOf("सूची" to source, "group_member" to sentinel)) as Map<String, SanskritValue>

        val grouped = assertIs<SanskritValue.Suchi>(values.getValue("LastResult"))
        assertEquals(sentinel, values.getValue("group_member"))
        assertEquals(2, grouped.items.size)
        fun members(index: Int): List<Long> = assertIs<SanskritValue.Suchi>(
            assertIs<SanskritValue.Rupa>(grouped.items[index]).fields.getValue("सदस्यसूची"),
        ).items.map { assertIs<SanskritValue.Sankhya>(it).value }
        assertEquals(listOf(1L, 3L), members(0))
        assertEquals(listOf(2L, 4L, 2L), members(1))
    }

    @Test
    fun `compiled vi Shish distinct preserves first typed members`() {
        val generated = BytecodeCompiler.compileAndLoad(
            "सूची + अम् वि + शिषॢँ + लोट् + सिप् ।",
            "CompiledNaturalDistinct",
        )
        val source = SanskritValue.Suchi(
            listOf(1L, 2L, 1L, 3L, 2L).map { SanskritValue.Sankhya(it, it.toString()) },
        )
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute", Map::class.java)
            .invoke(null, mapOf("सूची" to source)) as Map<String, SanskritValue>
        val distinct = assertIs<SanskritValue.Suchi>(values.getValue("LastResult"))
        assertEquals(listOf(1L, 2L, 3L), distinct.items.map { assertIs<SanskritValue.Sankhya>(it).value })
    }

    @Test
    fun `compiled Kram sorting preserves typed deterministic order`() {
        val source = SanskritValue.Suchi(
            listOf(3L, 1L, 2L, 1L).map { SanskritValue.Sankhya(it, it.toString()) },
        )
        for ((index, case) in listOf(
            "" to listOf(1L, 1L, 2L, 3L),
            "प्रति +" to listOf(3L, 2L, 1L, 1L),
        ).withIndex()) {
            val generated = BytecodeCompiler.compileAndLoad(
                "सूची + अम् ${case.first} क्रमुँ + लोट् + सिप् ।",
                "CompiledNaturalSort$index",
            )
            @Suppress("UNCHECKED_CAST")
            val values = generated.getMethod("execute", Map::class.java)
                .invoke(null, mapOf("सूची" to source)) as Map<String, SanskritValue>
            val sorted = assertIs<SanskritValue.Suchi>(values.getValue("LastResult"))
            assertEquals(case.second, sorted.items.map { assertIs<SanskritValue.Sankhya>(it).value })
        }
    }

    @Test
    fun `compiled prati vi Bhaj partition matches natural typed result`() {
        val generated = BytecodeCompiler.compileAndLoad(
            "सूची + अम् यु + टा प्रति + वि + भजँ + लोट् + सिप् ।",
            "CompiledNaturalPartition",
        )
        val source = SanskritValue.Suchi(
            listOf(1L, 2L, 3L, 4L, 2L).map { SanskritValue.Sankhya(it, it.toString()) },
        )
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute", Map::class.java)
            .invoke(null, mapOf("सूची" to source)) as Map<String, SanskritValue>
        val partition = assertIs<SanskritValue.Rupa>(values.getValue("LastResult"))
        fun members(field: String) = assertIs<SanskritValue.Suchi>(partition.fields.getValue(field))
            .items.map { assertIs<SanskritValue.Sankhya>(it).value }
        assertEquals(listOf(2L, 4L, 2L), members("सत्यसूची"))
        assertEquals(listOf(1L, 3L), members("असत्यसूची"))
    }

    @Test
    fun `compiled map and filter match unary typed callback semantics`() {
        val source = SanskritValue.Suchi(
            listOf(1L, 2L, 3L, 4L).map { SanskritValue.Sankhya(it, it.toString()) },
        )
        for ((index, case) in listOf(
            "सूची + अम् एधँ + टा सम् + यु + लोट् + सिप् ।" to listOf(2L, 4L, 6L, 8L),
            "सूची + अम् यु + टा वि + वृजीँ + लोट् + सिप् ।" to listOf(2L, 4L),
        ).withIndex()) {
            val generated = BytecodeCompiler.compileAndLoad(case.first, "CompiledTransform$index")
            val sentinel = SanskritValue.Shabda("बाह्य")
            @Suppress("UNCHECKED_CAST")
            val values = generated.getMethod("execute", Map::class.java)
                .invoke(null, mapOf("सूची" to source, "loop_element" to sentinel)) as Map<String, SanskritValue>
            val result = assertIs<SanskritValue.Suchi>(values.getValue("LastResult"))
            assertEquals(sentinel, values.getValue("loop_element"))
            assertEquals(case.second, result.items.map { assertIs<SanskritValue.Sankhya>(it).value })
        }
    }
}
