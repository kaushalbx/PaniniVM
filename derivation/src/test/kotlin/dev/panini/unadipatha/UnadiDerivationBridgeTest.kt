package dev.panini.unadipatha

import dev.panini.dhatupatha.tanadi.KruDhatu
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.TermKind
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UnadiDerivationBridgeTest {

    @Test
    fun testCreateInitialState() {
        val dhatu = KruDhatu()
        val matches = UnadiPatha.findSamjna(dhatu, "कनिन्")
        assertTrue(matches.isNotEmpty(), "Should find कनिन् for KruDhatu")

        val match = matches.first()
        val initialState = UnadiDerivationBridge.createInitialState(dhatu, match)

        assertEquals(2, initialState.terms.size)
        assertEquals(TermKind.DHATU, initialState.terms[0].kind)
        assertEquals(TermKind.PRATYAYA, initialState.terms[1].kind)
        assertEquals("कनिन्", initialState.terms[1].upadesha)
        assertEquals(DerivationStage.PRATYAYA_SELECTED, initialState.stage)
    }
}
