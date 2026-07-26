package dev.panini.analysis

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DhatuSemanticRelationsTest {

    @Test
    fun `getSynonyms returns expected synonyms for bhu and gam`() {
        val bhuSynonyms = DhatuSemanticRelations.getSynonyms("भू")
        assertTrue("अस्" in bhuSynonyms, "bhu should have as as synonym")
        assertTrue("विद्" in bhuSynonyms, "bhu should have vid as synonym")

        val gamSynonyms = DhatuSemanticRelations.getSynonyms("गम्")
        assertTrue("या" in gamSynonyms, "gam should have ya as synonym")
        assertTrue("व्रज्" in gamSynonyms, "gam should have vraj as synonym")
    }

    @Test
    fun `getAntonyms returns expected antonyms for gam and da`() {
        val gamAntonyms = DhatuSemanticRelations.getAntonyms("गम्")
        assertTrue("आगम्" in gamAntonyms, "gam antonym should be agam")

        val daAntonyms = DhatuSemanticRelations.getAntonyms("दा")
        assertTrue("ग्रह्" in daAntonyms, "da antonym should be grah")
    }

    @Test
    fun `getByCategory returns roots in semantic categories`() {
        val motionRoots = DhatuSemanticRelations.getByCategory(SemanticRelation.MOTION)
        assertTrue("गम्" in motionRoots)
        assertTrue("या" in motionRoots)

        val speechRoots = DhatuSemanticRelations.getByCategory(SemanticRelation.SPEECH)
        assertTrue("वद्" in speechRoots)
        assertTrue("ब्रू" in speechRoots)
    }

    @Test
    fun `getCategoriesFor returns categories for jna`() {
        val categories = DhatuSemanticRelations.getCategoriesFor("ज्ञा")
        assertTrue(SemanticRelation.COGNITION in categories)
    }
}
