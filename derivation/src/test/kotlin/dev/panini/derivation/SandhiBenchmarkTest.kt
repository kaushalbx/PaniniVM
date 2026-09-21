package dev.panini.derivation

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SandhiBenchmarkTest {
    private val sandhiEngine = SandhiEngine()

    @TestFactory
    fun `canonical sandhi benchmark`(): List<DynamicTest> = loadCases().map { case ->
        DynamicTest.dynamicTest("${case.id}: ${case.name}") {
            val result = sandhiEngine.join(case.left, case.right)
            val appliedSutras = result.applications.mapTo(mutableSetOf()) { it.sutra }

            assertEquals(case.expected, result.final.surface, "final surface")
            assertTrue(
                appliedSutras.containsAll(case.requiredSutras),
                "required rules missing: ${case.requiredSutras - appliedSutras}; applied: $appliedSutras",
            )
            assertTrue(
                case.forbiddenSutras.none { it in appliedSutras },
                "forbidden rule applied: ${case.forbiddenSutras intersect appliedSutras}",
            )
            assertEquals(DerivationStage.FINAL, result.final.stage, "sandhi derivation must be terminal")
            assertTrue(result.final.surface.none { it == '\u0000' }, "surface must not contain sentinel material")
        }
    }

    private fun loadCases(): List<BenchmarkCase> {
        val json = requireNotNull(javaClass.getResource("/sandhi_benchmark.json")) {
            "Missing sandhi_benchmark.json test resource"
        }.readText()
        val records = TestJsonParser.parse(json) as? List<*> ?: error("Benchmark root must be a JSON array")
        require(records.isNotEmpty()) { "Sandhi benchmark must contain at least one case" }
        return records.map { parseCase(it as? Map<*, *> ?: error("Benchmark entry must be an object")) }
    }

    private fun parseCase(raw: Map<*, *>): BenchmarkCase {
        fun field(name: String): String = raw[name] as? String
            ?: error("Missing '$name' in benchmark case: $raw")
        fun sutras(name: String): Set<String> = field(name).split(',').filterTo(mutableSetOf()) { it.isNotBlank() }

        return BenchmarkCase(
            id = field("id"),
            name = field("name"),
            left = field("left"),
            right = field("right"),
            expected = field("expected"),
            requiredSutras = sutras("requiredSutras"),
            forbiddenSutras = sutras("forbiddenSutras"),
        )
    }

    private data class BenchmarkCase(
        val id: String,
        val name: String,
        val left: String,
        val right: String,
        val expected: String,
        val requiredSutras: Set<String>,
        val forbiddenSutras: Set<String>,
    )
}
