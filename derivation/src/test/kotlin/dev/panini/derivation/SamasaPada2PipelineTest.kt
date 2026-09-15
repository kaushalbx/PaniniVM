package dev.panini.derivation

import dev.panini.analysis.SamasaPada
import dev.panini.core.Linga
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import kotlin.test.Test
import kotlin.test.assertTrue

class SamasaPada2PipelineTest {
    private val engine = SamasaEngine()

    @Test
    fun `2 2 2 classifies ardha tatpurusa`() {
        assertApplied("2.2.2", listOf(SamasaPada("अर्ध"), SamasaPada("पिप्पली")), SamasaType.TATPURUSA)
    }

    @Test
    fun `2 2 21 participates as an optional avyayibhava niyama`() {
        val result = engine.derive(
            listOf(SamasaPada("उप"), SamasaPada("ग्राम", Vibhakti.SAPTAMI)),
            SamasaType.AVYAYIBHAVA,
        )
        val alternatives = requireNotNull(result.samasaResolution).alternatives
        assertTrue(alternatives.any { "2.2.21" in it.transformationSutras })
        assertTrue(alternatives.any { "2.2.21" !in it.transformationSutras })
    }

    @Test
    fun `2 2 25 classifies approximate numeral bahuvrihi`() {
        assertApplied("2.2.25", listOf(SamasaPada("उप"), SamasaPada("पञ्चाशत्")), SamasaType.BAHUVRIHI)
    }

    @Test
    fun `2 2 30 records general upasarjana ordering`() {
        assertApplied(
            "2.2.30",
            listOf(SamasaPada("राजन्", Vibhakti.SASTHI), SamasaPada("पुरुष")),
            SamasaType.TATPURUSA,
        )
    }

    @Test
    fun `specific pada two ordering rules override general ordering`() {
        val examples = listOf(
            Triple("2.2.31", listOf(SamasaPada("राज"), SamasaPada("दन्त")), SamasaType.TATPURUSA),
            Triple("2.2.35", listOf(SamasaPada("कण्ठ", Vibhakti.SAPTAMI), SamasaPada("काल")), SamasaType.BAHUVRIHI),
            Triple("2.2.36", listOf(SamasaPada("कृत"), SamasaPada("कृत्य")), SamasaType.BAHUVRIHI),
        )
        examples.forEach { (rule, padas, type) -> assertApplied(rule, padas, type) }
    }

    @Test
    fun `optional ordering rules expose applied and omitted traces`() {
        val examples = listOf(
            Triple("2.2.37", listOf(SamasaPada("आहिताग्नि"), SamasaPada("पुरुष")), SamasaType.BAHUVRIHI),
            Triple("2.2.38", listOf(SamasaPada("कडार"), SamasaPada("हाटक")), SamasaType.KARMADHARAYA),
        )
        examples.forEach { (rule, padas, type) ->
            val alternatives = requireNotNull(
                engine.derive(padas, type, outputLinga = Linga.PUMS).samasaResolution,
            ).alternatives
            assertTrue(alternatives.any { rule in it.transformationSutras }, "$rule applied branch")
            assertTrue(alternatives.any { rule !in it.transformationSutras }, "$rule omitted branch")
        }
    }

    private fun assertApplied(rule: String, padas: List<SamasaPada>, type: SamasaType) {
        val result = engine.derive(padas, type, outputLinga = Linga.PUMS)
        assertTrue(result.applications.any { it.sutra == rule }, "$rule was not applied")
    }
}
