package dev.panini.unadipatha

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UnadiDerivationEngineTest {

    @Test
    fun testDeriveUnadiForKruAndUn() {
        val result = UnadiDerivationEngine.derive("कृ", "उण्")
        assertTrue(result.applications.isNotEmpty() || result.final.surface.isNotEmpty())
        assertEquals("root", result.initial.terms.first().id)
        assertEquals("unadi_1.1", result.initial.terms[1].id)
    }

    @Test
    fun testDeriveUnadiForPaaAndTrn() {
        val result = UnadiDerivationEngine.derive("पा", "तृन्")
        assertTrue(result.initial.terms.size == 2)
        assertEquals("तृन्", result.initial.terms[1].upadesha)
    }
}
