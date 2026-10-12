package dev.panini.derivation

import dev.panini.core.ItMarker
import kotlin.test.*

class PrecedingAugmentCompositionTest {
    @Test fun `beginning augment composition preserves target annotations identity and exact it positions`() {
        val raw = DerivationTerm("target", "इँऽट्", TermKind.DHATU, createdBySutra = "fixture")
        val target = raw.copy(deferredItDesignations = listOf(raw.designateVarnaIt(1, ItMarker.T, "1.3.3")),
            itProcessingPhase = ItProcessingPhase.DEFERRED_SUBSTITUTION)
        val augment = DerivationTerm("augment", "आँऽ", TermKind.AGAMA,
            augmentTargetId = target.id, mergeIntoAugmentTarget = false, establishedBySutras = setOf("1.1.46"))
        val state = DerivationState(listOf(augment, target))
        val after = state.concatenatePrecedingAugment(target.id, augment.id, "test")
        val merged = after.terms.single()
        assertEquals(augment.varnas + target.varnas, merged.varnas)
        assertEquals(target.id, merged.id)
        assertEquals(target.upadesha, merged.upadesha)
        assertEquals(target.createdBySutra, merged.createdBySutra)
        assertEquals(target.itProcessingPhase, merged.itProcessingPhase)
        assertEquals(setOf(2), merged.deferredItDesignations.single().varnaIndices)
        assertTrue(merged.phonologicalText.effectiveVarnas.take(2).all { it.nasalized })
        assertEquals(setOf(1, 2), merged.orthographicSigns.map { it.afterVarnaCount }.toSet())
        assertEquals(augment.surface, after.droppedTerms.single().originalSurfaceBeforeDrop)
        assertEquals(state.substitutions, after.substitutions)
        assertFailsWith<IllegalArgumentException> {
            DerivationState(listOf(target, augment)).concatenatePrecedingAugment(target.id, augment.id, "test")
        }
        assertFailsWith<IllegalArgumentException> {
            DerivationState(listOf(augment.copy(itProcessingPhase = ItProcessingPhase.RAW_UPADESHA), target))
                .concatenatePrecedingAugment(target.id, augment.id, "test")
        }
    }
}
