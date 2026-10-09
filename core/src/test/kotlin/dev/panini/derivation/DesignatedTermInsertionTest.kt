package dev.panini.derivation

import dev.panini.core.ItMarker
import kotlin.test.Test
import kotlin.test.assertEquals

class DesignatedTermInsertionTest {
    @Test
    fun `internal insertion reprojects designations on both members`() {
        val rawTarget = DerivationTerm("target", "अण्", TermKind.PRATYAYA)
        val target = rawTarget.copy(itDesignations = listOf(rawTarget.designateVarnaIt(1, ItMarker.NIT, "1.3.3")))
        val rawAugment = DerivationTerm("augment", "तुक्", TermKind.AGAMA)
        val augment = rawAugment.copy(itDesignations = listOf(rawAugment.designateVarnaIt(2, ItMarker.KIT, "1.3.3")))
        val merged = target.insertDesignatedTerm(augment, 1)
        assertEquals("अतुक्ण्", merged.surface)
        assertEquals(listOf(setOf(4), setOf(3)), merged.itDesignations.map { it.varnaIndices })
        assertEquals(listOf(5 to 7, 3 to 5), merged.itDesignations.map { it.start to it.endExclusive })
        assertEquals("अतु", merged.lopaOfDesignatedVarnas(merged.itDesignations).surface)
    }

    @Test
    fun `insertion retains nasalization and avagraha as distinct annotation`() {
        val target = DerivationTerm("target", "कँऽ", TermKind.PRATIPADIKA)
        val augment = DerivationTerm("augment", "तु", TermKind.AGAMA)
        val merged = target.insertDesignatedTerm(augment, 2)
        assertEquals("कँऽतु", merged.surface)
        assertEquals(true, merged.phonologicalText.effectiveVarnas[1].nasalized)
        assertEquals(target.varnas + augment.varnas, merged.varnas)
    }
}
