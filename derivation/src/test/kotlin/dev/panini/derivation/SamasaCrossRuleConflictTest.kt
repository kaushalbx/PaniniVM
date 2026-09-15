package dev.panini.derivation

import dev.panini.analysis.SamasaPada
import dev.panini.analysis.SamasaSemanticRelation
import dev.panini.core.Linga
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SamasaCrossRuleConflictTest {
    private val engine = SamasaEngine()

    @Test
    fun `stem transformation precedes kap and both operations remain structured`() {
        val resolution = requireNotNull(
            derive(SamasaPada("सह", Vibhakti.TRTIYA), SamasaPada("उरस्")).samasaResolution,
        )
        assertEquals(listOf("6.3.82", "5.4.151"), resolution.transformationSutras)
        assertEquals(mapOf(0 to "स"), resolution.operations[0].memberEdits)
        assertEquals("क", resolution.operations[1].samasantaSuffix)
    }

    @Test
    fun `proper-name prohibition blocks inherited kap but not earlier nipatana`() {
        val blocked = requireNotNull(
            derive(
                SamasaPada("व्यूढ"), SamasaPada("उरस्"),
                setOf(SamasaSemanticRelation.PROPER_NAME),
            ).samasaResolution,
        )
        assertTrue("5.4.155" in blocked.prohibitedSutras)
        assertTrue("5.4.151" !in blocked.transformationSutras)

        val lexicalResult = derive(
            SamasaPada("त्रि"), SamasaPada("ककुद"),
            setOf(SamasaSemanticRelation.PROPER_NAME),
        )
        val lexical = requireNotNull(lexicalResult.samasaResolution)
        assertTrue("5.4.155" in lexical.prohibitedSutras)
        assertEquals(listOf("5.4.147"), lexical.transformationSutras)
        assertEquals("त्रिककुत्", lexicalResult.final.surface)
    }

    @Test
    fun `semantic gandha rules select exactly one specific samasanta`() {
        val cases = listOf(
            Triple(SamasaPada("सु"), emptySet(), "5.4.135"),
            Triple(SamasaPada("अल्प"), setOf(SamasaSemanticRelation.SMALL_QUANTITY), "5.4.136"),
            Triple(SamasaPada("पद्म"), setOf(SamasaSemanticRelation.QUALIFIER_QUALIFIED), "5.4.137"),
        )
        cases.forEach { (first, semantics, expected) ->
            val resolution = requireNotNull(derive(first, SamasaPada("गन्ध"), semantics).samasaResolution)
            assertEquals(listOf(expected), resolution.transformationSutras)
        }
    }

    @Test
    fun `vidvas plus alaya preserves alaya member order`() {
        val result = engine.derive(
            listOf(
                SamasaPada("विद्वस्", Vibhakti.SASTHI),
                SamasaPada("आलय"),
            ),
            SamasaType.TATPURUSA,
            outputLinga = Linga.PUMS,
        )
        assertEquals("विद्वदालय", requireNotNull(result.samasaResolution).compoundStem)
        assertEquals("विद्वदालयः", result.final.surface)
        assertEquals(listOf("8.2.72"), requireNotNull(result.samasaResolution).transformationSutras)
    }

    @Test
    fun `s final prior member undergoes regular rutva before voiced consonant`() {
        val result = engine.derive(
            listOf(
                SamasaPada("मनस्", Vibhakti.SASTHI),
                SamasaPada("रथ"),
            ),
            SamasaType.TATPURUSA,
            outputLinga = Linga.PUMS,
        )
        assertEquals(
            "मनोरथ",
            requireNotNull(result.samasaResolution).compoundStem,
            result.applications.joinToString { "${it.sutra}:${it.after.surface}" },
        )
        assertEquals("मनोरथः", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "8.2.66" })
        assertTrue(result.applications.any { it.sutra == "6.1.114" })
        assertTrue(result.applications.any { it.sutra == "6.1.87" })
    }

    private fun derive(
        first: SamasaPada,
        last: SamasaPada,
        semantics: Set<SamasaSemanticRelation> = emptySet(),
    ) = engine.derive(
        listOf(first, last),
        SamasaType.BAHUVRIHI,
        outputLinga = Linga.PUMS,
        semanticRelations = semantics,
    )
}
