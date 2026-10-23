package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class NaturalListGroupByTest {
    @Test
    fun `natural vi bhaj sentence groups a named list by a typed key operation`() {
        val results = PaniniVM().evalScript(
            """
            एक + ङस् द्वि + ओस् त्रि + आम् चतुर् + आम् च द्वि + आम् च सूची + सुँ असँ + लट् + तिप् ।
            सूची + अम् यु + टा वि + भजँ + लोट् + सिप् ।
            """.trimIndent(),
        )

        val last = results.last()
        val grouped = assertIs<SanskritValue.Suchi>(assertIs<ExecutionResult.Success>(last, last.toString()).typedValue)
        assertEquals(2, grouped.items.size)
        fun values(index: Int): List<Long> = assertIs<SanskritValue.Suchi>(
            assertIs<SanskritValue.Rupa>(grouped.items[index]).fields.getValue("सदस्यसूची"),
        ).items.map { assertIs<SanskritValue.Sankhya>(it).value }
        assertEquals(listOf(1L, 3L), values(0))
        assertEquals(listOf(2L, 4L, 2L), values(1))
    }

    @Test
    fun `natural vi Shish sentence retains first typed-distinct members`() {
        val results = PaniniVM().evalScript(
            """
            एक + ङस् द्वि + ओस् एक + आम् त्रि + आम् च द्वि + आम् च सूची + सुँ असँ + लट् + तिप् ।
            सूची + अम् वि + शिषॢँ + लोट् + सिप् ।
            """.trimIndent(),
        )
        val last = assertIs<ExecutionResult.Success>(results.last(), results.last().toString())
        val distinct = assertIs<SanskritValue.Suchi>(last.typedValue)
        assertEquals(listOf(1L, 2L, 3L), distinct.items.map { assertIs<SanskritValue.Sankhya>(it).value })
    }

    @Test
    fun `natural Kram sentences sort ascending and descending`() {
        val declaration = "त्रि + ङस् एक + ओस् द्वि + आम् च एक + आम् च सूची + सुँ असँ + लट् + तिप् ।"
        fun evaluate(prefix: String): List<Long> {
            val result = PaniniVM().evalScript(
                "$declaration\nसूची + अम् $prefix क्रमुँ + लोट् + सिप् ।",
            ).last()
            return assertIs<SanskritValue.Suchi>(
                assertIs<ExecutionResult.Success>(result, result.toString()).typedValue,
            ).items.map { assertIs<SanskritValue.Sankhya>(it).value }
        }
        assertEquals(listOf(1L, 1L, 2L, 3L), evaluate(""))
        assertEquals(listOf(3L, 2L, 1L, 1L), evaluate("प्रति +"))
    }

    @Test
    fun `natural prati vi Bhaj sentence partitions with a typed truth operation`() {
        val results = PaniniVM().evalScript(
            """
            एक + ङस् द्वि + ओस् त्रि + आम् चतुर् + आम् च द्वि + आम् च सूची + सुँ असँ + लट् + तिप् ।
            सूची + अम् यु + टा प्रति + वि + भजँ + लोट् + सिप् ।
            """.trimIndent(),
        )
        val partition = assertIs<SanskritValue.Rupa>(
            assertIs<ExecutionResult.Success>(results.last(), results.last().toString()).typedValue,
        )
        fun values(field: String) = assertIs<SanskritValue.Suchi>(partition.fields.getValue(field))
            .items.map { assertIs<SanskritValue.Sankhya>(it).value }
        assertEquals(listOf(2L, 4L, 2L), values("सत्यसूची"))
        assertEquals(listOf(1L, 3L), values("असत्यसूची"))
    }

    @Test
    fun `natural map and filter use unary typed callbacks`() {
        val declaration = "एक + ङस् द्वि + ओस् त्रि + आम् चतुर् + आम् च सूची + सुँ असँ + लट् + तिप् ।"
        fun values(sentence: String): List<Long> {
            val result = PaniniVM().evalScript("$declaration\n$sentence").last()
            return assertIs<SanskritValue.Suchi>(
                assertIs<ExecutionResult.Success>(result, result.toString()).typedValue,
            ).items.map { assertIs<SanskritValue.Sankhya>(it).value }
        }
        assertEquals(
            listOf(2L, 4L, 6L, 8L),
            values("सूची + अम् एधँ + टा सम् + यु + लोट् + सिप् ।"),
        )
        assertEquals(
            listOf(2L, 4L),
            values("सूची + अम् यु + टा वि + वृजीँ + लोट् + सिप् ।"),
        )
    }
}
