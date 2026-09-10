package dev.panini.derivation

import dev.panini.analysis.SamasaMorphologicalFeature
import dev.panini.analysis.SamasaPada
import dev.panini.analysis.SamasaSemanticRelation
import dev.panini.core.Linga
import dev.panini.core.SamasaType
import kotlin.test.Test
import kotlin.test.assertTrue

class Samasanta69To112PipelineTest {
    private val engine = SamasaEngine()

    @Test
    fun `5 4 69 praise prohibition blocks otherwise matching samasanta`() {
        val resolution = requireNotNull(
            engine.derive(
                SamasaDerivationRequest(
                    padas = listOf(SamasaPada("सु"), SamasaPada("राजन्")),
                    type = SamasaType.TATPURUSA,
                    semanticRelations = setOf(SamasaSemanticRelation.PRAISE),
                ),
            ).samasaResolution,
        )
        assertTrue("5.4.69" in resolution.prohibitedSutras)
        assertTrue("5.4.91" !in resolution.transformationSutras)
    }

    @Test
    fun `5 4 72 restores an optional pathin branch after nan prohibition`() {
        val alternatives = requireNotNull(
            engine.derive(
                listOf(SamasaPada("नञ्"), SamasaPada("पथिन्")),
                SamasaType.NAN_TATPURUSA,
            ).samasaResolution,
        ).alternatives
        assertTrue(alternatives.any { "5.4.72" in it.transformationSutras })
        assertTrue(alternatives.any { "5.4.72" !in it.transformationSutras })
    }

    @Test
    fun `5 4 105 exposes applied and omitted brahman branches`() {
        val alternatives = requireNotNull(
            engine.derive(
                listOf(SamasaPada("कु"), SamasaPada("ब्रह्मन्")),
                SamasaType.TATPURUSA,
                outputLinga = Linga.PUMS,
            ).samasaResolution,
        ).alternatives
        assertTrue(alternatives.any { "5.4.105" in it.transformationSutras })
        assertTrue(alternatives.any { "5.4.105" !in it.transformationSutras })
    }

    @Test
    fun `5 4 109 outranks 108 and remains optional for neuter an ending`() {
        val resolution = requireNotNull(
            engine.derive(
                listOf(SamasaPada("उप"), SamasaPada("अन्", linga = Linga.NAPUMSAKA)),
                SamasaType.AVYAYIBHAVA,
            ).samasaResolution,
        )
        assertTrue("5.4.109" in resolution.transformationSutras)
        assertTrue("5.4.108" !in resolution.transformationSutras)
        assertTrue(resolution.alternatives.any { "5.4.109" !in it.transformationSutras })
    }
}
