package dev.panini.compiler

import dev.panini.execution.*
import java.io.File
import kotlin.test.*

class ReviewedCollectionExamplesTest {
    @Test fun `corrected collection examples preserve interpreter results when compiled`() {
        for ((index, name) in listOf("sort_and_partition", "group_by", "distinct").withIndex()) {
            val source = File("examples/collections/$name.pvm").readText()
            val results = PaniniVM().evalScript(source)
            assertTrue(results.all { it is ExecutionResult.Success }, "$name: $results")
            val expected = requireNotNull((results.last() as ExecutionResult.Success).typedValue)
            val generated = BytecodeCompiler.compileAndLoad(source, "ReviewedCollection$index")
            val values = generated.getMethod("execute").invoke(null) as Map<*, *>
            assertEquals(expected, values["LastResult"], name)
        }
    }
}
