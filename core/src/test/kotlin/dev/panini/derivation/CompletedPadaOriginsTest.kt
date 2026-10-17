package dev.panini.derivation

import dev.panini.core.ItMarker
import dev.panini.shiksha.*
import kotlin.test.*

class CompletedPadaOriginsTest {
    private val left = setOf("left-pada")
    private val right = setOf("right-pada")
    private fun state() = DerivationState(listOf(DerivationTerm("term", "अइन्", TermKind.PRATIPADIKA,
        completedPadaOrigins = listOf(left, right, right))))

    @Test fun `one to many replacement and insertion project exact occurrences`() {
        val replaced = state().replaceTermVarna("term", 0, listOf(Svara.A, Vyanjana.RA), "fixture")
        assertEquals(listOf(left, left, right, right), replaced.terms.single().completedPadaOrigins)
        val inserted = state().insertTermVarnas("term", 1, listOf(Vyanjana.KA), "fixture")
        assertEquals(listOf(left, right, right, right), inserted.terms.single().completedPadaOrigins)
    }

    @Test fun `range coalescence unions only contributing original padas`() {
        val after = state().replaceTermVarnaRange("term", 0, 2, listOf(Svara.E), mapOf(0 to 0), "fixture")
        assertEquals(listOf(left + right, right), after.terms.single().completedPadaOrigins)
    }

    @Test fun `range and occurrence deletions retain remaining owners`() {
        val ranged = state().deleteTermVarnas("term", 0, 2, "fixture")
        assertEquals(listOf(right), ranged.terms.single().completedPadaOrigins)
        val selected = state().deleteTermVarnaOccurrences("term", setOf(0, 2), "fixture")
        assertEquals(listOf(right), selected.terms.single().completedPadaOrigins)
    }

    @Test fun `whole affix exact position map retains surviving ownership`() {
        val term = state().terms.single().copy(kind = TermKind.PRATYAYA)
        val after = term.replaceWholeAffixWithVarnaMapping(listOf(Vyanjana.NA), mapOf(2 to 0), emptySet(), "fixture")
        assertEquals(listOf(right), after.completedPadaOrigins)
    }

    @Test fun `it lopa removes only designated occurrence ownership`() {
        val term = DerivationTerm("term", "अरुँ", TermKind.PRATIPADIKA,
            completedPadaOrigins = listOf(left, right, right))
        val designation = term.designateVarnaIt(2, ItMarker.U, "1.3.2")
        val after = term.lopaOfDesignatedVarnas(listOf(designation))
        assertEquals(listOf(left, right), after.completedPadaOrigins)
    }

    @Test fun `legacy string merger cannot fabricate ownership for a consumed completed pada`() {
        val state = DerivationState(listOf(
            DerivationTerm("left", "अ", TermKind.PRATIPADIKA, completedPadaOrigins = listOf(left)),
            DerivationTerm("right", "अ", TermKind.PRATIPADIKA, completedPadaOrigins = listOf(right))))
        assertFailsWith<IllegalArgumentException> {
            state.mergeTermsByVarnaSubstitution("left", "right", "आ", Svara.A, listOf(Svara.AA), "fixture")
        }
    }
}
