package dev.panini.unadipatha.analysis

import dev.panini.dhatupatha.adadi.VaaDhatu
import dev.panini.dhatupatha.tanadi.KruDhatu
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class UnadiAnalyzerTest {

    @Test
    fun testAnalyzeStemVayu() {
        val analysis = UnadiAnalyzer.analyzeStem("वायु")
        assertTrue(analysis.isRudhi, "'वायु' should be recognized as a Rūḍhi Saṁjñā")
        assertEquals(StemClassification.RUDHI_PRATIPADIKA, analysis.classification)
        assertNotNull(analysis.etymologicalRoot)
        assertEquals("वा", analysis.etymologicalRoot?.upadesha)
        assertEquals("उण्", analysis.pratyaya)
        assertEquals("1.1", analysis.sutraNumber)
    }

    @Test
    fun testAnalyzeStemKarna() {
        val analysis = UnadiAnalyzer.analyzeStem("कर्ण")
        assertTrue(analysis.isRudhi, "'कर्ण' should be recognized as a Rūḍhi Saṁjñā")
        assertEquals(StemClassification.RUDHI_PRATIPADIKA, analysis.classification)
        assertEquals("कनिन्", analysis.pratyaya)
        assertEquals("4.1", analysis.sutraNumber)
    }

    @Test
    fun testAnalyzePairKruAndKanin() {
        val analysis = UnadiAnalyzer.analyzePair(KruDhatu(), "कनिन्")
        assertNotNull(analysis, "Analysis should not be null for (KruDhatu, कनिन्)")
        assertTrue(analysis.isRudhi)
        assertEquals("कर्ण", analysis.stem)
        assertEquals("4.1", analysis.sutraNumber)
    }

    @Test
    fun testAnalyzePairVaaAndUn() {
        val analysis = UnadiAnalyzer.analyzePair(VaaDhatu(), "उण्")
        assertNotNull(analysis, "Analysis should not be null for (VaaDhatu, उण्)")
        assertTrue(analysis.isRudhi)
        assertEquals("वायु", analysis.stem)
        assertEquals("1.1", analysis.sutraNumber)
    }
}
