package dev.panini.derivation

import dev.panini.core.ItMarker
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class WholeAffixReplacementTest {
    private val designated = DerivationTerm(
        id = "affix", surface = "अप्", kind = TermKind.PRATYAYA, upadesha = "अप्",
        itMarkers = setOf(ItMarker.P), itProcessingPhase = ItProcessingPhase.DESIGNATED,
        itDesignations = listOf(ItDesignation(1, 3, marker = ItMarker.P, sutra = "1.3.3", designatedText = "प्")),
    )

    @Test fun `preserve policy remaps the exact surviving segment`() {
        val result = designated.replaceWholeAffix(
            "इप्", "इप्", "x", WholeAffixDesignationPolicy.PreserveAndRemap(
                listOf(ItDesignationRemap(1, 3, 1, 3)),
            ),
        )
        assertEquals("प्", result.itDesignations.single().designatedText)
        assertEquals(1, result.itDesignations.single().start)
        assertEquals(ItProcessingPhase.DESIGNATED, result.itProcessingPhase)
    }

    @Test fun `preserve policy rejects an unaccounted designation`() {
        assertFailsWith<IllegalArgumentException> {
            designated.replaceWholeAffix("इ", "इ", "x", WholeAffixDesignationPolicy.PreserveAndRemap(emptyList()))
        }
    }

    @Test fun `consume policy supersedes exact designations but preserves marker provenance`() {
        val result = designated.replaceWholeAffix("इ", "इ", "x", WholeAffixDesignationPolicy.Consume)
        assertEquals(emptyList(), result.itDesignations)
        assertEquals(ItProcessingPhase.PROCESSED, result.itProcessingPhase)
        assertEquals(setOf(ItMarker.P), result.sthaniProps?.itMarkers)
    }

    @Test fun `preserve policy can consume one superseded designation and remap another`() {
        val term = designated.copy(
            surface = "फक्",
            upadesha = "फक्",
            itDesignations = listOf(
                ItDesignation(0, 1, marker = ItMarker.T, sutra = "1.3.7", designatedText = "फ"),
                ItDesignation(1, 3, marker = ItMarker.KIT, sutra = "1.3.3", designatedText = "क्"),
            ),
        )
        val result = term.replaceWholeAffix(
            "आयन्क्",
            "फक्",
            "7.1.2",
            WholeAffixDesignationPolicy.PreserveAndRemap(
                remaps = listOf(ItDesignationRemap(1, 3, 4, 6)),
                consumed = listOf(ItDesignationConsumption(0, 1)),
            ),
        )

        assertEquals(listOf(ItMarker.KIT), result.itDesignations.map { it.marker })
        assertEquals(4 to 6, result.itDesignations.single().let { it.start to it.endExclusive })
    }

    @Test fun `fresh upadesha requires a new it processing pass`() {
        val result = designated.replaceWholeAffix("यप्", "यप्", "x", WholeAffixDesignationPolicy.FreshUpadesha)
        assertEquals(ItProcessingPhase.RAW_UPADESHA, result.itProcessingPhase)
        assertEquals(emptyList(), result.itDesignations)
        assertEquals(emptySet(), result.itMarkers)
    }

    @Test fun `consume for drop records provenance and leaves no pending designation`() {
        val result = consumeAffixForDrop(designated, "7.1.1")
        assertEquals("", result.surface)
        assertEquals("अप्", result.originalSurfaceBeforeDrop)
        assertEquals("7.1.1", result.droppedBySutra)
        assertEquals(ItProcessingPhase.PROCESSED, result.itProcessingPhase)
        assertEquals(emptyList(), result.itDesignations)
        assertEquals(setOf(ItMarker.P), result.sthaniProps?.itMarkers)
    }
}
