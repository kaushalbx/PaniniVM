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
            val result = samasaEngine.derive(
                SamasaDerivationRequest(
                    padas = case.padas,
                    type = case.samasaType,
                    outputLinga = case.outputLinga,
                    outputVacana = case.outputVacana,
                )
            )
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
        val records = TestJsonParser.parse(json) as? List<*> ?: error("Benchmark root must be a JSON array")
        return records.map { parseCase(it as? Map<*, *> ?: error("Benchmark entry must be an object")) }
    }

    private fun parseCase(raw: Map<*, *>): BenchmarkCase {
        fun field(name: String): String = raw[name] as? String ?: error("Missing '$name' in benchmark case: $raw")
        fun optionalField(name: String): String? = raw[name] as? String
        val padas = (raw["padas"] as? List<*>)?.map { item ->
            val pada = item as? Map<*, *> ?: error("Pada must be an object: $item")
            SamasaPada(pada["upadesha"] as String, Vibhakti.valueOf(pada["vibhakti"] as String))
        } ?: error("Missing padas in benchmark case: $raw")
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
            outputLinga = optionalField("outputLinga")?.let(dev.panini.core.Linga::valueOf),
            outputVacana = optionalField("outputVacana")?.let(dev.panini.core.Vacana::valueOf),
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
        val outputLinga: dev.panini.core.Linga?,
        val outputVacana: dev.panini.core.Vacana?,
    )
}
