package dev.panini.derivation

import dev.panini.core.ItMarker
import dev.panini.shiksha.OrthographicSign
import dev.panini.shiksha.OrthographicSignPlacement
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ItVarnaLopaTest {
    @Test
    fun `lopa preserves unrelated nasalization and remaps avagraha without making it a varna`() {
        val term = DerivationTerm("test", "कँउँऽ", TermKind.PRATYAYA)
        val designation = term.designateVarnaIt(2, ItMarker.U, "1.3.2")
        val result = term.lopaOfDesignatedVarnas(listOf(designation))
        assertEquals("कँऽ", result.surface)
        assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, 2)), result.orthographicSigns)
        assertEquals(term.varnas.take(2), result.varnas)
    }

    @Test
    fun `same written offsets cannot silently shift a designated token`() {
        val term = DerivationTerm("affix", "अप्", TermKind.PRATYAYA)
        val designated = term.copy(itDesignations = listOf(term.designateVarnaIt(1, ItMarker.P, "1.3.3")))
        assertFailsWith<IllegalArgumentException> {
            DerivationState(listOf(designated)).substituteTermSurface("affix", "कप्", 'अ', "क", "test")
        }
    }

    @Test
    fun `stale provenance and out of range token positions are rejected`() {
        val term = DerivationTerm("affix", "अप्", TermKind.PRATYAYA)
        val designation = term.designateVarnaIt(1, ItMarker.P, "1.3.3")
        assertFailsWith<IllegalArgumentException> {
            term.copy(surface = "अक्").lopaOfDesignatedVarnas(listOf(designation))
        }
        assertFailsWith<IllegalArgumentException> {
            term.lopaOfDesignatedVarnas(listOf(designation.copy(varnaIndices = setOf(20))))
        }
    }
}
