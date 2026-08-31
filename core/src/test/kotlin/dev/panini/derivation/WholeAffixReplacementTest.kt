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

    @Test fun `fresh upadesha requires a new it processing pass`() {
        val result = designated.replaceWholeAffix("यप्", "यप्", "x", WholeAffixDesignationPolicy.FreshUpadesha)
        assertEquals(ItProcessingPhase.RAW_UPADESHA, result.itProcessingPhase)
        assertEquals(emptyList(), result.itDesignations)
        assertEquals(emptySet(), result.itMarkers)
    }
}
