package dev.panini.derivation

import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.core.Linga
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
                alternativeChoices = case.alternativeChoices,
            )
            val result = if (case.leftRupa != null || case.rightRupa != null || case.leftUpadesha != null || case.rightUpadesha != null ||
                case.leftVisargaSource != null || case.rightVisargaSource != null) {
                fun pada(id: String, surface: String, rupa: Rupa?, upadesha: String?, source: VisargaSource?): SandhiEngine.Pada {
                    if (upadesha in setOf("तद्", "एतद्") && rupa?.linga != null &&
                        rupa.vibhakti != null && rupa.vacana != null) {
                        val derivation = SubantaEngine().derive(SubantaDerivationRequest(
                            upadesha!!, requireNotNull(rupa.vibhakti), requireNotNull(rupa.vacana),
                            requireNotNull(rupa.linga)))
                        val derived = sandhiEngine.padaFromDerivation(derivation, id)
                        require(derived.term.surface == surface) {
                            "Declared benchmark form $surface does not follow from $upadesha and $rupa"
                        }
                        return derived
                    }
                    return SandhiEngine.Pada(
                        DerivationTerm(id, surface, TermKind.PRATIPADIKA, upadesha = upadesha ?: surface), rupa ?: Rupa(),
                        visargaSource = source,
                    )
                }
                sandhiEngine.join(pada("left", case.left, case.leftRupa, case.leftUpadesha, case.leftVisargaSource),
                    pada("right", case.right, case.rightRupa, case.rightUpadesha, case.rightVisargaSource), config, case.finalBoundary)
            } else sandhiEngine.join(case.left, case.right, config, case.finalBoundary)
            val appliedSutras = result.applications.mapTo(mutableSetOf()) { it.sutra }

            assertEquals(case.expected, sandhiEngine.render(result), "rendered final surface; applied: $appliedSutras")
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
            require(fields.keys.all { it in setOf("linga", "vibhakti", "vacana", "isVocative") }) { "Unknown Rupa field: $fields" }
            return Rupa(
                linga = fields["linga"]?.let { Linga.valueOf(it as String) },
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
            alternativeChoices = (raw["alternativeChoices"] as? Map<*, *>).orEmpty().entries.associate {
                it.key as String to (it.value as Number).toInt()
            },
            finalBoundary = (raw["finalBoundary"] as? String)?.let(SpeechBoundary::valueOf) ?: SpeechBoundary.CONTINUATION,
            leftUpadesha = raw["leftUpadesha"] as? String,
            rightUpadesha = raw["rightUpadesha"] as? String,
            leftVisargaSource = (raw["leftVisargaSource"] as? String)?.let(VisargaSource::valueOf),
            rightVisargaSource = (raw["rightVisargaSource"] as? String)?.let(VisargaSource::valueOf),
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
        val alternativeChoices: Map<String, Int>,
        val finalBoundary: SpeechBoundary,
        val leftUpadesha: String?,
        val rightUpadesha: String?,
        val leftVisargaSource: VisargaSource?,
        val rightVisargaSource: VisargaSource?,
    )
}
