package dev.panini.derivation

import dev.panini.analysis.SamasaMorphologicalFeature
import dev.panini.analysis.SamasaPada
import dev.panini.analysis.SamasaSemanticRelation
import dev.panini.core.Linga
import dev.panini.core.SamasaType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Samasanta114To160PipelineTest {
    private val engine = SamasaEngine()

    @Test
    fun `5 4 114 performs its licensed member transformation`() {
        val resolution = derive(
            SamasaPada("द्वि"),
            SamasaPada("अङ्गुलि"),
            semantics = setOf(SamasaSemanticRelation.WOODEN_OBJECT),
        )
        assertEquals("द्व्यङ्गुल", resolution.compoundStem)
        assertTrue("5.4.114" in resolution.transformationSutras)
    }

    @Test
    fun `5 4 121 exposes applied and omitted optional branches`() {
        val resolution = derive(SamasaPada("सु"), SamasaPada("हलि"))
        assertTrue(resolution.alternatives.any { "5.4.121" in it.transformationSutras })
        assertTrue(resolution.alternatives.any { "5.4.121" !in it.transformationSutras })
    }

    @Test
    fun `5 4 152 recognizes ordinary devanagari in ending`() {
        val resolution = derive(
            SamasaPada("बहु"),
            SamasaPada("स्वामिन्"),
            outputLinga = Linga.STRI,
        )
        assertEquals("बहुस्वामिन्क", resolution.compoundStem)
        assertTrue("5.4.152" in resolution.transformationSutras)
    }

    @Test
    fun `5 4 156 prohibition suppresses residual kap`() {
        val resolution = derive(
            SamasaPada("बहु"),
            SamasaPada("श्रेयस्", morphologicalFeatures = setOf(SamasaMorphologicalFeature.IYAS_ENDING)),
            semantics = setOf(SamasaSemanticRelation.RESIDUAL_KAP_OPTION),
        )
        assertTrue("5.4.156" in resolution.prohibitedSutras)
        assertTrue("5.4.154" !in resolution.transformationSutras)
    }

    private fun derive(
        first: SamasaPada,
        last: SamasaPada,
        outputLinga: Linga = Linga.PUMS,
        semantics: Set<SamasaSemanticRelation> = emptySet(),
    ) = requireNotNull(
        engine.derive(
            listOf(first, last),
            SamasaType.BAHUVRIHI,
            outputLinga = outputLinga,
            semanticRelations = semantics,
        ).samasaResolution,
    )
}
