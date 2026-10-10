package dev.panini.derivation

import dev.panini.shiksha.*
import kotlin.test.*

class TermPrefixTransferTest {
    @Test fun `prefix transfer preserves nasal tokens signs and both identities`() {
        val left = DerivationTerm("left", "इँऽ", TermKind.PRATIPADIKA, createdBySutra = "left-source")
        val right = DerivationTerm("right", "अँऽय्ँऽ", TermKind.AGAMA, upadesha = "सीयुट्", createdBySutra = "right-source")
        val state = DerivationState(listOf(left, right))
        val after = state.transferFollowingPrefix(left.id, right.id, 1, "test")
        assertEquals(listOf(Svara.I, Svara.A), after.terms.first().varnas)
        assertTrue(after.terms.first().phonologicalText.effectiveVarnas.all { it.nasalized })
        assertEquals(listOf(1, 2), after.terms.first().orthographicSigns.map { it.afterVarnaCount })
        assertEquals(listOf(Vyanjana.YA), after.terms.last().varnas)
        assertTrue(after.terms.last().phonologicalText.effectiveVarnas.single().nasalized)
        assertEquals(listOf(1), after.terms.last().orthographicSigns.map { it.afterVarnaCount })
        assertEquals(listOf(left.id, right.id), after.terms.map { it.id })
        assertEquals(listOf(left.upadesha, right.upadesha), after.terms.map { it.upadesha })
        assertEquals(listOf(left.createdBySutra, right.createdBySutra), after.terms.map { it.createdBySutra })
        assertTrue(after.substitutions.isEmpty())
        assertTrue(after.droppedTerms.isEmpty())
        assertFailsWith<IllegalArgumentException> { state.transferFollowingPrefix(right.id, left.id, 1, "test") }
        assertFailsWith<IllegalArgumentException> { state.transferFollowingPrefix(left.id, right.id, 3, "test") }
        val raw = state.replaceTerm(right.id, right.copy(itProcessingPhase = ItProcessingPhase.RAW_UPADESHA))
        assertFailsWith<IllegalArgumentException> { raw.transferFollowingPrefix(left.id, right.id, 1, "test") }
    }
}
