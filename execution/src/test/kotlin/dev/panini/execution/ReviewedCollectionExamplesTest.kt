package dev.panini.execution

import java.io.File
import kotlin.test.*

class ReviewedCollectionExamplesTest {
    @Test fun `corrected source endings preserve member order and duplicate values`() {
        for ((name, members) in mapOf(
            "sort_and_partition" to listOf(3L, 1L, 2L, 1L),
            "group_by" to listOf(1L, 2L, 3L, 4L, 2L),
            "distinct" to listOf(1L, 2L, 1L, 3L, 2L))) {
            val results = PaniniVM().evalScript(File("examples/collections/$name.pvm").readText())
            assertTrue(results.all { it is ExecutionResult.Success }, "$name: $results")
            val successes = results.filterIsInstance<ExecutionResult.Success>()
            val declared = assertIs<SanskritValue.Suchi>(successes.first().typedValue)
            assertEquals(members, declared.items.map { assertIs<SanskritValue.Sankhya>(it).value }, name)
            if (name == "distinct") {
                val distinct = assertIs<SanskritValue.Suchi>(successes.last().typedValue)
                assertEquals(listOf(1L, 2L, 3L), distinct.items.map { assertIs<SanskritValue.Sankhya>(it).value })
            }
        }
    }
}
