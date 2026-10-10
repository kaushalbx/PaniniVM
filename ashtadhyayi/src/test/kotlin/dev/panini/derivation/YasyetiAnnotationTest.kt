package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya1.pada3.HalantyamSutra
import dev.panini.ashtadhyayi.adhyaya6.pada4.YasyetiCaSutra
import dev.panini.core.ItMarker
import dev.panini.shiksha.*
import kotlin.test.*

class YasyetiAnnotationTest {
    @Test fun `all existing normalizations preserve surviving annotations and original dropped affix`() {
        for ((upadesha, surface, normalized) in listOf(
            Triple("अण्", "अँऽ", "अँ"),
            Triple("इञ्", "इँञ्ऽ", "इँ"),
            Triple("आयन्", "आँयनऽ", "आँयन्"),
            Triple("एय्", "एँयऽ", "एँय्"),
            Triple("यञ्", "यँञ्ऽ", "यँ"),
        )) {
            val stem = DerivationTerm("stem", "गँर्गँऽ", TermKind.PRATIPADIKA, upadesha = "गर्ग", createdBySutra = "fixture")
            val affix = DerivationTerm("affix", surface, TermKind.PRATYAYA, upadesha = upadesha)
            val before = DerivationState(listOf(stem, affix), activeAdhikaras = setOf("6.4.1"))
            assertTrue(YasyetiCaSutra.matches(before))
            val result = YasyetiCaSutra.apply(before).state
            val changed = result.terms.single()
            assertEquals(stem.varnas.dropLast(1) + normalized.toVarnas(), changed.varnas, upadesha)
            assertTrue(changed.phonologicalText.effectiveVarnas[1].nasalized)
            assertTrue(changed.phonologicalText.effectiveVarnas.drop(stem.varnas.size - 1).any { it.nasalized })
            assertEquals(stem.id, changed.id)
            assertEquals(stem.upadesha, changed.upadesha)
            assertEquals(stem.createdBySutra, changed.createdBySutra)
            assertEquals(stem.varnas.lastIndex, result.substitutions.single().sourceVarnaIndex)
            assertEquals(surface, result.droppedTerms.single().originalSurfaceBeforeDrop)
            assertEquals(setOf(upadesha), changed.sourceSuffixUpadeshas)
            assertTrue(changed.orthographicSigns.any { it.sign == OrthographicSign.AVAGRAHA })
        }
    }

    @Test fun `normalization explicitly consumes an it designation retaining marker provenance`() {
        val raw = DerivationTerm("affix", "इञ्", TermKind.PRATYAYA, itProcessingPhase = ItProcessingPhase.RAW_UPADESHA)
        val designated = HalantyamSutra.apply(DerivationState(listOf(raw))).state.terms.single()
        val before = DerivationState(listOf(DerivationTerm("stem", "गर्ग", TermKind.PRATIPADIKA), designated),
            activeAdhikaras = setOf("6.4.1"))
        val after = YasyetiCaSutra.apply(before).state
        val dropped = after.droppedTerms.single()
        assertEquals("इञ्", dropped.originalSurfaceBeforeDrop)
        assertEquals("इञ्", dropped.upadesha)
        assertTrue(dropped.hasEffectiveMarker(ItMarker.NYIT))
        assertTrue(dropped.itDesignations.isEmpty())
        assertEquals(ItProcessingPhase.PROCESSED, dropped.itProcessingPhase)
        assertEquals("गर्गि", after.surface)
    }
}
