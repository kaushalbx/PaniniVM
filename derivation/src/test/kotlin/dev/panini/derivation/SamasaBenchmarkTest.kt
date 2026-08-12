package dev.panini.derivation

import dev.panini.analysis.SamasaPada
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SamasaBenchmarkTest {
    private val samasaEngine = SamasaEngine()

    @TestFactory
    fun `canonical samasa benchmark`(): List<DynamicTest> = loadCases().map { case ->
        DynamicTest.dynamicTest("${case.id}: ${case.name}") {
            val result = samasaEngine.derive(case.padas, case.samasaType)
            val resolution = requireNotNull(result.samasaResolution)

            assertEquals(case.expectedStem, resolution.compoundStem, "compound stem")
            assertEquals(case.expectedSurface, result.final.surface, "final surface")
            assertEquals(case.classificationSutra, resolution.classificationSutra, "classification rule")
            assertEquals(case.transformationSutras, resolution.transformationSutras, "transformation rules")
            val appliedRules = result.applications.mapTo(mutableSetOf()) { it.sutra }
            assertTrue(case.forbiddenSutras.none { it in appliedRules }, "forbidden rule applied: ${case.forbiddenSutras intersect appliedRules}")
            assertTrue("2.4.71" in resolution.supLopaSutras, "internal sup-lopa must be recorded")
            assertTrue(result.final.stage == DerivationStage.FINAL, "samasa derivation must be terminal")
            assertTrue(result.final.terms.size == 1, "completed samasa must contain one final term")
            assertTrue(result.final.surface.none { it == '\u0000' }, "surface must not contain sentinel material")
        }
    }

    private fun loadCases(): List<BenchmarkCase> {
        val json = requireNotNull(javaClass.getResource("/samasa_benchmark.json")) {
            "Missing samasa_benchmark.json test resource"
        }.readText()
        return json.trim().removePrefix("[").removeSuffix("]")
            .split(Regex("\\n\\s*},\\s*\\n\\s*\\{"))
            .map { raw -> parseCase(raw.trim().removePrefix("{").removeSuffix("}")) }
    }

    private fun parseCase(raw: String): BenchmarkCase {
        fun field(name: String): String = Regex("\\\"$name\\\"\\s*:\\s*\\\"([^\\\"]*)\\\"")
            .find(raw)?.groupValues?.get(1) ?: error("Missing '$name' in benchmark case: $raw")
        val padasBlock = Regex("\\\"padas\\\"\\s*:\\s*\\[(.*?)]", RegexOption.DOT_MATCHES_ALL)
            .find(raw)?.groupValues?.get(1) ?: error("Missing padas in benchmark case: $raw")
        val padas = Regex("\\{\\s*\\\"upadesha\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"\\s*,\\s*\\\"vibhakti\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"\\s*}")
            .findAll(padasBlock)
            .map { match -> SamasaPada(match.groupValues[1], Vibhakti.valueOf(match.groupValues[2])) }
            .toList()
        val transformations = field("transformationSutras").split(',').filter { it.isNotBlank() }
        val forbidden = field("forbiddenSutras").split(',').filter { it.isNotBlank() }

        return BenchmarkCase(
            id = field("id"),
            name = field("name"),
            padas = padas,
            samasaType = SamasaType.valueOf(field("samasaType")),
            expectedStem = field("expectedStem"),
            expectedSurface = field("expectedSurface"),
            classificationSutra = field("classificationSutra"),
            transformationSutras = transformations,
            forbiddenSutras = forbidden,
        )
    }

    private data class BenchmarkCase(
        val id: String,
        val name: String,
        val padas: List<SamasaPada>,
        val samasaType: SamasaType,
        val expectedStem: String,
        val expectedSurface: String,
        val classificationSutra: String,
        val transformationSutras: List<String>,
        val forbiddenSutras: List<String>,
    )
}
