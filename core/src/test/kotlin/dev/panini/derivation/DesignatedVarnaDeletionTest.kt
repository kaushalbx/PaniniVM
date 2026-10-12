package dev.panini.derivation

import dev.panini.core.ItMarker
import kotlin.test.*

class DesignatedVarnaDeletionTest {
    @Test fun `deletion remaps surviving active and deferred designations`() {
        val source = DerivationTerm("suffix", "अट्", TermKind.PRATYAYA)
        val designation = source.designateVarnaIt(1, ItMarker.T, "1.3.3")
        for (deferred in listOf(false, true)) {
            val term = source.copy(
                itDesignations = if (deferred) emptyList() else listOf(designation),
                deferredItDesignations = if (deferred) listOf(designation) else emptyList(),
                itProcessingPhase = if (deferred) ItProcessingPhase.DEFERRED_SUBSTITUTION else ItProcessingPhase.DESIGNATED,
            )
            val state = DerivationState(listOf(term))
            val after = state.deleteTermVarnas(term.id, 0, 1, "test")
            val changed = after.terms.single()
            val remapped = (changed.itDesignations + changed.deferredItDesignations).single()
            assertEquals(setOf(0), remapped.varnaIndices)
            assertEquals("ट्", changed.surface)
            assertEquals(changed.surface, remapped.designatedText)
            assertEquals(term.itProcessingPhase, changed.itProcessingPhase)
            assertEquals(designation.marker, remapped.marker)
            assertEquals(designation.sutra, remapped.sutra)
            assertEquals(0, after.substitutions.single().sourceVarnaIndex)
            assertFailsWith<IllegalArgumentException> { state.deleteTermVarnas(term.id, 1, 1, "test") }
        }
    }
}
