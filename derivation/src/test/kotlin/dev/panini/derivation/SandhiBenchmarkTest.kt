package dev.panini.derivation

import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SandhiBenchmarkTest {
    private val sandhiEngine = SandhiEngine()

    @TestFactory
    fun `canonical sandhi benchmark`(): List<DynamicTest> = loadCases().map { case ->
        DynamicTest.dynamicTest("${case.id}: ${case.name}") {
            val config = DerivationConfig(
                optionalRulePolicy = OptionalRulePolicy.CUSTOM,
                optionalRuleSelector = { it !in case.skippedSutras },
            )
            val result = if (case.leftRupa != null || case.rightRupa != null) {
                fun pada(id: String, surface: String, rupa: Rupa?) = SandhiEngine.Pada(
                    DerivationTerm(id, surface, TermKind.PRATIPADIKA), rupa ?: Rupa(),
                )
                sandhiEngine.join(pada("left", case.left, case.leftRupa), pada("right", case.right, case.rightRupa), config)
            } else sandhiEngine.join(case.left, case.right, config)
            val appliedSutras = result.applications.mapTo(mutableSetOf()) { it.sutra }

            assertEquals(case.expected, sandhiEngine.render(result), "rendered final surface")
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
        val cases = records.map { parseCase(it as? Map<*, *> ?: error("Benchmark entry must be an object")) }
        require(cases.map { it.id }.distinct().size == cases.size) { "Benchmark IDs must be unique" }
        return cases
    }

    private fun parseCase(raw: Map<*, *>): BenchmarkCase {
        fun field(name: String): String = raw[name] as? String
            ?: error("Missing '$name' in benchmark case: $raw")
        fun sutras(name: String): Set<String> = field(name).split(',').filterTo(mutableSetOf()) { it.isNotBlank() }
        fun rupa(name: String): Rupa? {
            val value = raw[name] ?: return null
            val fields = value as? Map<*, *> ?: error("'$name' must be a Rupa object")
            require(fields.keys.all { it in setOf("vibhakti", "vacana", "isVocative") }) { "Unknown Rupa field: $fields" }
            return Rupa(
                vibhakti = fields["vibhakti"]?.let { Vibhakti.valueOf(it as String) },
                vacana = fields["vacana"]?.let { Vacana.valueOf(it as String) },
                isVocative = fields["isVocative"]?.let { it as Boolean } ?: false,
            )
        }

        return BenchmarkCase(
            id = field("id"),
            name = field("name"),
            left = field("left"),
            right = field("right"),
            expected = field("expected"),
            leftRupa = rupa("leftRupa"),
            rightRupa = rupa("rightRupa"),
            requiredSutras = sutras("requiredSutras"),
            forbiddenSutras = sutras("forbiddenSutras"),
            skippedSutras = (raw["skippedSutras"] as? String).orEmpty().split(',').filterTo(mutableSetOf()) { it.isNotBlank() },
        )
    }

    private data class BenchmarkCase(
        val id: String,
        val name: String,
        val left: String,
        val right: String,
        val expected: String,
        val leftRupa: Rupa?,
        val rightRupa: Rupa?,
        val requiredSutras: Set<String>,
        val forbiddenSutras: Set<String>,
        val skippedSutras: Set<String>,
    )
}
