package dev.panini.derivation

import dev.panini.analysis.SamasaPada
import dev.panini.analysis.SamasaSemanticRelation
import dev.panini.core.Linga
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class SamasaRejectionBenchmarkTest {
    private val engine = SamasaEngine()

    @TestFactory
    fun `strict samasa rejection benchmark`(): List<DynamicTest> {
        val text = requireNotNull(javaClass.getResource("/samasa_rejection_benchmark.json")).readText()
        val cases = TestJsonParser.parse(text) as List<*>
        return cases.map { entry ->
            val case = entry as Map<*, *>
            DynamicTest.dynamicTest("${case["id"]}: ${case["name"]}") {
                val padas = (case["padas"] as List<*>).map { raw ->
                    val pada = raw as Map<*, *>
                    SamasaPada(pada["upadesha"] as String, Vibhakti.valueOf(pada["vibhakti"] as String))
                }
                val error = assertFailsWith<IllegalArgumentException> {
                    engine.derive(
                        SamasaDerivationRequest(
                            padas = padas,
                            type = SamasaType.valueOf(case["samasaType"] as String),
                            outputLinga = (case["outputLinga"] as? String)?.let(Linga::valueOf),
                            semanticRelations = (case["semanticRelations"] as List<*>).mapTo(mutableSetOf()) {
                                SamasaSemanticRelation.valueOf(it as String)
                            },
                            strictSemantics = true,
                        ),
                    )
                }
                assertTrue(error.message.orEmpty().contains(case["expectedMissing"] as String), error.message)
            }
        }
    }
}
